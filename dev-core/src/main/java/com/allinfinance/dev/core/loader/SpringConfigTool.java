package com.allinfinance.dev.core.loader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张勇
 * @date 2020-11-28 01:23
 */
@Configuration
public class SpringConfigTool implements ApplicationContextAware {

    private static ApplicationContext context = null;
    private static SpringConfigTool stools = null;

    public synchronized static SpringConfigTool init() {
        if (stools == null) {
            stools = new SpringConfigTool();
        }
        return stools;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public synchronized static Object getBeanByBeanName(String beanName) {
        return context.getBean(beanName);
    }

    public synchronized static Object getBeanByClassName(String className) throws ClassNotFoundException {
        return context.getBean(Class.forName(className));
    }
}
