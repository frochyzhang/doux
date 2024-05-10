package cn.lezoo.doux.dispatch.scaffold;

import cn.lezoo.doux.dispatch.scaffold.config.JobExecutorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author qipeng
 * @date 2022/1/7 16:17
 */
@EnableConfigurationProperties(JobExecutorProperties.class)
@ComponentScan(basePackages = DispatchScaffoldConfiguration.SCAFFOLD_BASE_PACKAGE_PREFIX)
@Configuration
@PropertySource("classpath:xxl-job-executor.properties")
public class DispatchScaffoldConfiguration {
    public static final String SCAFFOLD_BASE_PACKAGE_PREFIX = "cn.lezoo.doux.dispatch.scaffold";
    private static final Logger logger = LoggerFactory.getLogger(DispatchScaffoldConfiguration.class);

    public DispatchScaffoldConfiguration() {
        logger.info("Dispatch脚手架加载成功!");
    }

}
