package com.allinfinance.dev.example.conn;

import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import com.allinfinance.dev.connection.pool.scaffold.util.StopWatchExpand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/8
 **/
@SpringBootTest
class WrapperExampleTest {

    private static final Logger logger = LoggerFactory.getLogger(WrapperExampleTest.class);
    @Autowired
    private MessagePorter messagePorter;

    @Test
    public void testSendMsg() {
        StopWatchExpand.init();
        StopWatchExpand.start("first ");
        logger.info(messagePorter.writeAndFlush("shiytrsi26ujyjyikjwaskldfjnmaksl;dfccasdakfasdjfasdpkfjae0iksaed-09ikwp0fuiwsad09dfuawed0fasdfsdsd"));
        StopWatchExpand.stop();

        StopWatchExpand.start("second");
        logger.info(messagePorter.writeAndFlush("shiytrsi26ujyjyikjwaskldfjnmaksl;dfccasdakfasdjfasdpkfjae0iksaed-09ikwp0fuiwsad09dfuawed0fasdfsdsd"));
        StopWatchExpand.stop();


        StopWatchExpand.start("third");
        logger.info(messagePorter.writeAndFlush("shiytrsi26ujyjyikjwaskldfjnmaksl;dfccasdakfasdjfasdpkfjae0iksaed-09ikwp0fuiwsad09dfuawed0fasdfsdsd"));
        StopWatchExpand.stop();


        StopWatchExpand.start("fourth");
        logger.info(messagePorter.writeAndFlush("shiytrsi26ujyjyikjwaskldfjnmaksl;dfccasdakfasdjfasdpkfjae0iksaed-09ikwp0fuiwsad09dfuawed0fasdfsdsd"));
        StopWatchExpand.stop();
        logger.info(StopWatchExpand.prettyPrint());
    }
}