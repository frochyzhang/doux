package com.allinfinance.dev.example.dubbo.consumer;

import com.allinfinance.dev.mrp.param.RequestParams;
import com.allinfinance.dev.mrp.service.GenericMqRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "com.allinfinance")
@ImportResource(locations = {"classpath:example-dubbo-consumer-config.xml", "classpath:example-rabbitmq-producer-config.xml"})
public class DubboExampleConsumerApplication {
    private static final Logger logger = LoggerFactory.getLogger(DubboExampleConsumerApplication.class);

//    @Autowired
//    private DynamicDubboService dubboService;
//
//    @Autowired
//    private ExampleInterface exampleInterface;

    @Autowired
    private GenericMqRpcService stringGenericMqRpcService;

    public static void main(String[] args) {
        SpringApplication.run(DubboExampleConsumerApplication.class, args);
    }

//    @Bean
//    public void testDynamicDubboService() {
//        try {
//            exampleInterface = dubboService.getDubboService("0001", ExampleInterface.class);
//        } catch (Exception e) {
//            logger.error("动态获取dubbo服务异常!", e);
//        }
//        logger.info("consumer发送内容:{}", exampleInterface.hello("test dynamicDubboService"));
//    }
//
//    @Bean
//    public void testAutowired() {
//        logger.info("consumer发送内容:{}", exampleInterface.hello("test autowired"));
//    }

//    @Bean
//    public void testGenericRpc() {
//        RequestParams requestParams = new RequestParams();
//        requestParams.setRpcInterface("com.allinfinance.dev.dev.example.dubbo.ExampleInterface");
//        requestParams.setRpcMethod("hello");
//        logger.info("consumer发送内容:{}", stringGenericMqRpcService.genericInvoke("test generic", requestParams));
//    }

    @Bean
    public void testGenericMq() {
        RequestParams requestParams = new RequestParams();
        requestParams.setRpcInterface("com.allinfinance.dev.dev.example.dubbo.ExampleInterface");
        requestParams.setRpcMethod("hello");
        requestParams.setExchangeName("fanout-mq-exchange");
        requestParams.setRoutingKey("test-routing-key");
        logger.info("调用返回结果:{}", stringGenericMqRpcService.<String>genericInvoke(requestParams.toString(), requestParams));
    }

}
