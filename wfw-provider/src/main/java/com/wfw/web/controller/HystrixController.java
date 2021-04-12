package com.wfw.web.controller;

import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liaonanzhou
 * @date 2021/4/12 16:04
 * @description
 **/
@Controller
@RequestMapping(value = "/hystrix")
public class HystrixController extends WebApiController {

    @GetMapping(value = "/pass")
    public ResponseEntity<String> pass(@RequestParam Integer xp) {
        WebApiResponse<Boolean> webApiResponse = WebApiResponse.ok();
        webApiResponse.setData(true);
        if ((xp / 2) == 0) {
            throw new ServiceException();
        }

        return response(webApiResponse);
    }
}
