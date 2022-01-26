package com.allinfinance.dev.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/11 19:49
 */
@SpringBootApplication(scanBasePackages = "com.allinfinance.dev.batch")
public class BatchSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchSampleApplication.class, args);
    }
}
