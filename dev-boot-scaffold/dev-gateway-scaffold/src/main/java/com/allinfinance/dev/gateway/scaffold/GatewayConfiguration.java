package com.allinfinance.dev.gateway.scaffold;

import com.allinfinance.dev.gateway.scaffold.config.Bootstrap;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/11/8 16:32
 */
@EnableConfigurationProperties(Bootstrap.class)
@Configuration
public class GatewayConfiguration {
}
