package com.allinfinance.rpc.consumer.reference;

import com.allinfinance.rpc.consumer.RpcConsumerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RpcConsumerApplication.class)
class TestConsumerServiceTest {

    @Autowired
    private TestConsumerService testConsumerService;

    @Test
    void test1() {
        testConsumerService.test();
    }
}