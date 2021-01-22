package com.allinfinance.dev.example.socket;

import com.allinfinance.dev.core.util.validate.BeanConvertValidator;
import com.allinfinance.dev.socket.MinaApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 张勇
 * @description
 * @date 2020/11/30 21:01
 */
public class SocketServerTest {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:application-context-test.xml");
        BeanConvertValidator.beanVerify("abc", "utf-8");
        MinaApplication.run(SocketServerTest.class);
    }
}
