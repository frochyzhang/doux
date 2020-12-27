package com.allinfinance.dev.socket.test;

import com.allinfinance.dev.socket.MinaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 张勇
 * @description
 * @date 2020/11/30 21:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class SocketServerTest {
    @Test
    public void test() {
        MinaApplication.run(SocketServerTest.class);
    }
}
