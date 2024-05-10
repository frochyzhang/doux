package cn.lezoo.doux.framework.extension.config;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.framework.extension.annotation.EnableExtensionAutoLoad;
import cn.lezoo.doux.framework.extension.annotation.Extensible;
import cn.lezoo.doux.framework.extension.loader.ExtensionClass;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/2/27 15:39
 */
public class ExtensionBeanRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(Extensible.class));
        getBasePackages(metadata).forEach(basePackage -> {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            candidateComponents.forEach(beanDefinition -> {
                ExtensionLoader<?> extensionLoader = null;
                try {
                    extensionLoader = ExtensionLoaderFactory.getExtensionLoader(Class.forName(beanDefinition.getBeanClassName()));
                } catch (ClassNotFoundException e) {
                    return;
                }
                for (Map.Entry<String, ? extends ExtensionClass<?>> entry : extensionLoader.getAllExtensions().entrySet()) {
                    Constructor<?> constructor = Arrays.stream(entry.getValue().getClazz().getConstructors())
                        .max(Comparator.comparingInt(o -> o.getParameterTypes().length))
                        .get();
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] objects = Arrays.stream(parameterTypes)
                        .map(SpringUtil::getBean)
                        .toArray();
                    extensionLoader.getExtension(entry.getKey(), parameterTypes, objects);
                }
            });
        });
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
            .getAnnotationAttributes(EnableExtensionAutoLoad.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        if (attributes == null) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
            return basePackages;
        }
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
