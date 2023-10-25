package com.allinfinance.dev.feign;

import java.lang.annotation.Target;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DevFeign {
    String name();

    String url() default "";

    String msgEncode() default "UTF-8";

    int timeout() default 3000;

    int msgLengthSize() default 6;
}
