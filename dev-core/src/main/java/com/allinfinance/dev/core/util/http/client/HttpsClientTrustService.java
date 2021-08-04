package com.allinfinance.dev.core.util.http.client;

import com.allinfinance.dev.core.loader.SpringConfigTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class HttpsClientTrustService {
    private static final String DEFAULT_CONTENT_TYPE = "application/xml";
    private final Logger logger = LoggerFactory.getLogger(HttpClientService.class);
    private HttpConnectionManagerTrust httpConnectionManager;

    public String httpRequest(HashMap<String, String> header, String request, String url, int retryTimes, int timeout) {
        logger.info("httpRequest开始!");

        if (StringUtils.isBlank(request)
                || StringUtils.isBlank(url)) {
            logger.error("请求参数错误!");
            return null;
        }
        if (retryTimes <= 0) {
            retryTimes = 3;
        }
        if (timeout <= 0) {
            timeout = 30000;
        }
        CloseableHttpResponse httpResponse;
        String response;
        try {
            httpConnectionManager = (HttpConnectionManagerTrust) SpringConfigTool.getBeanByBeanName("httpConnectionManagerTrust");

            StringEntity stringEntity = new StringEntity(request, httpConnectionManager.getCharSet());
            HttpPost httpPost = new HttpPost(url);
            if (header == null) {
                logger.error("消息头信息为null，请检查请求参数");
                return null;
            }
            String contentType = header.get("contentType");
            stringEntity.setContentType(StringUtils.isNotBlank(contentType) ? contentType : DEFAULT_CONTENT_TYPE);
            //添加消息头信息
            for (String s : header.keySet()) {
                httpPost.setHeader(s, header.get(s));
            }

            httpPost.setEntity(stringEntity);
            httpResponse = httpConnectionManager.getHttpClient(retryTimes, timeout).execute(httpPost);
            if (httpResponse == null) {
                logger.error("http应答为空!");
                return null;
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                logger.info("http post 请求成功!");
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                httpResponse.close();
                if (StringUtils.isEmpty(response)) {
                    logger.error("http应答报文为空!");
                    return null;
                }
                logger.info("http应答报文:" + response);
            } else {
                logger.error("http post 请求失败! statusCode:" + statusCode);
                httpResponse.close();
                return null;
            }
            return response;
        } catch (Exception e) {
            logger.error("系统异常!", e);
            return null;
        }
    }

    public HashMap<String, Object> httpRequest(HashMap<String, String> header, byte[] request, String url, int retryTimes, int timeout) {
        logger.info("httpRequest开始!");

        if (null == request
                || request.length == 0
                || StringUtils.isBlank(url)) {
            logger.error("请求参数错误!");
            return null;
        }
        if (retryTimes <= 0) {
            retryTimes = 3;
        }
        if (timeout <= 0) {
            timeout = 30000;
        }
        CloseableHttpResponse httpResponse;
        try {
            httpConnectionManager = (HttpConnectionManagerTrust) SpringConfigTool.getBeanByBeanName("httpConnectionManagerTrust");

            HttpPost httpPost = new HttpPost(url);
            if (header == null) {
                logger.error("消息头信息为null,请检查请求参数");
                return null;
            }

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(request);
            String contentType = header.get("contentType");
            byteArrayEntity.setContentType(StringUtils.isNotBlank(contentType) ? contentType : DEFAULT_CONTENT_TYPE);
            //添加消息头信息
            for (String s : header.keySet()) {
                httpPost.setHeader(s, header.get(s));
            }

            httpPost.setEntity(byteArrayEntity);
            httpResponse = httpConnectionManager.getHttpClient(retryTimes, timeout).execute(httpPost);
            if (httpResponse == null) {
                logger.error("http应答为空!");
                return null;
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                logger.info("http post 请求成功!");
                HashMap<String, Object> map = new HashMap<>();
                Header[] httpHeaders = httpResponse.getAllHeaders();
                HttpEntity httpEntity = httpResponse.getEntity();
                byte[] httpBody = EntityUtils.toByteArray(httpEntity);
                httpResponse.close();
                if (null == httpBody || httpBody.length == 0) {
                    logger.info("http应答报文体为空!");
                    return map;
                }
                map.put("header", httpHeaders);
                map.put("body", httpBody);
                for (Header header1 : httpHeaders) {
                    logger.info("header:name:{},value:{}", header1.getName(), header1.getValue());
                }
                logger.info("body:{}", Arrays.toString(httpBody));
                return map;
            } else {
                logger.error("http post 请求失败! statusCode:" + statusCode);
                httpResponse.close();
                return null;
            }
        } catch (Exception e) {
            logger.error("系统异常!", e);
            return null;
        }
    }
}
