package com.allinfinance.dev.batch.task;

/**
 * @author 张勇
 * @description
 * @date 2020/12/10 15:10
 */
public class TestStrDto {
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "TestStrDto{" +
                "str='" + str + '\'' +
                '}';
    }
}
