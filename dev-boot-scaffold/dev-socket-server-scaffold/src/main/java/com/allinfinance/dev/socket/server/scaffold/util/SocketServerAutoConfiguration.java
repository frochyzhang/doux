package com.allinfinance.dev.socket.server.scaffold.util;

import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.socket.server.scaffold.configure.SocketScaffoldConfigure;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-22 14:44
 */
@Configuration
@EnableConfigurationProperties(SocketScaffoldConfigure.class)
@ComponentScan("com.allinfinance.dev.socket.server.scaffold")
public class SocketServerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SocketServerAutoConfiguration.class);
    @Autowired
    private SocketScaffoldConfigure socketScaffoldConfigure;

    @Bean(name = "socketServerList")
    public List<Properties> getServerPropertiesList() {
        return socketScaffoldConfigure.getServerMetadataMap()
                .stream().map(serverMetadataConfigure -> {
                    logger.info("服务端配置信息：{}", serverMetadataConfigure);
                    Properties serverProperties = new Properties();
                    PropertiesParseUtils.fromBean(serverProperties, serverMetadataConfigure);
                    return serverProperties;
                }).collect(Collectors.toList());
    }
}
