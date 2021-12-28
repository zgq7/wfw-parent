package com.wfw.auth.config;

import com.wfw.authcommon.common.enums.Oauth2ClientUserEnums;
import com.wfw.authcommon.handle.AuthTokenExceptionHandler;
import com.wfw.authcommon.helper.PasswordHelper;
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
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static com.wfw.authcommon.common.constants.GrantTypeConstants.*;

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
    private final AuthTokenExceptionHandler authTokenExceptionHandler;
    private final AuthorizationServerTokenServices tokenService;
    private final ClientDetailsService clientDetailsService;
    private final DataSource dataSource;

    public WebAuthorizationConfig(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  PasswordEncoder passwordEncoder,
                                  TokenStore tokenStore,
                                  AuthorizationCodeServices authorizationCodeServices,
                                  AuthTokenExceptionHandler authTokenExceptionHandler,
                                  AuthorizationServerTokenServices tokenService,
                                  ClientDetailsService clientDetailsService,
                                  DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authTokenExceptionHandler = authTokenExceptionHandler;
        this.tokenService = tokenService;
        this.clientDetailsService = clientDetailsService;
        this.dataSource = dataSource;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
         * ----------------【配置client加载源】---------------
         */

        // 自定义加载
        clients.withClientDetails(clientDetailsService);
        // 内存加载
        //clients.jdbc(dataSource);
        // 内存加载
        //clientWithInMemory(clients);
    }

    /**
     * 内存存放客户端配置
     **/
    private void clientWithInMemory(ClientDetailsServiceConfigurer clients) throws Exception {
        String secret = PasswordHelper.encryptPassword(Oauth2ClientUserEnums.ADMIN.getClientSecret());
        clients.inMemory()
                .withClient(Oauth2ClientUserEnums.ADMIN.getClientId())
                .secret(secret)
                .scopes("all", "test")
                .resourceIds("admin")
                // autoApprove 可跳过授权页直接返回code/token
                .autoApprove(false)
                // autoApprove = true，approveScope 用这个
                .autoApprove("all")
                // autoApprove = false，approveScope 用这个
                .scopes("default-user")
                .redirectUris("http://www.baidu.com")
                //客户端认证所支持的授权类型 1:客户端凭证 2:账号密码 3:授权码 4:token刷新 5:简易模式
                .authorizedGrantTypes(CLIENT_CREDENTIALS, PASSWORD, REFRESH_TOKEN, AUTHORIZATION_CODE, IMPLICIT)
                //用户角色
                .authorities("admin")
                //允许自动授权
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
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")                   //开启 /oauth/token_key 的访问权限控制
                .checkTokenAccess("permitAll()")                 //开启 /oauth/check_token 验证端口认证权限访问
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置授权服务器端点的属性
        endpoints.authenticationManager(authenticationManager)    //认证管理器
                .tokenStore(tokenStore)
                .tokenServices(tokenService)
                .authorizationCodeServices(authorizationCodeServices)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(authTokenExceptionHandler);


        //overrideUrl(endpoints);
    }

    /**
     * 更改oauth2默认url
     **/
    private void overrideUrl(AuthorizationServerEndpointsConfigurer endpoints) {
        // 地址映射 默认地址：自定义地址
        endpoints
                // token获取
                .pathMapping("/oauth/token", "/auth/login")
                // 授权认证
                .pathMapping("/oauth/authorize", "/auth/authorize")
                // 授权确认
                .pathMapping("/oauth/confirm_access", "/auth/confirm_access")
        ;

    }


}
