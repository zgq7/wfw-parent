package com.wfw.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author liaonanzhou
 * @date 2021/12/28 18:08
 * @description
 **/
public class VipTokenService implements AuthorizationServerTokenServices {

    private final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    public VipTokenService(TokenStore tokenStore,
                           ClientDetailsService clientDetailsService,
                           JwtAccessTokenConverter jwtAccessTokenConverter,
                           AuthenticationManager authenticationManager) {
        this.defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        this.defaultTokenServices.setAuthenticationManager(authenticationManager);
        this.defaultTokenServices.setTokenStore(tokenStore);
        this.defaultTokenServices.setClientDetailsService(clientDetailsService);
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        return this.defaultTokenServices.createAccessToken(authentication);
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshToken, TokenRequest tokenRequest) throws AuthenticationException {
        return this.defaultTokenServices.refreshAccessToken(refreshToken, tokenRequest);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return this.defaultTokenServices.getAccessToken(authentication);
    }

    public DefaultTokenServices getDefaultTokenServices() {
        return defaultTokenServices;
    }
}
