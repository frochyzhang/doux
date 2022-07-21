package com.allinfinance.dev.hsp.constant;

/**
 * @author huanghf
 * @date 2022/6/21 13:48
 */
public enum AlgorithmEnum {
    DES("01", "DES/3DES"),
    SM4("07", "SM4");
    private final String code;
    private final String desc;

    AlgorithmEnum(String code, String desc) {
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
