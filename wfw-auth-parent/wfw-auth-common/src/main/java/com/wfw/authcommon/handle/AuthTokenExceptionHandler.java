package com.wfw.authcommon.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author liaonanzhou
 * @date 2021/5/17 17:00
 * @description 用户密码错误异常处理器
 **/
public class AuthTokenExceptionHandler implements WebResponseExceptionTranslator<OAuth2Exception> {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenExceptionHandler.class);

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        //logger.error("授权异常:{}", e.getMessage());
        logger.error("授权异常:", e);
        //return new ResponseEntity<>(WebApiResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), "授权异常"), HttpStatus.INTERNAL_SERVER_ERROR);
//        OAuth2Exception auth2Exception = (OAuth2Exception) e;
//        return ResponseEntity.ok(auth2Exception);

        return new DefaultWebResponseExceptionTranslator().translate(e);
    }

}
