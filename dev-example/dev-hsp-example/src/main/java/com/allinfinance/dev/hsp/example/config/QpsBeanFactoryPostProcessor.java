package com.allinfinance.dev.hsp.example.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/7/4 14:34
 */
@Component
public class QpsBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    public ConfigurableListableBeanFactory getConfigurableListableBeanFactory() {
        return configurableListableBeanFactory;
    }
}
