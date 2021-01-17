package com.allinfinance.dev.example.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "com.allinfinance")
@ImportResource(locations = {"classpath:example-rabbitmq-producer-config.xml"})
public class RabbitMqExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqExampleApplication.class, args);
    }
}
