package com.allinfinance.dev.common.dictionary.desensitized;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:11
 */
public enum DesensitizedType {
    /**
     * x姓名
     */
    NAME,
    /**
     * 其他ID
     */
    OTHER_ID,
    /**
     * 证件号
     */
    ID_NO,
    /**
     * 手机号
     */
    MOBILE_NO,
    /**
     * 卡号
     */
    CARD_NO,
    /**
     * 密码
     */
    PASSWORD,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 邮箱
     */
    EMAIL,
    /**
     * 其他
     */
    OBJECT;

    DesensitizedType() {
    }
}
