package com.wfw.auth.config;

import com.wfw.auth.core.AuthPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.concurrent.TimeUnit;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:27
 * @description 配置一些给auth2+sercurity 使用的自定义bean
 **/
@Configuration
public class ApplicationConfig {

    /**
    * xx
    *
    * @param
    * @return
    **/
    @Bean
    public ClientDetailsService clientDetailsService(PasswordEncoder passwordEncoder) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
        builder.withClient("admin")
                .secret("admin")
                .scopes("xx")
                //客户端认证所支持的授权类型 1:客户端凭证 2:账号密码 3:授权码 4:token刷新
                .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
                //用户角色
                .authorities("admin")
                //允许自动授权
                .autoApprove(false)
                //token 过期时间
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(12))
                //refresh_token 过期时间
                .refreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
        ;
        return builder.build();
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
     * code 生成服务
     **/
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

}
