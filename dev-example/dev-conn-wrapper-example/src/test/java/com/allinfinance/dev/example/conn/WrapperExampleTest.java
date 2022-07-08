package com.allinfinance.dev.example.conn;

import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/8
 **/
@SpringBootTest
class WrapperExampleTest {

    @Autowired
    private MessagePorter messagePorter;

    @Test
    public void testSendMsg() {
        System.out.println(messagePorter.writeAndFlush("shiytrsi26ujyjyikjwaskldfjnmaksl;dfccasdakfasdjfasdpkfjae0iksaed-09ikwp0fuiwsad09dfuawed0fasdfsdsd"));
    }
}