package cn.lezoo.doux.feign;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

    Class<?> encoderClass() default Void.class;

    Class<?> decoderClass() default Void.class;
}
