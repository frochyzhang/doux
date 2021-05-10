package com.allinfinance.dev.hsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

/**
 * Classname  com.allinfinance.dev.hsp.HspBootApplication
 *
 * @Description TODO
 * @Date 2021/3/24 9:49
 * @Created by ZhangYong
 */
@Slf4j
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.allinfinance.dev")
@ImportResource(locations = {"classpath:hsp-rpc-context.xml"})
public class HspBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HspBootApplication.class, args);
        log.info("dev-hsp启动成功!");
    }
}
