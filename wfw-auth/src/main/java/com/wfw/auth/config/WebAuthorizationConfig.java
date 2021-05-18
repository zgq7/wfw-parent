package com.wfw.auth.config;

import com.wfw.auth.handle.AuthClientExceptionHandler;
import com.wfw.auth.handle.AuthTokenExceptionHandler;
import com.wfw.auth.helper.PasswordHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:35
 * @description auth2授权配置
 **/
@Configuration
@EnableAuthorizationServer
public class WebAuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final AuthorizationCodeServices authorizationCodeServices;
    private final AuthClientExceptionHandler authClientExceptionHandler;
    private final AuthTokenExceptionHandler authTokenExceptionHandler;

    public WebAuthorizationConfig(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  PasswordEncoder passwordEncoder,
                                  TokenStore tokenStore,
                                  AuthorizationCodeServices authorizationCodeServices,
                                  AuthClientExceptionHandler authClientExceptionHandler,
                                  AuthTokenExceptionHandler authTokenExceptionHandler) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authClientExceptionHandler = authClientExceptionHandler;
        this.authTokenExceptionHandler = authTokenExceptionHandler;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String secret = PasswordHelper.encryptPassword("admin");

        clients.inMemory()
                .withClient("admin")
                .secret(secret)
                .scopes("all")
                .resourceIds("admin")
                //客户端认证所支持的授权类型 1:客户端凭证 2:账号密码 3:授权码 4:token刷新 5:简易模式
                .authorizedGrantTypes("client_credentials", "password", "refresh_token", "authorization_code")
                //用户角色
                .authorities("admin")
                //允许自动授权
                .autoApprove(false)
                //token 过期时间
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(12))
                //refresh_token 过期时间
                .refreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder)                //设置密码编辑器
                .tokenKeyAccess("permitAll()")                   //
                .checkTokenAccess("isAuthenticated()")           //开启/oauth/check_token验证端口认证权限访问
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 地址映射 默认地址：自定义地址
        //endpoints.pathMapping("/oauth/token","/auth/login");
        //endpoints.pathMapping("/auth/login", "/oauth/token");
        // 配置授权服务器端点的属性
        endpoints.authenticationManager(authenticationManager)    //认证管理器
                .tokenStore(tokenStore)
                .authorizationCodeServices(authorizationCodeServices)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(authTokenExceptionHandler);
    }


}
