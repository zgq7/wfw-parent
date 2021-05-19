package com.wfw.auth.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wfw.auth.controller.dto.UserLoginDto;
import com.wfw.auth.controller.vo.AuthToken;
import com.wfw.authcommon.common.constants.GrantTypeConstants;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.util.ObjectMapperUtil;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
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
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request, Principal principal) throws HttpRequestMethodNotSupportedException {
        Map<String, String> mapDto = ObjectMapperUtil.str2Obj(userLoginDto, new TypeReference<Map<String, String>>() {
        });
        mapDto.put(GrantTypeConstants.GRANT_TYPE, GrantTypeConstants.PASSWORD);

        OAuth2AccessToken token;
        try {
            token = tokenEndpoint.postAccessToken(principal, mapDto).getBody();
            if (token == null) {
                throw new ServiceException("登录异常");
            }
        } catch (Exception e) {
            if (e instanceof InvalidGrantException) {
                throw new ServiceException("用户名密码错误");
            } else {
                throw e;
            }
        }
        return response(WebApiResponse.ok(AuthToken.build(token)));
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
