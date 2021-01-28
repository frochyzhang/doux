package com.allinfinance.dev.dubbo.config;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DynamicDubboService implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDubboService.class);
    private static ApplicationContext applicationContext;
    private static final String ZOOKEEPER_PROTOCOL = "zookeeper";

    private String version;
    private String zkAddress;

    public <T> T getDubboService(String group, Class<T> clazz) throws Exception {
        logger.info("获取动态dubbo服务, group = {}, interface = {}!", group, clazz);
        ReferenceBean<T> referenceBean = new ReferenceBean<>();
        referenceBean.setApplicationContext(applicationContext);
        referenceBean.setInterface(clazz);
        referenceBean.setCheck(false);
        referenceBean.setGroup(group);
        referenceBean.setVersion(version);
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkAddress);
        registryConfig.setProtocol(ZOOKEEPER_PROTOCOL);
        referenceBean.setRegistry(registryConfig);
        try {
            referenceBean.afterPropertiesSet();
            return referenceBean.get();
        } catch (Exception e) {
            logger.error("动态获取dubbo服务异常!", e);
            throw new Exception("动态获取dubbo服务异常", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DynamicDubboService.applicationContext = applicationContext;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }
}
