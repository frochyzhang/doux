package com.allinfinance.rpc.consumer;

import com.allinfinance.rpc.consumer.reference.TestConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2021/12/31 15:15
 */
@RestController
@SpringBootApplication
public class RpcConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerApplication.class);
    }

    @Autowired
    private TestConsumerService testConsumerService;

    @GetMapping
    public void test() {
        testConsumerService.test();
    }
}
