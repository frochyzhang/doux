package com.allinfinance.dev.core.dto.hsp.constant;

/**
 * @author huanghf
 * @date 2022/6/21 14:36
 */
public enum HashAlgorithmEnum {
    MD5("00", "MD5"),
    SHA1("01", "SHA-1"),
    SHA224("02", "SHA-224"),
    SHA256("03", "SHA-256"),
    SHA384("04", "SHA-384"),
    SHA512("05", "SHA-512"),
    SM3("06", "SM3");
    private final String code;
    private final String desc;

    HashAlgorithmEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
