package com.allinfinance.dev.hsp;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author huanghf
 * @date 2022/6/23 14:35
 */
@SpringBootApplication(scanBasePackages = "com.allinfinance")
public class HspApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder(HspApplication.class).build(args);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run();
    }

    @ConditionalOnProperty(prefix = "com.allinfinance.hsp", name = "dubbo-enabled", havingValue = "true")
    @EnableDubboConfig
    @EnableDubbo
    @Configuration
    @ImportResource(locations = {"${dev.dubbo.files}"})
    @EnableConfigurationProperties
    @ConfigurationProperties(prefix = "com.allinfinance.hsp")
    public static class DubboEnable {
        /**
         * 是否启用DUBBO
         */
        private Boolean dubboEnabled = Boolean.FALSE;

        public Boolean getDubboEnabled() {
            return dubboEnabled;
        }

        public void setDubboEnabled(Boolean dubboEnabled) {
            this.dubboEnabled = dubboEnabled;
        }

        @Override
        public String toString() {
            return "DubboEnable{" +
                    "dubboEnabled=" + dubboEnabled +
                    '}';
        }
    }
}
