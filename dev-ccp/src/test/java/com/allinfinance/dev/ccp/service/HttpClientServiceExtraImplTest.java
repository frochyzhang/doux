package com.allinfinance.dev.ccp.service;

import cn.hutool.extra.spring.SpringUtil;
import com.allinfinance.dev.common.api.http.HttpClientService;
import com.allinfinance.dev.common.api.http.constant.HttpMethod;
import com.allinfinance.dev.common.api.http.dto.HttpRequestDTO;
import com.allinfinance.dev.common.api.http.dto.HttpResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest
public class HttpClientServiceExtraImplTest extends AbstractBenchmark {

    @Autowired
    private HttpClientService httpClientService;

    @Test
    void request_get() {
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setUrl("http://localhost:8001/test/get/zhangsan");
        requestDTO.setHttpMethod(HttpMethod.GET);
        requestDTO.setTimeout(1);
//        requestDTO.setRetryTime(3);
        HttpResponseDTO responseDTO = httpClientService.request(requestDTO);
        System.out.println(responseDTO);
        Assertions.assertNotNull(responseDTO, "响应dto为空");
        Assertions.assertTrue(responseDTO.getSuccess(), "请求服务端失败，原因" + responseDTO.getBody());
    }

    @Benchmark
    public void request_post() {
        httpClientService = SpringUtil.getBean(HttpClientService.class);
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
//        Assertions.assertNotNull(responseDTO, "响应dto为空");
//        Assertions.assertTrue(responseDTO.getSuccess(), "请求服务端失败，原因" + responseDTO.getBody());
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