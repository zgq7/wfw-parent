package com.wfw.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wfw.auth.controller.dto.UserLoginDto;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaonanzhou
 * @date 2021/5/14 15:23
 * @description
 **/
@RequestMapping(value = "/auth")
@RestController
public class AuthController extends WebApiController {

    private final ConsumerTokenServices consumerTokenServices;
    private final TokenEndpoint tokenEndpoint;

    public AuthController(ConsumerTokenServices consumerTokenServices, TokenEndpoint tokenEndpoint) {
        this.consumerTokenServices = consumerTokenServices;
        this.tokenEndpoint = tokenEndpoint;
    }

    /**
     * 自定义测试接口
     */
    @GetMapping(value = "/demo")
    public ResponseEntity<String> demo() {
        Map<String, Boolean> result = new HashMap<>();
        result.put("访问结果", true);
        return response(WebApiResponse.ok(result));
    }

    /**
     * 自定义登录接口
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request, Principal principal) {
        String basic = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Basic ", "");

        Map<String, String> mapDto = new HashMap<>();
        mapDto.put("username", userLoginDto.getUsername());
        mapDto.put("password", userLoginDto.getPassword());
        mapDto.put("grant_type", "password");

        try {
            OAuth2AccessToken token = tokenEndpoint.postAccessToken(principal, mapDto).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("登录结果", true);
        return response(WebApiResponse.ok(result));
    }

    /**
     * 自定义登出接口
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace("bearer ", "");
        consumerTokenServices.revokeToken(accessToken);
        Map<String, Boolean> result = new HashMap<>();
        result.put("登出结果", true);
        return response(WebApiResponse.ok(result));
    }

}
