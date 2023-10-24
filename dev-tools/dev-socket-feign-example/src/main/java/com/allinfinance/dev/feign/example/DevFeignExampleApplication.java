package com.allinfinance.dev.feign.example;

import com.allinfinance.dev.feign.DevFeign;
import com.allinfinance.dev.feign.DevRequestLine;
import com.allinfinance.dev.feign.scaffold.EnableDevFeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 20:33
 */
@EnableDevFeign
@SpringBootApplication
public class DevFeignExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevFeignExampleApplication.class);
    }

    @DevFeign(name = "myService", url = "localhost:8080")
    interface MyService {
        @DevRequestLine
        String doSomething(String abcd);
    }
}
