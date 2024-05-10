package cn.lezoo.doux.white.list.diversion.http;

import cn.lezoo.doux.white.list.diversion.http.config.TrafficBackupConfig;
import cn.lezoo.doux.white.list.diversion.http.config.WhiteListConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huanghf
 * @date 2024/3/19 11:42
 */
@Configuration
@EnableConfigurationProperties({WhiteListConfig.class, TrafficBackupConfig.class})
@ComponentScan(basePackages = "cn.lezoo.doux.white.list.diversion.http")
public class WhiteListDiversionAutoConfiguration {
}
