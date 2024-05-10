package cn.lezoo.doux.white.list.diversion.socket;

import cn.lezoo.doux.white.list.diversion.socket.config.WhiteListConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huanghf
 * @date 2024/3/19 11:42
 */
@Configuration
@EnableConfigurationProperties(WhiteListConfig.class)
@ComponentScan(basePackages = "cn.lezoo.doux.white.list.diversion")
public class WhiteListDiversionAutoConfiguration {
}
