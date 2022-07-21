package com.allinfinance.dev.hsp.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author huanghf
 * @date 2022/6/27 14:40
 */
@SpringBootApplication(scanBasePackages = "com.allinfinance")
//@ImportResource(locations = {"classpath:dubboConfig.xml"})
public class HspExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(HspExampleApplication.class);
    }
}
