package cn.lezoo.doux.gateway.scaffold.bootstrap;

import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import cn.lezoo.doux.gateway.scaffold.api.ProcessService;
import cn.lezoo.doux.gateway.scaffold.config.Bootstrap;
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
@ConditionalOnProperty(value = Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class ProcessServiceConfig {
    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceConfig.class);

    @Autowired
    private Bootstrap bootstrap;

    @Bean
    public ProcessService processServiceBean() {
        ExtensionLoader<ProcessService> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(ProcessService.class);
        ProcessService extension = extensionLoader.getExtension(bootstrap.getProcessServiceExtension());
        logger.info("获取ProcessService扩展实现: {}", extension);
        return extension;
    }
}
