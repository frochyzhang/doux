package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.core.util.http.client.IHttpClientService;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 17:50
 */
@Service
public class HttpClientServiceImpl implements IHttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);
    private static final OkHttpClient.Builder OK_HTTP_CLIENT_BUILDER = new OkHttpClient.Builder();

    @Override
    public String httpRequest(HttpMethod httpMethod, HashMap<String, String> header, String message, String url, int retryTimes, int timeout) throws IOException {
        logger.info("开始请求【{}】,请求方法【{}】,请求消息【{}】,请求头【{}】", url, httpMethod, message, header);
        Request.Builder builder = new Request.Builder().url(url);
        header.forEach(builder::header);
        switch (httpMethod) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), message));
                break;
            default:
                throw new IllegalArgumentException("请求方法不正确");
        }
        final Request request = builder.build();
        Call call = OK_HTTP_CLIENT_BUILDER
                .retryOnConnectionFailure(true)
                .connectTimeout(timeout, TimeUnit.SECONDS) //连接超时
                .readTimeout(timeout, TimeUnit.SECONDS) //读取超时
                .writeTimeout(timeout, TimeUnit.SECONDS) //写超时
                .build()
                .newCall(request);
        Response response = null;
        String resp;
        try {
            response = call.execute();
            resp = response.body().string();
        } catch (NullPointerException | IOException e) {
            logger.error("调用{}失败.", url);
            throw e;
        } finally {
            logger.info("请求完成，响应为: {}", response);
        }

        return resp;
    }
}
