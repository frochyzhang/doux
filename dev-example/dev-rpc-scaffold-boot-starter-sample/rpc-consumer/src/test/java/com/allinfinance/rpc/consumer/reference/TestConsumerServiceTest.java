//package com.allinfinance.rpc.consumer.reference;
//
//import com.allinfinance.dev.common.api.http.HttpClientService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.stream.IntStream;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class TestConsumerServiceTest {
//
//    @Autowired
//    private TestConsumerService testConsumerService;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Test
//    void test1() {
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        IntStream.rangeClosed(1, 1000)
//                .parallel()
//                .forEach(i -> {
//                    try {
//                        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                                .andExpect(MockMvcResultMatchers.status().isOk());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
//
//    @Autowired
//    private SocketClientService iSocketClientService;
//    @Autowired
//    private HttpClientService iHttpClientService;
//
//    @Test
//    public void test2() {
//        IntStream.rangeClosed(1,1000)
//                .parallel()
//                .forEach(i->{
//                    String result = iSocketClientService.clientRequest("127.0.0.1", 13454, "diy", 30, false, "000017alksdjfalskdjfaaa", 6, "UTF-8");
//                    System.out.println(result);
//                    Assertions.assertNotNull(result);
//                });
//    }
//
//    @Test
//    public void test3(){
//        IntStream.rangeClosed(1,10000)
//                .parallel()
//                .forEach(i->{
////                    String result = iHttpClientService.httpRequest(HttpMethod.GET, new HashMap<>(), null, "http://localhost:3000", 3, 30);
//                    String result = null;
//                    try {
//                        result = iHttpClientService.httpRequest(HttpMethod.POST, new HashMap<>(), "{\"name\":\"zy\",\"age\":27,\"dept\":\"dev3\"}", "http://localhost:3000", 3, 30);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(result);
//                    Assertions.assertNotNull(result);
//                });
//    }
//}