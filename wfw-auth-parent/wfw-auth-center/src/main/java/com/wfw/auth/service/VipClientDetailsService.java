package com.wfw.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wfw.authcommon.common.enums.Oauth2ClientUserEnums;
import com.wfw.authcommon.helper.PasswordHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.wfw.authcommon.common.constants.GrantTypeConstants.*;

/**
 * @author liaonanzhou
 * @date 2021/12/27 18:05
 * @description
 **/
@Primary
@Service
public class VipClientDetailsService implements ClientDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(VipClientDetailsService.class);
    private final static String OAUTH_CLIENT = "oauth:client";

    private final RedisTemplate<String, String> redisTemplate;

    public VipClientDetailsService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails cd = null;
        try {
            String v = (String) redisTemplate.opsForHash().get(OAUTH_CLIENT, clientId);
            if (StringUtils.isNoneEmpty(v)) {
                cd = JSON.parseObject(v, ClientDetails.class);
            } else {
                cd = buildDefault();
            }
        } catch (Exception e) {
            logger.error("error:", e);
        }
        logger.info("cd:{}", JSONObject.toJSONString(cd));
        return cd;
    }

    /**
     * {@link }
     **/
    private ClientDetails buildDefault() {
        String secret = PasswordHelper.encryptPassword(Oauth2ClientUserEnums.ADMIN.getClientSecret());

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(Oauth2ClientUserEnums.ADMIN.getClientId());
        clientDetails.setAuthorizedGrantTypes(Arrays.asList(CLIENT_CREDENTIALS, PASSWORD, REFRESH_TOKEN, AUTHORIZATION_CODE, IMPLICIT));
        clientDetails.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(12));
        clientDetails.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        clientDetails.setRegisteredRedirectUri(new HashSet<>(Collections.singletonList("http://www.baidu.com")));
        clientDetails.setClientSecret(secret);
        clientDetails.setScope(Arrays.asList("all", "test"));
        clientDetails.setAuthorities(AuthorityUtils.createAuthorityList("admin"));
        clientDetails.setResourceIds(Collections.singleton("admin"));
        clientDetails.setAutoApproveScopes(Collections.singletonList("default-user"));
        return clientDetails;
    }


}
