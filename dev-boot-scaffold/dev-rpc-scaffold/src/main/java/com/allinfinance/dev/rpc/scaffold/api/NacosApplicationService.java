package com.allinfinance.dev.rpc.scaffold.api;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @Description: nacos远程文件读取
 * @Author: qipeng
 * @Date: 2022/3/24
 **/
public interface NacosApplicationService {

    /**
     * 读取配置
     *
     * @param configName  配置文件的data-id
     * @param targetClass 需要读取的配置文件对象类型
     * @param <T>         需要读取的配置对象
     * @param type        远程配置类型，如ConfigType.YAML
     * @return 读取出的配置对象
     * @throws NacosException          NacosException
     * @throws JsonProcessingException JsonProcessingException
     */
    <T> T fetchNacosConfig(String configName, Class<T> targetClass, ConfigType type) throws NacosException, JsonProcessingException;

    /**
     * 发布配置
     *
     * @param configName 配置文件的data-id
     * @param content    需要发布的配置字符串
     * @param type       需要发布配置文件类型，如ConfigType.YAML
     * @return 发布的配置内容
     * @throws NacosException NacosException
     */
    Boolean publishConfig(String configName, String content, ConfigType type) throws NacosException;
}
