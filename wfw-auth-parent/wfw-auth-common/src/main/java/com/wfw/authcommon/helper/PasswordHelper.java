package com.wfw.authcommon.helper;

import com.wfw.authcommon.common.enums.EncryptionTypeEnum;
import com.wfw.authcommon.core.AuthPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaonanzhou
 * @date 2021/6/7 16:26
 * @description 密码匹配器
 **/
public class PasswordHelper {

    private static final Map<EncryptionTypeEnum, AuthPasswordEncryptor> passwordEncryptors = new HashMap<>();

    private static PasswordEncryptor getPasswordEncryptor(final EncryptionTypeEnum typeEnum) {
        if (passwordEncryptors.get(typeEnum) == null) {
            synchronized (passwordEncryptors) {
                if (passwordEncryptors.get(typeEnum) == null) {
                    AuthPasswordEncryptor passwordEncryptor = new AuthPasswordEncryptor(typeEnum.getAlgorithm());
                    passwordEncryptor.setStringOutputEncryptionTypeEnum(typeEnum.getOutputType());
                    passwordEncryptors.put(typeEnum, passwordEncryptor);

                }
            }
        }
        return passwordEncryptors.get(typeEnum);
    }

    /**
     * @param password           密码
     * @param encryptionTypeEnum 加密类型
     * @return 加密后的密码
     **/
    public static String encryptPassword(final String password,
                                         final EncryptionTypeEnum encryptionTypeEnum) {
        return getPasswordEncryptor(encryptionTypeEnum).encryptPassword(password);
    }

    public static String encryptPassword(final String password) {
        return getPasswordEncryptor(EncryptionTypeEnum.SHA256_BASE64).encryptPassword(password);
    }

    /**
     * @param password          真实密码
     * @param encryptedPassword 加密后的密码
     * @return 密码是否正确
     **/
    public static boolean validatePassword(final String password,
                                           final String encryptedPassword,
                                           final EncryptionTypeEnum EncryptionTypeEnum) {
        return getPasswordEncryptor(EncryptionTypeEnum).checkPassword(password, encryptedPassword);
    }

    public static boolean validatePassword(final String password,
                                           final String encryptedPassword) {
        return getPasswordEncryptor(EncryptionTypeEnum.SHA256_BASE64).checkPassword(password, encryptedPassword);
    }


}
