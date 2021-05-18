package com.wfw.authclient.handle;

import com.wfw.authclient.helper.FilterHelper;
import com.wfw.framework.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liaonanzhou
 * @date 2021/5/17 17:11
 * @description 客户端凭证错误异常处理器
 **/
@Component
public class AuthClientExceptionHandler implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(AuthClientExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) {
        logger.error("客户端认证异常 ", authException);
        //logger.error("客户端认证异常:{}", authException.getMessage());

        Throwable cause = authException.getCause();
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        if (cause instanceof InvalidTokenException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            FilterHelper.errorResponse(response, new ServiceException());
        } else {
            if (authException instanceof InsufficientAuthenticationException) {
                //凭证并没有认证通过
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                FilterHelper.errorResponse(response, new ServiceException("客户端认证异常"));
                return;
            }
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            FilterHelper.errorResponse(response, new ServiceException("服务器异常"));
        }

    }

}
