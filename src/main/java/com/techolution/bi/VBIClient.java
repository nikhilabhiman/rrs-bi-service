package com.techolution.bi;


import com.netflix.hystrix.HystrixCommand;
import com.techolution.bi.model.BiTransaction;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="VBI",url = "http://localhost:8080/",configuration = BiFiegnConfig.class)
//@FeignClient(name="VBI",url = "https://esafe.sandbox.vbp.io",configuration = BiFiegnConfig.class)
public interface VBIClient {

    @RequestLine("GET /api/electronicsafes?safeguid={safeguid}")
    String getEndPoints(@Param("safeguid") String safeguid);

    @RequestLine("GET /api/electronicsafes?safeguid={safeguid}")
    HystrixCommand<String> getBILinks(@Param("safeguid") String safeguid);

    @RequestLine("POST /api/electronicsafes/createtransaction")
    BiTransaction createTransaction(@RequestBody BiTransaction transaction);

    @RequestLine("POST /api/electronicsafes/createtransaction")
    HystrixCommand<BiTransaction> getString(@RequestBody BiTransaction transaction);


}
