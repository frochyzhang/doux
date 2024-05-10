package cn.lezoo.doux.connection.pool.scaffold;

import cn.lezoo.doux.common.util.convert.PropertiesParseUtils;
import cn.lezoo.doux.connection.pool.scaffold.configure.ConnectionPoolConfigure;
import cn.lezoo.doux.connection.pool.scaffold.configure.ScaffoldConfigure;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.driver.ServerMetadataFactory;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@EnableConfigurationProperties({ConnectionPoolConfigure.class, ScaffoldConfigure.class})
@Configuration
@ComponentScan("cn.lezoo.doux.connection.pool.scaffold")
public class ConnectionPoolAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolAutoConfiguration.class);

    @Autowired
    private ScaffoldConfigure scaffoldConfigure;
    @Autowired
    private ConnectionPoolConfigure connectionPoolConfigure;

    @Bean(name = "serverMetadataList")
    public List<ServerMetadata> getServerMetadataList() {
        return scaffoldConfigure.getServerMetadataMap().values()
                .stream().map(metadataConfigure -> {
                    logger.info("服务端配置信息：{}", metadataConfigure);

                    Properties properties = new Properties();
                    PropertiesParseUtils.fromBean(properties, metadataConfigure);
                    PropertiesParseUtils.fromBean(properties, connectionPoolConfigure);
                    ExtensionLoader<ServerMetadataFactory> serverMetadataExtensionLoader = ExtensionLoaderFactory.getExtensionLoader(ServerMetadataFactory.class);
                    ServerMetadataFactory factory = serverMetadataExtensionLoader.getExtension(connectionPoolConfigure.getConnectionPoolType());

                    factory.setProperties(properties);
                    return factory.getMetadata();
                }).collect(Collectors.toList());
    }
}
