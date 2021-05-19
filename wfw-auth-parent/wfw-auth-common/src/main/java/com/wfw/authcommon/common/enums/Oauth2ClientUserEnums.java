package com.wfw.authcommon.common.enums;

import lombok.Getter;

/**
 * @author liaonanzhou
 * @date 2021/5/19 10:14
 * @description
 **/
@Getter
public enum Oauth2ClientUserEnums {

    ADMIN("admin", "admin");

    private String clientId;

    private String clientSecret;

    Oauth2ClientUserEnums(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
