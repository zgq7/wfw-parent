package com.wfw.auth.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author liaonanzhou
 * @date 2021/12/28 18:26
 * @description
 **/
@Component
@Aspect
@Slf4j
public class ClientDetailsServiceAspect {

    @Pointcut(value = "execution(public * org.springframework.security.oauth2.provider.ClientDetailsService.loadClientByClientId(*))")
    public void interceptor() {

    }

    @AfterReturning(value = "interceptor()", returning = "returned")
    public Object afterReturning(Object returned) {
        log.info("ClientDetailsServiceAspect returned = {}", returned);
        BaseClientDetails clientDetails = (BaseClientDetails) returned;
        clientDetails.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2));
        log.info("ClientDetailsServiceAspect reChanged = {}", clientDetails);
        return clientDetails;
    }

}
