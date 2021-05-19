package com.wfw.auth.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author liaonanzhou
 * @date 2021/5/19 10:49
 * @description
 **/
@Getter
public class AuthToken {

    private AuthToken(String accessToken, String refreshToken, String tokenType, int expireIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expireIn = expireIn;
    }

    @JSONField(name = OAuth2AccessToken.ACCESS_TOKEN)
    private String accessToken;

    @JSONField(name = OAuth2AccessToken.REFRESH_TOKEN)
    private String refreshToken;

    @JSONField(name = OAuth2AccessToken.TOKEN_TYPE)
    private String tokenType;

    @JSONField(name = OAuth2AccessToken.EXPIRES_IN)
    private int expireIn;

    public static AuthToken build(OAuth2AccessToken oAuth2AccessToken) {
        return new AuthToken(oAuth2AccessToken.getValue(), oAuth2AccessToken.getRefreshToken().getValue(),
                oAuth2AccessToken.getTokenType(), oAuth2AccessToken.getExpiresIn());
    }

}
