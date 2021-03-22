package com.allinfinance.dev.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 张勇
 * @description
 * @date 2020/12/4 17:06
 */
public class TestBatch {
    private static final Logger logger = LoggerFactory.getLogger(TestBatch.class);

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("application-context-test.xml");
    }
}
