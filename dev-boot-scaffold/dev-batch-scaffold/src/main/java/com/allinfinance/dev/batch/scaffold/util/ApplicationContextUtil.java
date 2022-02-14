package com.allinfinance.dev.batch.scaffold.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/13 9:17
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);
    private static ApplicationContext applicationContext;

    /**
     * 动态注入单例bean实例
     *
     * @param beanName        bean名称
     * @param singletonObject 单例bean实例
     * @return 注入实例
     */
    public static Object registerSingletonBean(String beanName, Object singletonObject) {

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();

        if (defaultListableBeanFactory.containsBeanDefinition(beanName)) {
            logger.info("bean: " + beanName + "已存在，无需重复注册！");
        } else {
            //动态注册bean.
            defaultListableBeanFactory.registerSingleton(beanName, singletonObject);
        }

        //获取动态注册的bean.
        return configurableApplicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
