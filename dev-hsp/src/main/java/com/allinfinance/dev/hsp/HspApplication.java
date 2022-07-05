package com.allinfinance.dev.hsp;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ImportResource;

/**
 * @author huanghf
 * @date 2022/6/23 14:35
 */
@EnableDubboConfig
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.allinfinance")
@ImportResource(locations = {"${dev.dubbo.files}"})
public class HspApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder(HspApplication.class).build(args);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run();
    }
}
