package com.techolution.bi;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class HmacAuthorizationInterceptor implements RequestInterceptor {

    private static final String HEADER_NAME = "Authorization";
    /**
     * The encoding used for the header
     */
    private static final String ENCODING = "UTF-8";

    /**
     * Identifier for the MD5 algorithm as expected from {@link MessageDigest java.security.MessageDigest}
     */
    private static final String MD5_ALGORITHM_NAME = "MD5";

    /**
     * Identifier for the HmacSHA256 algorithm as expected from {@link javax.crypto.Mac}
     */
    private static final String HMAC_SHA256_ALGORITHM_NAME = "HmacSHA256";

    /**
     * The number of characters from the signature used in the header
     */
    private static final int HEADER_SIGNATURE_LENGTH = 10;

    /**
     * The symbol used to separate the parts of the header's value
     */
    private static final String HEADER_PARTS_SEPARATOR = " ";

    /**
     * A prefix used for the header's value
     */
    private static final String HEADER_PREFIX = "HMAC ";


    private String partnerId;

    /**
     * An authentication secret key for the partner
     */
    public String secretKey;

    /**
     * UNIX time (the number of seconds since Epoch UTC (January 01, 1970)) used to identify a request
     */
    private long timestampInSeconds;

    /**
     * A UUID used to identify a request
     */
    private UUID uuid;

    /**
     * The HTTP method of the request
     */
    @Autowired
    public HmacAuthorizationInterceptor(@Value("${hmac.apikey}") final String apikey, @Value("${hmac.secret}") final String secretKey) {
        super();
        this.partnerId = apikey;
        this.secretKey = secretKey;
    }

    String nonce;
    String IsoTime;

    void setNonce() {
        nonce = java.util.UUID.randomUUID().toString();
    }

    void setTstamp() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        IsoTime = df.format(new Date());
    }


    /**
     * Returns the authorization header value for the Payment API and returns it. The header value should be in follow
     * the pattern: "hmac {partnerId}:{signature}:{nonce}:{timestamp}" where:
     * <ul>
     * <li>{partner} the partner id</li>
     * <li>{signature} the first 10 characters of a Hmac Sha256 hash used as signature</li>
     * <li>{nonce} a UUID that is unique for each header</li>
     * <li>{timestamp} the partner id</li>
     *
     * @return the value of the authorization header
     */
    public String getHeaderValue()  {
        String headerValue = null;
        try {
            headerValue = constructHeaderValueFromSourceData();
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return headerValue;
    }

    /**
     * Returns the name of the authorization header's name
     *
     * @return the name of the authorization header
     */
    public String getHeaderName() {
        return HEADER_NAME;
    }

    /**
     * Constructs the authorization header value for the Payment API from the header's source data
     *
     * @return the value of the authorization header
     * @throws UnsupportedEncodingException in case the {@link #ENCODING header's encoding} is not supported
     * @throws NoSuchAlgorithmException     in case any of the hashing algorithms used is not supported
     * @throws InvalidKeyException          in case the privateSecretKey is inappropriate
     */
    private String constructHeaderValueFromSourceData() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String message = generateMessageToBeSigned();
        String signature = sign(message);
        String header = buildAuthorizationHeaderString(signature);

        return header;
    }


    /**
     * Generates a string (called "message") used to create signature for the header. This message is constructed from
     * the request data, the {@link #partnerId partner ID}, the {@link #timestampInSeconds} and the {@link #uuid}
     *
     * @return a message used to create a header's signature
     * @throws UnsupportedEncodingException in case the {@link #ENCODING} is not supported by the URL encoder or
     *                                      string-to-byte encoder
     * @throws NoSuchAlgorithmException     in case the hashing algorithm is not supported
     */
    private String generateMessageToBeSigned() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        setNonce();
        setTstamp();
        String messageToBeSigned = nonce+IsoTime;
        return messageToBeSigned;
    }

    /**
     * Creates a HMAC signature string for the header
     *
     * @param message an input message to be signed
     * @return the provided message hashed with HMAC-SHA256 using the {@link #}
     * @throws UnsupportedEncodingException in case the charset used to encode the message into a byte array
     * @throws InvalidKeyException          in case the {@link #} is inappropriate for MAC
     * @throws NoSuchAlgorithmException     in case the selected MAC algorithm (HMAC-SHA256) is not supported
     */
    private String sign(String message) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        byte[] decodedSecretKeyByteArray = getDecodedSecretKeyAsByteArray();
        byte[] messageByteArray = message.getBytes(ENCODING);

        byte[] hash = createSha256Hash(decodedSecretKeyByteArray, messageByteArray);
        String encodedHash = Base64.encodeBase64String(hash);

        return encodedHash;
    }

    /**
     * Builds a authorization header value using part of the provided signature by joining all the header parts (the
     * {@link #partnerId partner ID}, the first characters of the signature, the {@link #uuid} and the
     * {@link #HEADER_PREFIX prefix} to the string
     *
     * @param fullSignature the full signature for the header
     * @return an authorization header that uses the provided signature
     */
    private String buildAuthorizationHeaderString(String fullSignature) {
        String timestampAsString =IsoTime;
        String headerSignature = fullSignature.substring(0, HEADER_SIGNATURE_LENGTH);

        String joinedHeaderParts = String.format("keyId=%s,nonce=%s,timestamp=%s,signature=%s",
                partnerId, nonce, IsoTime,fullSignature);
        String headerValue = HEADER_PREFIX + joinedHeaderParts;
        return headerValue;
    }

    /**
     * Returns the secret key decoded with Base64
     *
     * @return the decoded secret key
     */
    private byte[] getDecodedSecretKeyAsByteArray() {
        byte[] decodedSecretKey = Base64.decodeBase64(secretKey);

        return decodedSecretKey;
    }

    /**
     * Creates a MD5 hash from the provided input
     *
     * @param input the input string to hash
     * @return the creates hash as binary data
     * @throws NoSuchAlgorithmException     in case the selected algorithm (MD4) is not supported
     * @throws UnsupportedEncodingException in case the encoding of the input character set is not supported
     */
    private static byte[] createMd5Hash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5Digest = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
        md5Digest.update(input.getBytes(ENCODING));
        byte[] hash = md5Digest.digest();

        return hash;
    }

    /**
     * Creates HmacSHA256 hash of a message using the provided secret key
     *
     * @param secretKey a secret key used to create the hash
     * @param message   the message to be hashed and authenticated/validated later
     * @return the created hash as binary data
     * @throws NoSuchAlgorithmException in case the selected algorithm (HmacSHA256) is not supported
     * @throws InvalidKeyException      in case the provided key is inappropriate for the MAC
     */
    private static byte[] createSha256Hash(byte[] secretKey, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac algorithm = Mac.getInstance(HMAC_SHA256_ALGORITHM_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, HMAC_SHA256_ALGORITHM_NAME);
        algorithm.init(secretKeySpec);
        byte[] hash = algorithm.doFinal(message);
        return hash;
    }


    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", getHeaderValue());
    }

}
