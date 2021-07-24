package com.allinfinance.dev.ccs;

import com.allinfinance.dev.ccs.content.RSAKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication(scanBasePackages = "com.allinfinance")
@MapperScan(basePackages = "com.allinfinance.dev.ccs.dal.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
@EnableConfigurationProperties({RSAKeyProperties.class})
public class CCSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CCSApplication.class, args);
    }
}