package com.wfw.auth.core;

import com.wfw.auth.helper.PasswordHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author liaonanzhou
 * @date 2021/5/14 11:32
 * @description 自定义密码加密解密工具
 **/
public class AuthPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return PasswordHelper.encryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordHelper.validatePassword(rawPassword.toString(), encodedPassword);
    }

}
