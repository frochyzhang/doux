package com.allinfinance.dev.example.dubbo.provider.impl;

import com.allinfinance.dev.dev.example.dubbo.ExampleInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("exampleService")
public class ExampleInterfaceImpl implements ExampleInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExampleInterfaceImpl.class);

    @Override
    public String hello(String msg) {
        logger.info("provider接收内容:{}", msg);
        return msg;
    }
}
