package com.allinfinance.dev.dal.mybatis;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 张勇
 * @description 详情请查看{@link org.mybatis.spring.annotation.MapperScannerRegistrar}
 * @date 2020/12/6 01:31
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(MapperScannerRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes annoAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScanner.class.getName()));
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        scanner.setSqlSessionTemplateBeanName(annoAttrs.getString("sqlSessionTemplateRef"));
        scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(parsePlaceHolder(pkg));
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(parsePlaceHolder(pkg));
            }
        }
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));

    }

    /**
     * 新增占位符解析
     *
     * @param pro 占位符名
     * @return 占位符值
     */
    private String parsePlaceHolder(String pro) {
        if (pro != null && pro.contains(PropertySourcesPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX)) {
            String propertyName = pro.substring(2, pro.length() - 1);
            String value = env.getProperty(propertyName);

            if (logger.isDebugEnabled()) {
                logger.debug("find placeholder value {} for key {}! ", value, pro);
            }

            if (null == value) {
                try {
                    logger.debug("未获取到对应环境参数，重新获取参数信息:{}", pro);
                    Configurations configurations = new Configurations();
                    String fileName = this.getClass().getResource(CommonConstants.DB_PROPERTY_FILE).getPath();
                    PropertiesConfiguration properties = configurations.properties(fileName);
                    value = properties.getString(propertyName);
                    if (null == value) {
                        throw new IllegalArgumentException("缺少配置字段:" + propertyName);
                    }
                } catch (ConfigurationException e) {
                    logger.error("配置文件信息读取失败!", e);
                }
            }
            return value;
        }
        return pro;
    }

    /**
     * load properties
     *
     * @param properties
     * @return
     */
    private Properties loadProperties(String[] properties) {
        Properties container = new Properties();
        if (null != properties) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            for (String pro : properties) {
                try {
                    container.load(resolver.getResource(pro).getInputStream());
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

        }
        return container;
    }

    @Override
    public void setResourceLoader(ResourceLoader loader) {
        this.resourceLoader = loader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

}