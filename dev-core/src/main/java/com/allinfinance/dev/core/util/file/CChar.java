package com.allinfinance.dev.core.util.file;

import java.lang.annotation.*;
import java.text.MessageFormat;

/**
 * CChar
 *
 * @author hongmr
 * @date 2017/8/1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CChar {
    int value() default 1;
    /**
     * 在指定到整数数字字段的情况下是否要填充0
     * @return
     */
    boolean zeroPadding() default true;

    /**
     * 在指定到数字字段的情况下的格式化字段串，使用 {@link MessageFormat}的格式。比{@link #zeroPadding()}优先级高。
     * @return
     */
    String formatPattern() default "";

    /**
     * 向左填充空格。默认情况下为 false，即尾随追加空格。
     * @return
     */
    boolean leftPadding() default false;

    /**
     * 对于字符串型的字段，在解析时自动执行trim()
     * @return
     */
    boolean autoTrim() default false;
}
