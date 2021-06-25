package com.wfw.auth.permisson;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liaonanzhou
 * @date 2021/6/21 17:44
 * @description 动态权限调用服务
 **/
public class VipSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private Set<PermRoleEntity> permRoleEntitySet;

    private final FilterInvocationSecurityMetadataSource superMetadataSource;
    private final VipSecurityOauthService vipSecurityOauthService;

    public VipSecurityMetadataSource(FilterInvocationSecurityMetadataSource superMetadataSource, VipSecurityOauthService vipSecurityOauthService) {
        this.superMetadataSource = superMetadataSource;
        this.vipSecurityOauthService = vipSecurityOauthService;
    }

    private void loadPerms() {
        permRoleEntitySet = vipSecurityOauthService.loadPerms();
    }

    /**
     * 返回能访问该请求的所有角色集合
     **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        loadPerms();

        FilterInvocation fi = (FilterInvocation) object;
        String access_uri = fi.getRequestUrl();
        String path = access_uri.split("\\?")[0];

        Map<String, List<ConfigAttribute>> configAttributeMap = permRoleEntitySet.stream()
                .collect(Collectors.toMap(
                        PermRoleEntity::getAccessUri,
                        PermRoleEntity::getConfigAttributeList)
                );

        if (configAttributeMap.get(path) != null) {
            return configAttributeMap.get(path);
        } else {
            return superMetadataSource.getAttributes(object);
        }

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        loadPerms();

        Set<ConfigAttribute> attributeSet = new HashSet<>();
        permRoleEntitySet.stream().map(PermRoleEntity::getConfigAttributeList).forEach(attributeSet::addAll);
        return attributeSet;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}