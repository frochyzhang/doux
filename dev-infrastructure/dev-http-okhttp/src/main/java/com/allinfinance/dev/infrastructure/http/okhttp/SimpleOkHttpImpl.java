package com.allinfinance.dev.infrastructure.http.okhttp;

import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.http.driver.SimpleHttp;
import com.allinfinance.dev.framework.http.driver.dto.HttpRequest;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.time.Duration;
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
     * 5分钟之后没有访问过的url就会被删除
     */
    private Cache<String, Call> callCache;

    /**
     * 初始化客户端基本的配置
     */
    @Override
    public void init() {
        this.okHttpClientBuilder = new OkHttpClient.Builder();
        this.callCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(5L))
                .build();
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
        Call call = callCache.getIfPresent(httpRequest.getUrl());
        if (ObjectUtils.isEmpty(call)) {
            call = createCall(httpRequest);
            callCache.put(httpRequest.getUrl(), call);
        }
        String response = null;
        try {
            Response execute = call.execute();
            response = execute.body().string();
        } catch (IOException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("网络IO异常");
        } catch (NullPointerException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("获取响应为空");
        }
        httpResponse.setSuccess(true);
        httpResponse.setResponse(response);
        return httpResponse;
    }

    private Call createCall(HttpRequest httpRequest) {
        Request.Builder builder = new Request.Builder().url(httpRequest.getUrl());
        httpRequest.getHeader().forEach(builder::header);
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
                //默认发送get请求，请求类型判断交给服务端来鉴别
                request = builder.url(httpRequest.getUrl()).build();
                break;
        }

        return okHttpClientBuilder.retryOnConnectionFailure(true)
                .connectTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpRequest.getTimeout(), TimeUnit.SECONDS)
                .build().newCall(request);
    }
}
