package com.allinfinance.dev.gateway.socket;
import com.allinfinance.dev.boot.socket.autoconfigure.EnableSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 张勇
 * @description
 * @date 2020/11/30 21:01
 */
@EnableSocketServer
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
