package com.wfw.auth.controller;

import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaonanzhou
 * @date 2021/5/14 15:23
 * @description
 **/
@RequestMapping(value = "/auth")
@RestController
public class AuthTestController extends WebApiController {

    private final ConsumerTokenServices consumerTokenServices;

    public AuthTestController(ConsumerTokenServices consumerTokenServices) {
        this.consumerTokenServices = consumerTokenServices;
    }


    @GetMapping(value = "/demo")
    public ResponseEntity<String> demo() {
        Map<String, Boolean> result = new HashMap<>();
        result.put("访问结果", true);
        return response(WebApiResponse.ok(result));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(HttpServletRequest request) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("登录结果", true);
        return response(WebApiResponse.ok(result));
    }

//    @PostMapping(value = "/login")
//    public ResponseEntity<String> login(@RequestParam("username") String username,
//                                        @RequestParam("password") String password) {
//        Map<String, Boolean> result = new HashMap<>();
//        result.put("登出结果", true);
//        return response(WebApiResponse.ok(result));
//    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace("bearer ", "");
        consumerTokenServices.revokeToken(accessToken);
        Map<String, Boolean> result = new HashMap<>();
        result.put("登出结果", true);
        return response(WebApiResponse.ok(result));
    }

}
