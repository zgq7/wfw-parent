package com.wfw.feign.feign;

import com.wfw.framework.web.WebApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liaonanzhou
 * @date 2021-02-04 11:40
 * @description
 */
@FeignClient(value = "WFW-PROVIDER", path = "/wfw/provider")
public interface WebFeign {

    @GetMapping(value = "/web/getSome")
    WebApiResponse<String> getSome();

}
