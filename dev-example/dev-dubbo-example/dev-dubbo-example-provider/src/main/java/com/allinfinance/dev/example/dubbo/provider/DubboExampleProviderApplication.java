package com.allinfinance.dev.example.dubbo.provider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

// TODO: 2021/1/6 注意：zk版本与依赖的次版本需保持统一。。。。。
@SpringBootApplication(scanBasePackages = "com.allinfinance")
@ImportResource(locations = {"classpath:example-dubbo-provider-config.xml"})
public class DubboExampleProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboExampleProviderApplication.class, args);
    }
}
