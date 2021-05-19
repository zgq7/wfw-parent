package com.wfw.auth.config;

import com.wfw.authcommon.core.AuthPasswordEncoder;
import com.wfw.authcommon.handle.AuthClientExceptionHandler;
import com.wfw.authcommon.handle.AuthTokenExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:27
 * @description 配置一些给auth2+sercurity 使用的自定义bean
 **/
@Configuration
public class ApplicationConfig {

    @Bean
    public AuthClientExceptionHandler authClientExceptionHandler() {
        return new AuthClientExceptionHandler();
    }

    @Bean
    public AuthTokenExceptionHandler authTokenExceptionHandler() {
        return new AuthTokenExceptionHandler();
    }

    /**
     * 密码解析器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AuthPasswordEncoder();
    }

    /**
     * token 存储器
     **/
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * /oauth/token_key 接口404的问题
     **/
    @Bean
    public TokenKeyEndpoint tokenKeyEndpoint() {
        // 不尽兴自定义的TokenStoreService,因此使用默认的jwtConverter
        return new TokenKeyEndpoint(new JwtAccessTokenConverter());
    }

    /**
     * code 生成服务
     **/
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

}
