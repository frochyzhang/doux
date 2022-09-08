package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.api.http.HttpClientService;
import com.allinfinance.dev.common.api.http.dto.HttpRequestDTO;
import com.allinfinance.dev.common.api.http.dto.HttpResponseDTO;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.http.driver.SimpleHttp;
import com.allinfinance.dev.framework.http.driver.dto.HttpRequest;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author qipeng
 * @date 2022/9/7 18:56
 * @desc
 */
@Service
public class HttpClientServiceExtraImpl implements HttpClientService, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceExtraImpl.class);

    private SimpleHttp simpleHttp;
    /**
     * 默认使用okhttp作为客户端实现
     */
    @Value("${dev.ccp.driver:okhttp}")
    private String driver;

    /**
     * http请求
     *
     * @param httpRequestDTO 请求内容封装
     * @return 响应请求结果
     */
    @Override
    public HttpResponseDTO request(HttpRequestDTO httpRequestDTO) {
        logger.info("接收到请求, url=【{}】, 请求方法=【{}】, 请求头【{}】, 请求体=【{}】", httpRequestDTO.getUrl(),
                httpRequestDTO.getHttpMethod(), httpRequestDTO.getHeader(), httpRequestDTO.getBody());
        HttpResponseDTO httpResponseDTO = null;
        if (StringUtils.isBlank(httpRequestDTO.getUrl())) {
            logger.error("请求参数有误：url不能为空！");
            httpResponseDTO = new HttpResponseDTO(false, "请求url为空");
            return httpResponseDTO;
        }
        HttpResponse httpResponse = null;
        int retryTime = httpRequestDTO.getRetryTime();
        while (retryTime-- > 0 && httpResponse == null) {
            httpResponse = simpleHttp.execute(DTOMapper.INSTANCE.convertToHttpRequest(httpRequestDTO));
        }
        httpResponseDTO = DTOMapper.INSTANCE.convertToHttpResponseDTO(httpResponse);
        logger.info("接收到响应, 响应结果={}, 响应内容={}", httpResponseDTO.getSuccess(), httpResponseDTO.getBody());
        return httpResponseDTO;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExtensionLoader<SimpleHttp> serverMetadataExtensionLoader = ExtensionLoaderFactory.getExtensionLoader(SimpleHttp.class);
        this.simpleHttp = serverMetadataExtensionLoader.getExtension(driver);
        this.simpleHttp.init();
    }
}
