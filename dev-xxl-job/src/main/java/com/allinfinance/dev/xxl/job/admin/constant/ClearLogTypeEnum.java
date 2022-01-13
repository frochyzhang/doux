package com.allinfinance.dev.xxl.job.admin.constant;

/**
 * @author huanghf
 * @date 2022/1/4 11:25
 */
public enum ClearLogTypeEnum {
    CLEAR_LOG_ONE_MONTH_AGO(1, "清理一个月之前日志数据"),
    CLEAR_LOG_THREE_MONTHS_AGO(2, "清理三个月之前日志数据"),
    CLEAR_LOG_SIX_MONTHS_AGO(3, "清理六个月之前日志数据"),
    CLEAR_LOG_ONE_YEAR_AGO(4, "清理一年之前日志数据"),
    CLEAR_LOG_ONE_THOUSAND_LEFT(5, "清理一千条以前日志数据"),
    CLEAR_LOG_TEN_THOUSAND_LEFT(6, "清理一万条以前日志数据"),
    CLEAR_LOG_THIRTY_THOUSAND_LEFT(7, "清理三万条以前日志数据"),
    CLEAR_LOG_ONE_HUNDRED_THOUSAND_LEFT(8, "清理十万条以前日志数据"),
    CLEAR_LOG_ALL(9, "清理所有日志数据");
    private final int type;
    private final String desc;

    ClearLogTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
