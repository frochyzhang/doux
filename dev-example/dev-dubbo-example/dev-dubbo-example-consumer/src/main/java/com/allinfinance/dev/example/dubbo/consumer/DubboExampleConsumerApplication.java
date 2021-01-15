package com.allinfinance.dev.example.dubbo.consumer;

import com.allinfinance.dev.dev.example.dubbo.ExampleInterface;
import com.allinfinance.dev.dubbo.config.DynamicDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:example-dubbo-consumer-config.xml"})
public class DubboExampleConsumerApplication {
    private static final Logger logger = LoggerFactory.getLogger(DubboExampleConsumerApplication.class);

    @Autowired
    private DynamicDubboService dubboService;

    @Autowired
    private ExampleInterface exampleInterface;

    public static void main(String[] args) {
        SpringApplication.run(DubboExampleConsumerApplication.class, args);
    }

    @Bean
    public void testDynamicDubboService() {
        try {
            exampleInterface = dubboService.getDubboService("0001", ExampleInterface.class);
        } catch (Exception e) {
            logger.error("动态获取dubbo服务异常!", e);
        }
        logger.info("consumer发送内容:{}", exampleInterface.hello("test dynamicDubboService"));
    }

    @Bean
    public void testAutowired() {
        logger.info("consumer发送内容:{}", exampleInterface.hello("test autowired"));
    }
}
