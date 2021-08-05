package com.wfw.feign.feign;

import com.wfw.framework.web.WebApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liaonanzhou
 * @date 2021/4/12 16:18
 * @description
 **/
@FeignClient(value = "WFW-PROVIDER", path = "/wfw/provider")
public interface HystrixFeign {

    @GetMapping(value = "/hystrix/pass")
    WebApiResponse<String> pass(@RequestParam Integer xp);
}
