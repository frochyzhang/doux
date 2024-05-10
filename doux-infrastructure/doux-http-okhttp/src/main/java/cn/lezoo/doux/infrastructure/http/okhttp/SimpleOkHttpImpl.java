package cn.lezoo.doux.infrastructure.http.okhttp;

import cn.hutool.core.collection.CollectionUtil;
import cn.lezoo.doux.framework.extension.annotation.Extension;
import cn.lezoo.doux.framework.http.driver.SimpleHttp;
import cn.lezoo.doux.framework.http.driver.dto.HttpRequest;
import cn.lezoo.doux.framework.http.driver.dto.HttpResponse;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
        Response response;
        String responseBody = null;
        try {
            response = call.execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("网络IO异常");
            return httpResponse;
        } catch (NullPointerException e) {
            httpResponse.setSuccess(false);
            httpResponse.setResponse("获取响应为空");
            return httpResponse;
        }
        Map<String, String> headers = new HashMap<>();
        response.headers()
                .names()
                .forEach(name -> headers.put(name, response.header(name)));
        httpResponse.setSuccess(true);
        httpResponse.setHeaders(headers);
        httpResponse.setResponse(responseBody);
        httpResponse.setHttpStatus(response.code());
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
                request = builder.post(RequestBody.create(MediaType.parse(
                                StringUtils.isEmpty(httpRequest.getMediaType()) ? httpRequest.getHeader("Content-Type") : httpRequest.getMediaType()),
                        httpRequest.getBody())).build();
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
