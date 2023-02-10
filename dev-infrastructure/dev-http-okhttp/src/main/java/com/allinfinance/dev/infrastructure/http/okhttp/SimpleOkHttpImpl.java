package com.allinfinance.dev.infrastructure.http.okhttp;

import cn.hutool.core.collection.CollectionUtil;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.http.driver.SimpleHttp;
import com.allinfinance.dev.framework.http.driver.dto.HttpRequest;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/9/7 16:08
 * @desc SimpleHttp的okhttp实现
 */
@Extension("okhttp")
public class SimpleOkHttpImpl implements SimpleHttp {
    private OkHttpClient.Builder okHttpClientBuilder;

    /**
     * 初始化客户端基本的配置
     */
    @Override
    public void init() {
        this.okHttpClientBuilder = new OkHttpClient.Builder();
    }

    /**
     * 发送请求
     *
     * @param httpRequest
     * @return
     */
    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        Call call = createCall(httpRequest);
        String response = null;
        try {
            Response execute = call.execute();
            response = execute.body().string();
        } catch (IOException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("网络IO异常");
            return httpResponse;
        } catch (NullPointerException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("获取响应为空");
            return httpResponse;
        }
        httpResponse.setSuccess(true);
        httpResponse.setResponse(response);
        return httpResponse;
    }

    private Call createCall(HttpRequest httpRequest) {
        Request.Builder builder = new Request.Builder().url(httpRequest.getUrl());
        if (CollectionUtil.isNotEmpty(httpRequest.getHeader())) {
            httpRequest.getHeader().forEach(builder::header);
        }
        String httpMethod = httpRequest.getHttpMethod();
        Request request = null;
        switch (httpMethod) {
            case "GET":
                request = builder.get()
                        .build();
                break;
            case "POST":
                request = builder.post(RequestBody.create(MediaType.get(httpRequest.getMediaType()), httpRequest.getBody()))
                        .build();
                break;
            // TODO: 2022/9/7 暂时只做了get/post两种请求，后续可继续完善
            default:
                request = builder.build();
                break;
        }

        return okHttpClientBuilder.retryOnConnectionFailure(true)
                .connectTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .build().newCall(request);
    }
}
