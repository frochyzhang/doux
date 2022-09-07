package com.allinfinance.dev.common.api.http;

import com.allinfinance.dev.common.api.http.dto.HttpRequestDTO;
import com.allinfinance.dev.common.api.http.dto.HttpResponseDTO;

/**
 * @author qipeng
 * @date 2022/9/7 10:09
 * @desc 作为rpc接口暴露服务
 */
public interface HttpClientService {
    /**
     * http请求
     *
     * @param httpRequestDTO 请求内容封装
     * @return 响应请求结果
     */
    HttpResponseDTO request(HttpRequestDTO httpRequestDTO);
}
