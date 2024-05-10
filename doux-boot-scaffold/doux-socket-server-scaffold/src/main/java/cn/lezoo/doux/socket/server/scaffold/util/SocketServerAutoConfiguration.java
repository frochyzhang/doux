package cn.lezoo.doux.socket.server.scaffold.util;

import cn.lezoo.doux.common.util.convert.PropertiesParseUtils;
import cn.lezoo.doux.socket.server.scaffold.configure.ServerMetadataConfigure;
import cn.lezoo.doux.socket.server.scaffold.configure.SocketScaffoldConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-22 14:44
 */
@Configuration
@ComponentScan("cn.lezoo.doux.socket.server.scaffold")
public class SocketServerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SocketServerAutoConfiguration.class);
    @Autowired
    private SocketScaffoldConfigure socketScaffoldConfigure;

    @ConditionalOnProperty(prefix = "cn.lezoo.socket.server.bootstrap", name = "enabled", havingValue = "true")
    @Bean(name = "socketServerList")
    public Map<String, List<Properties>> getServerPropertiesList() {
        return socketScaffoldConfigure.getServerMetadataList()
                .stream()
                .collect(Collectors.groupingBy(ServerMetadataConfigure::getBootstrap, HashMap::new, Collectors.mapping(serverMetadataConfigure -> {
                    logger.info("服务端配置信息：{}", serverMetadataConfigure);
                    Properties serverProperties = new Properties();
                    PropertiesParseUtils.fromBean(serverProperties, serverMetadataConfigure);
                    return serverProperties;
                }, Collectors.toList())));
    }
}
