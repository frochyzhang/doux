package com.allinfinance.dev.feign.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DevFeignExampleApplicationTest {

    @Autowired
    DevFeignExampleApplication.MyService myService;

    @Test
    void test() {
        System.out.println(myService.doSomething("alskdfalsdjflasdjlaskdflas"));
    }
}