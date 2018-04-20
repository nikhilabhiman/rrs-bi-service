package com.techolution.bi;

//import feign.Contract;
import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
