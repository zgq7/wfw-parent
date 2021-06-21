package com.wfw.auth.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wfw.auth.controller.dto.UserLoginDto;
import com.wfw.auth.controller.vo.AuthToken;
import com.wfw.authcommon.common.constants.GrantTypeConstants;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.util.ObjectMapperUtil;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final ConsumerTokenServices consumerTokenServices;
    private final TokenEndpoint tokenEndpoint;
    private final AuthorizationEndpoint authorizationRequest;
    private final WhitelabelApprovalEndpoint whitelabelApprovalEndpoint;
    private final WhitelabelErrorEndpoint whitelabelErrorEndpoint;

    public AuthController(ConsumerTokenServices consumerTokenServices, TokenEndpoint tokenEndpoint,
                          AuthorizationEndpoint authorizationRequest, WhitelabelApprovalEndpoint whitelabelApprovalEndpoint,
                          WhitelabelErrorEndpoint whitelabelErrorEndpoint) {
        this.consumerTokenServices = consumerTokenServices;
        this.tokenEndpoint = tokenEndpoint;
        this.authorizationRequest = authorizationRequest;
        this.whitelabelApprovalEndpoint = whitelabelApprovalEndpoint;
        this.whitelabelErrorEndpoint = whitelabelErrorEndpoint;
    }

    /**
     * 自定义登录接口
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, Principal principal) throws HttpRequestMethodNotSupportedException {
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

    /**
     * 自定义认证接口
     **/
    @GetMapping(value = "/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
                                  SessionStatus sessionStatus, Principal principal) {
        logger.info("authorize...");
        return authorizationRequest.authorize(model, parameters, sessionStatus, principal);
    }

    /**
     * 授权确认
     **/
    @GetMapping(value = "/confirm_access")
    public ModelAndView confirm(Map<String, Object> model, HttpServletRequest request) {
        try {
            return whitelabelApprovalEndpoint.getAccessConfirmation(model, request);
        } catch (Exception e) {
            throw new ServiceException(3000, "确认授权异常");
        }
    }

}
