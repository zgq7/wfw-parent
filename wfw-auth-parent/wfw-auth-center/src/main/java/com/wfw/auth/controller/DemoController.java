package com.wfw.auth.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaonanzhou
 * @date 2021/6/25 16:35
 * @description
 **/
@RestController
@RequestMapping(value = "/demo")
public class DemoController extends WebApiController {

    @GetMapping(value = "/admin")
    public ResponseEntity<String> admin() {
        return response(WebApiResponse.ok());
    }

    @GetMapping(value = "/sp-admin")
    public ResponseEntity<String> spAdmin() {
        return response(WebApiResponse.ok());
    }
}
