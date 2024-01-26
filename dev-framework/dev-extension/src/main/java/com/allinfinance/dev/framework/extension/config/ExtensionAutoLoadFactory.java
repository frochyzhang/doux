package com.allinfinance.dev.framework.extension.config;

import cn.hutool.extra.spring.SpringUtil;
import com.allinfinance.dev.framework.extension.annotation.Extensible;
import com.allinfinance.dev.framework.extension.loader.ExtensionClass;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * @author huanghf
 * @date 2024/1/25 17:00
 */
@Configuration
public class ExtensionAutoLoadFactory {
    public ExtensionAutoLoadFactory() {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(Extensible.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(ClassUtils.getPackageName("com.allinfinance.qps.transaction.TransactionApplication"));
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
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, SpringUtil.getApplicationContext().getEnvironment()) {
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
}
