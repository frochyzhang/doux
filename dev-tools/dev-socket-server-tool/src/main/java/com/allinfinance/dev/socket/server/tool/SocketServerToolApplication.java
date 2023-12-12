package com.allinfinance.dev.socket.server.tool;

import com.allinfinance.dev.socket.server.tool.handler.SocketServerHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huanghf
 * @date 2023/8/17 9:49
 */
@SpringBootApplication
public class SocketServerToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocketServerHandler.class, args);
    }
}
