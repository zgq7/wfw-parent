package com.wfw.auth.config;

import com.wfw.auth.permisson.VipAccessDecisionManager;
import com.wfw.auth.permisson.VipSecurityMetadataSource;
import com.wfw.auth.permisson.VipSecurityOauthService;
import com.wfw.authcommon.handle.AuthClientExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;

/**
 * @author liaonanzhou
 * @date 2021/5/17 14:24
 * @description
 **/
@Configuration
@EnableResourceServer
public class WebResourceConfig extends ResourceServerConfigurerAdapter {

    private final AuthClientExceptionHandler authClientExceptionHandler;

    public WebResourceConfig(AuthClientExceptionHandler authClientExceptionHandler) {
        this.authClientExceptionHandler = authClientExceptionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("admin").stateless(true).authenticationEntryPoint(authClientExceptionHandler);
    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        // 资源链路
//        http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and().formLogin().permitAll()
//                // 登录放通
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**", "/favicon.ico")
//                .permitAll()
//                // 其他请求都需认证
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                // 跨域
//                .and()
//                .cors()
//                // 关闭跨站请求防护
//                .and()
//                .csrf()
//                .disable();
//    }

    @Resource
    private VipAccessDecisionManager vipAccessDecisionManager;
    @Resource
    private VipSecurityOauthService vipSecurityOauthService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 资源链路
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().formLogin().permitAll()
                // 登录放通
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/favicon.ico")
                .permitAll()
                // 其他请求都需认证
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                // 动态权限
                .and()
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fi) {
                        fi.setSecurityMetadataSource(new VipSecurityMetadataSource(fi.getSecurityMetadataSource(),vipSecurityOauthService));
                        fi.setAccessDecisionManager(vipAccessDecisionManager);
                        return fi;
                    }
                })
                // 跨域
                .and()
                .cors()
                // 关闭跨站请求防护
                .and()
                .csrf()
                .disable();
    }

}
