package com.wfw.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author liaonanzhou
 * @date 2021/5/28 15:14
 * @description
 **/
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-TOKEN", UUID.randomUUID().toString());
    }

}
