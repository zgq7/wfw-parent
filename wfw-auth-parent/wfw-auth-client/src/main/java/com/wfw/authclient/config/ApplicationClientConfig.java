package com.wfw.authclient.config;

import com.wfw.authcommon.handle.AuthClientExceptionHandler;
import com.wfw.authcommon.handle.AuthTokenExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:27
 * @description
 **/
@Configuration
public class ApplicationClientConfig {

    @Bean
    public AuthClientExceptionHandler authClientExceptionHandler() {
        return new AuthClientExceptionHandler();
    }

    @Bean
    public AuthTokenExceptionHandler authTokenExceptionHandler() {
        return new AuthTokenExceptionHandler();
    }


    /**
     * token 存储器
     **/
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }
}
