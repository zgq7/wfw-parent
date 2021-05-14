package com.wfw.auth.core;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:32
 * @description 自定义密码加密解密工具
 **/
public class AuthPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return false;
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
