package com.allinfinance.dev.rpc.scaffold.service;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.factory.CacheableEventPublishingNacosServiceFactory;
import com.allinfinance.dev.rpc.scaffold.api.NacosApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/3/24
 **/
@ConditionalOnProperty(value = "com.allinfinance.rpc.nacos.config.enabled", havingValue = "true")
@ConditionalOnClass(CacheableEventPublishingNacosServiceFactory.class)
@Service("nacosApplicationService")
public class NacosApplicationServiceImpl implements NacosApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(NacosApplicationServiceImpl.class);

    private final CacheableEventPublishingNacosServiceFactory factory = CacheableEventPublishingNacosServiceFactory.getSingleton();
    @Value("${nacos.config.namespace}")
    private String namespace;
    @Value("${nacos.config.server-addr}")
    private String serverAddr;
    @Value("${nacos.config.group}")
    private String group;
    @Value("${nacos.config.config-long-poll-timeout}")
    private String timeout;

    /**
     * 读取配置
     *
     * @param configName  配置文件的data-id
     * @param targetClass 需要读取的配置文件对象类型
     * @param <T>         需要读取的配置对象
     * @param type        远程配置类型，如ConfigType.YAML，默认为json格式
     * @return 读取出的配置对象
     * @throws NacosException          NacosException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Override
    public <T> T fetchNacosConfig(String configName, Class<T> targetClass, ConfigType type) throws NacosException, JsonProcessingException {
        logger.info("读取配置请求开始:{}", configName);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        logger.info("nacos远程配置信息:{}", properties);
        ConfigService configService = factory.createConfigService(properties);
        String content = configService.getConfig(configName, group, Integer.parseInt(timeout));
        ObjectMapper mapper;
        if (ConfigType.YAML.equals(type)) {
            mapper = new ObjectMapper(new YAMLFactory());
        } else {
            mapper = new ObjectMapper();
        }
        return mapper.readValue(content, targetClass);
    }

    /**
     * 发布配置
     *
     * @param configName 配置文件的data-id
     * @param content    需要发布的配置字符串
     * @param type       需要发布配置文件类型，如ConfigType.YAML
     * @return 发布的配置内容
     * @throws NacosException NacosException
     */
    @Override
    public Boolean publishConfig(String configName, String content, ConfigType type) throws NacosException {
        logger.info("发布配置请求开始：{}", content);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = factory.createConfigService(properties);
        logger.info("待发布的配置信息:{}", content);
        //写入配置文件
        configService.publishConfig(configName, group, content, type.getType());
        logger.info("发布配置完成");
        return Boolean.TRUE;
    }

}

