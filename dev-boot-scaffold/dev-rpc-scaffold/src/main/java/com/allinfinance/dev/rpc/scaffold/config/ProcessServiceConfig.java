package com.allinfinance.dev.rpc.scaffold.config;

import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huanghf
 * @date 2022/7/7 17:00
 */
@ConditionalOnProperty(value = RpcConfigurationProperties.Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class ProcessServiceConfig {
    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceConfig.class);

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Bean
    public ProcessService processServiceBean() {
        ExtensionLoader<ProcessService> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(ProcessService.class);
        ProcessService extension = extensionLoader.getExtension(rpcConfigurationProperties.getBootstrap().getProcessServiceExtension());
        logger.info("获取ProcessService扩展实现: {}", extension);
        return extension;
    }
}
