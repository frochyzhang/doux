package com.allinfinance.dev.white.list.diversion.http.client;

import cn.hutool.core.collection.CollectionUtil;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.http.driver.SimpleHttp;
import com.allinfinance.dev.framework.http.driver.dto.HttpRequest;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import com.allinfinance.dev.white.list.diversion.http.config.WhiteListConfig;
import com.allinfinance.dev.white.list.diversion.http.service.HttpRequestDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author huanghf
 * @date 2024/3/18 22:06
 */
@Service
public class ForwardClientService implements InitializingBean {
    private SimpleHttp simpleHttp;

    private final WhiteListConfig whiteListConfig;

    public ForwardClientService(WhiteListConfig whiteListConfig) {
        this.whiteListConfig = whiteListConfig;
    }

    public HttpResponse forward(HttpRequestDTO requestDTO, String mediaType) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        StringBuilder urlBuilder = new StringBuilder(whiteListConfig.getUrl() + requestDTO.getUri());
        if (CollectionUtil.isNotEmpty(requestDTO.getParameters())) {
            urlBuilder.append("?");
            requestDTO.getParameters()
                    .forEach((name, value) -> urlBuilder.append(name).append("=").append(value).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.lastIndexOf("&"));
        }
        httpRequest.setUrl(urlBuilder.toString());
        httpRequest.setHttpMethod(requestDTO.getMethod());
        httpRequest.setHeader(requestDTO.getHeaders());
        httpRequest.setBody(requestDTO.getBody());
        httpRequest.setMediaType(mediaType);
        httpRequest.setTimeout(whiteListConfig.getTimeoutSec());
        return simpleHttp.execute(httpRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        simpleHttp = ExtensionLoaderFactory.getExtension(SimpleHttp.class, "okhttp");
        simpleHttp.init();
    }
}
