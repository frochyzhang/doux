package com.allinfinance.rpc.consumer.reference;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
class TestConsumerServiceTest {

    @Autowired
    private TestConsumerService testConsumerService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void test1() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        IntStream.rangeClosed(1, 1000)
                .parallel()
                .forEach(i -> {
                    try {
                        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                                .andExpect(MockMvcResultMatchers.status().isOk());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}