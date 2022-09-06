package com.allinfinance.dev.common.dictionary.validate;

import java.lang.annotation.*;

/**
 * @author 张勇
 * @description
 * @date 2020/12/7 20:59
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Check {
    /**
     * 字段类型
     */
    Class<?> type() default String.class;

    /**
     * 字段长度
     */
    int length() default 0;

    /**
     * 字段长度最大值
     */
    int maxLength() default 0;

    /**
     * 字段长度最小值
     */
    int minLength() default 0;

    /**
     * 字段校验正则
     */
    String regex() default "";
}
