package com.allinfinance.dev.hsp.constant;

/**
 * @author huanghf
 * @date 2022/6/21 9:38
 */
public enum InstructionEnum {
    D306("D306", "用SM2私钥做签名"),
    D307("D307", "用SM2公钥做验签"),
    D30A("D30A", "摘要");
    private final String code;
    private final String desc;

    InstructionEnum(String code, String desc) {
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
