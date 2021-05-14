package com.wfw.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaonanzhou
 * @date 2021/5/14 15:23
 * @description
 **/
@RequestMapping(value = "/auth")
@RestController
public class AuthTestController {

    @GetMapping(value = "/demo")
    public Map demo() {
        Map<String, Boolean> result = new HashMap<>();
        result.put("访问结果", true);
        return result;
    }

}
