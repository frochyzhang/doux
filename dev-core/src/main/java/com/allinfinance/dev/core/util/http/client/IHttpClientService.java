package com.allinfinance.dev.core.util.http.client;

import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 15:33
 */
public interface IHttpClientService {
    String httpRequest(HttpMethod httpMethod, HashMap<String, String> header, String message, String url, int retryTimes, int timeout) throws IOException;
}
