package com.wfw.authcommon.core;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author liaonanzhou
 * @date 2021/5/14 15:09
 * @description 密码编码器
 **/
public class AuthPasswordEncryptor implements PasswordEncryptor {

    private final StandardStringDigester digester;

    public AuthPasswordEncryptor(String algorithm) {
        this.digester = new StandardStringDigester();
        this.digester.setAlgorithm(algorithm);
        // hash 次数
        this.digester.setIterations(1000);
        // 加盐字节大小
        this.digester.setSaltSizeBytes(8);
    }

    public void setStringOutputEncryptionTypeEnum(final String stringOutputType) {
        this.digester.setStringOutputType(stringOutputType);
    }

    @Override
    public String encryptPassword(final String password) {
        return this.digester.digest(password);
    }

    @Override
    public boolean checkPassword(final String plainPassword,
                                 final String encryptedPassword) {
        return this.digester.matches(plainPassword, encryptedPassword);
    }

}
