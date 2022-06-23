package com.allinfinance.dev.hsp.constant;

/**
 * @author huanghf
 * @date 2022/6/21 13:54
 */
public enum HashFlagEnum {
    No("01", "不做HASH，此时数据长度必须是32字节"),
    YES("02", "用SM3在内部做HASH");
    private final String code;
    private final String desc;

    HashFlagEnum(String code, String desc) {
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
