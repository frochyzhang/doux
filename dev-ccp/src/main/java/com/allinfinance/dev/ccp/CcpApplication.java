package com.allinfinance.dev.ccp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 15:29
 */
@SpringBootApplication
public class CcpApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder(CcpApplication.class).build(args);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run();
    }
}
