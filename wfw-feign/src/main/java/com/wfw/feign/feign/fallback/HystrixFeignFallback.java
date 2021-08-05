package com.wfw.feign.feign.fallback;

import com.wfw.feign.feign.HystrixFeign;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author liaonanzhou
 * @date 2021/8/4 10:21
 * @description
 **/
@Component
public class HystrixFeignFallback implements FallbackFactory<HystrixFeign> {

    @Override
    public HystrixFeign create(Throwable throwable) {
        return new HystrixFeign() {
            @Override
            public WebApiResponse<String> pass(Integer xp) {
                return WebApiResponse.build(new ServiceException());
            }
        };
    }
}
