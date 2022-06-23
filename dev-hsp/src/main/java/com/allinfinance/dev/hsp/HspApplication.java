package com.allinfinance.dev.hsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * @author huanghf
 * @date 2022/6/23 14:35
 */
@SpringBootApplication
public class HspApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder(HspApplication.class).build(args);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run();
    }
}
