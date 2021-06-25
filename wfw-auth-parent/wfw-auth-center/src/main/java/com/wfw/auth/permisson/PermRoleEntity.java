package com.wfw.auth.permisson;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;

/**
 * @author liaonanzhou
 * @date 2021/6/25 16:28
 * @description
 **/
@Data
@Accessors(chain = true)
public class PermRoleEntity {

    /**
     * 访问的接口
     **/
    private String accessUri;

    /**
     * 可访问该接口的角色集合
     **/
    private List<ConfigAttribute> configAttributeList;
}
