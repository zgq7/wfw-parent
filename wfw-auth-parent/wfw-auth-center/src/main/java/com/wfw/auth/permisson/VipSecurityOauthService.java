package com.wfw.auth.permisson;

import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liaonanzhou
 * @date 2021/6/25 15:58
 * @description
 **/
@Component
public class VipSecurityOauthService {

    public Set<PermRoleEntity> loadPerms() {
        Set<PermRoleEntity> permRoleEntitySet = new HashSet<>();
        permRoleEntitySet.add(new PermRoleEntity().setAccessUri("/demo/admin").setConfigAttributeList(SecurityConfig.createList("admin")));
        permRoleEntitySet.add(new PermRoleEntity().setAccessUri("/demo/sp-admin").setConfigAttributeList(SecurityConfig.createList("sp_admin")));

        return permRoleEntitySet;
    }

}
