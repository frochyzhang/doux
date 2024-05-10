package cn.lezoo.doux.common.dictionary.desensitized;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DesensitizedMark {
}
