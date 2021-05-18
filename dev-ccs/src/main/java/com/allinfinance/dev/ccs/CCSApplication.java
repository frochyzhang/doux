package com.allinfinance.dev.ccs;

import com.allinfinance.dev.dal.mybatis.MapperScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.allinfinance")
@MapperScanner(basePackages = "com.allinfinance.dev.ccs.dal.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
public class CCSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CCSApplication.class,args);
    }
}