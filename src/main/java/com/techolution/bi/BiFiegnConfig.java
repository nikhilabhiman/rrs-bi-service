package com.techolution.bi;

//import feign.Contract;
import feign.Contract;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class BiFiegnConfig {

    HmacAuthorizationInterceptor interceptor;

    public BiFiegnConfig(HmacAuthorizationInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public HmacAuthorizationInterceptor hmacAuthRequestInterceptor() {
        return interceptor;
    }

    //    @Bean
//    public RequestInterceptor requestInterceptor() {
//
//        return new BasicAuthRequestInterceptor("abc","def") {
//
//            @Override
//            public void apply(RequestTemplate template) {
//                template.header(HttpHeaders.AUTHORIZATION, "abcd");
//            }
//        };
//    }

//    @Bean
//    public RequestInterceptor hmacAuthRequestInterceptor() {
//        //return new HmacAuthorizationInterceptor("abc", "def");
//        return new BasicAuthRequestInterceptor("abc", "def");
//
//    }
}
