package com.allinfinance.dev.core.util.file;

import java.lang.annotation.*;

/**
 * CBinaryInt
 *
 * @author hongmr
 * @date 2017/8/1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CBinaryInt {
    boolean bigEndian() default true;

    /**
     * 字节数,最大值为8
     *
     * @return
     */
    int length() default 4;
}
