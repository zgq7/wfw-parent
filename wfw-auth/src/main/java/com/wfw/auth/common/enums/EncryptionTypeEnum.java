package com.wfw.auth.common.enums;

/**
 * @author liaonanzhou
 * @date 2021/5/14 15:03
 * @description
 **/
public enum EncryptionTypeEnum {
    SHA256_BASE64("SHA-256", "base64"),
    SHA256_HEX("SHA-256", "hexadecimal"),
    MD5_BASE64("MD5", "base64"),
    MD5_HEX("MD5", "hexadecimal");

    private String algorithm;
    private String outputType;

    private EncryptionTypeEnum(String algorithm, String value) {
        this.algorithm = algorithm;
        this.outputType = value;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getOutputType() {
        return outputType;
    }
}
