package com.allinfinance.dev.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApplication.class, args);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public void test() {

    }
}
