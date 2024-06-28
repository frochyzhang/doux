package cn.lezoo.doux.framework.extension.config;

import cn.lezoo.doux.framework.extension.annotation.Extensible;
import cn.lezoo.doux.framework.extension.loader.ExtensionClass;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/2/27 15:39
 */
@Slf4j
public class ExtensionBeanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
        getExtensibleClasses().stream()
                .filter(extensibleClass -> extensibleClass.isAnnotationPresent(Extensible.class))
                .forEach(extensibleClass -> {
                    Extensible extensible = extensibleClass.getDeclaredAnnotation(Extensible.class);
                    ExtensionLoader<?> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(extensibleClass);
                    for (Map.Entry<String, ? extends ExtensionClass<?>> entry : extensionLoader.getAllExtensions().entrySet()) {
                        if (entry.getValue().isTrusteeship()) {
                            Class<?> extensionClass = entry.getValue().getClazz();
                            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(extensionClass);
                            if (!extensible.singleton()) {
                                beanDefinitionBuilder.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                            }
                            registry.registerBeanDefinition(extensionClass.getName(), beanDefinitionBuilder.getBeanDefinition());
                        }
                    }
                });
    }

    private Set<Class<?>> getExtensibleClasses() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources("classpath*:META-INF/services/lezoo/*");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<Class<?>> extensibleClasses = new HashSet<>();
        for (Resource resource : resources) {
            try {
                extensibleClasses.add(Class.forName(resource.getFilename()));
            } catch (ClassNotFoundException e) {
                log.error("Error when load extension from file [{}], the file name must be the full extensible class name", resource.getFilename());
                throw new RuntimeException(e);
            }
        }
        return extensibleClasses;
    }
}
