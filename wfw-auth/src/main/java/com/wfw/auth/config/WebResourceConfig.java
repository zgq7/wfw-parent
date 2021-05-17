package com.wfw.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author liaonanzhou
 * @date 2021/5/17 14:24
 * @description
 **/
@Configuration
@EnableResourceServer
public class WebResourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 资源链路
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                // 登录放通
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/token","/oauth/token2")
                .permitAll()
                // 其他请求都需认证
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                // 跨域
                .and()
                .cors()
                // 关闭跨站请求防护
                .and()
                .csrf()
                .disable();

    }
}
