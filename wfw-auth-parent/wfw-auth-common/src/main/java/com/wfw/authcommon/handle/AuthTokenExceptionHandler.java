package com.wfw.authcommon.handle;

import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author liaonanzhou
 * @date 2021/5/17 17:00
 * @description 用户密码错误异常处理器
 **/
public class AuthTokenExceptionHandler implements WebResponseExceptionTranslator<OAuth2Exception> {


    private static final Logger logger = LoggerFactory.getLogger(AuthTokenExceptionHandler.class);

    @Override
    public ResponseEntity translate(Exception e) {
        logger.error("授权异常:{}", e.getMessage());
        return new ResponseEntity<>(WebApiResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), "授权异常"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
