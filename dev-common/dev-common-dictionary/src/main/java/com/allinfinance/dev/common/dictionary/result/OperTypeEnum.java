package com.allinfinance.dev.common.dictionary.result;


public enum OperTypeEnum {

    QUERY("query", "查询"),
    DELETE("delete", "删除"),
    UPDATE("update", "更新"),
    INSERT("insert", "插入");

    private final String code;
    private final String message;

    OperTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
