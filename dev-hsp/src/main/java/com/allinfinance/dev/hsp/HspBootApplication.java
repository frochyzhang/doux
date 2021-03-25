package com.allinfinance.dev.hsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classname  com.allinfinance.dev.hsp.HspBootApplication
 *
 * @Description TODO
 * @Date 2021/3/24 9:49
 * @Created by ZhangYong
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.allinfinance.dev")
public class HspBootApplication {

    public static void main(String[] args) {
//        new ClassPathXmlApplicationContext("classpath:dev-hsp-context.xml");
        SpringApplication.run(HspBootApplication.class, args);
        log.info("dev-hsp启动成功!");
        //维持进程，不退出
//        synchronized (HspBootApplication.class) {
//            while (true) {
//                try {
//                    HspBootApplication.class.wait();
//                } catch (InterruptedException e) {
//                    log.error("QpsBizMain synchronized error:", e);
//                }
//            }
//        }
    }
}
