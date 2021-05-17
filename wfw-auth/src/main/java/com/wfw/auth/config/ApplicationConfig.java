package com.wfw.auth.config;

import com.wfw.auth.core.AuthPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:27
 * @description 配置一些给auth2+sercurity 使用的自定义bean
 **/
@Configuration
public class ApplicationConfig {

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
     * code 生成服务
     **/
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

}
