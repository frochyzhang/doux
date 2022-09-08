package com.allinfinance.dev.ccp.service;

import com.alibaba.nacos.common.http.client.request.HttpClientRequest;
import com.allinfinance.dev.common.api.http.HttpClientService;
import com.allinfinance.dev.common.api.http.constant.HttpMethod;
import com.allinfinance.dev.common.api.http.dto.HttpRequestDTO;
import com.allinfinance.dev.common.api.http.dto.HttpResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HttpClientServiceExtraImplTest {

    @Autowired
    private HttpClientService httpClientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void request_get() {
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setUrl("http://localhost:8001/test/get/zhangsan");
        requestDTO.setHttpMethod(HttpMethod.GET);
        requestDTO.setTimeout(1);
        requestDTO.setRetryTime(3);
        HttpResponseDTO responseDTO = httpClientService.request(requestDTO);
        System.out.println(responseDTO);
        Assertions.assertNotNull(responseDTO, "响应dto为空");
        Assertions.assertTrue(responseDTO.getSuccess(), "请求服务端失败，原因" + responseDTO.getBody());
    }

    @Test
    void request_post() {
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setUrl("http://localhost:8001/test/post/zhangsan");
        requestDTO.setHttpMethod(HttpMethod.POST);
        requestDTO.setMediaType("application/json");
        requestDTO.setTimeout(1);
        requestDTO.setBody("{\n" +
                "  \"name\": \"test_e832fd86cdf4\",\n" +
                "  \"age\": 26\n" +
                "}");
        requestDTO.setRetryTime(3);
        HttpResponseDTO responseDTO = httpClientService.request(requestDTO);
        System.out.println(responseDTO);
        Assertions.assertNotNull(responseDTO, "响应dto为空");
        Assertions.assertTrue(responseDTO.getSuccess(), "请求服务端失败，原因" + responseDTO.getBody());
    }

    @Test
    void request_put() {
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setUrl("http://localhost:8001/test/ge/hangsan");
        requestDTO.setHttpMethod(HttpMethod.PUT);
        requestDTO.setTimeout(1);
        requestDTO.setRetryTime(3);
        HttpResponseDTO responseDTO = httpClientService.request(requestDTO);
        System.out.println(responseDTO);
        Assertions.assertNotNull(responseDTO, "响应dto为空");
        Assertions.assertTrue(responseDTO.getSuccess(), "请求服务端失败，原因" + responseDTO.getBody());
    }
}