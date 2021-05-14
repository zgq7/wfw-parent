package com.wfw.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

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
    private final ClientDetailsService clientDetailsService;
    private final TokenStore tokenStore;
    private final AuthorizationCodeServices authorizationCodeServices;

    public WebAuthorizationConfig(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  PasswordEncoder passwordEncoder,
                                  ClientDetailsService clientDetailsService,
                                  TokenStore tokenStore,
                                  AuthorizationCodeServices authorizationCodeServices) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.clientDetailsService = clientDetailsService;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder)                //设置密码编辑器
                .checkTokenAccess("isAuthenticated()");          //开启/oauth/check_token验证端口认证权限访问
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置授权服务器端点的属性
        endpoints.authenticationManager(authenticationManager)    //认证管理器
                .tokenStore(tokenStore)
                .authorizationCodeServices(authorizationCodeServices)
                .userDetailsService(userDetailsService);
    }


}
