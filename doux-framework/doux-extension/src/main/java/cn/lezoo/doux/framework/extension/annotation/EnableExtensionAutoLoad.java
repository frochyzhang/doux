package cn.lezoo.doux.framework.extension.annotation;

import cn.lezoo.doux.framework.extension.config.ExtensionBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ExtensionBeanRegistrar.class)
public @interface EnableExtensionAutoLoad {
}
