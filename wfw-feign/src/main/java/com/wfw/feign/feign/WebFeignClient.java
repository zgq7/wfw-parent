package com.wfw.feign.feign;

import com.wfw.framework.web.WebApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liaonanzhou
 * @date 2021-02-04 11:40
 * @description
 */
@FeignClient(value = "WFW-PROVIDER")
public interface WebFeignClient {

    @GetMapping(value = "/getSome")
    WebApiResponse<String> getSome();


}
