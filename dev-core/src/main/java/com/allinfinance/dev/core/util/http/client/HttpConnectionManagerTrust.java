package com.allinfinance.dev.core.util.http.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpConnectionManagerTrust {

    private static final int DEFAULT_MAX_POOL_SIZE = 50;
    private static final Log logger = LogFactory.getLog(HttpConnectionManager.class);
    private PoolingHttpClientConnectionManager connManager = null;
    private int maxPoolSize = 20;
    private int initPoolSize = 5;
    private String charSet = "utf-8";
    private int socketSoTimeOut = 30000;

    public void init() {
        logger.info("*************init httpclient connect pool,charSet="
                + charSet + ",socketSoTimeOut=" + socketSoTimeOut);

        ConnectionConfig connConfig = ConnectionConfig.custom()
                .setCharset(Charset.forName(charSet)).build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketSoTimeOut).build();
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainConnectionSocketFactory);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, (paramArrayOfX509Certificate, paramString) -> true).build();
            LayeredConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            registryBuilder.register("https", sslSf);
        } catch (KeyStoreException e) {
            logger.error("创建受信任的keystore异常", e);
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            logger.error("创建受信任的KeyManagement异常", e);
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("指定信任密钥存储对象和连接套接字工厂异常", e);
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(Math.min(maxPoolSize, DEFAULT_MAX_POOL_SIZE));
        connManager.setDefaultMaxPerRoute(initPoolSize);
        connManager.setDefaultConnectionConfig(connConfig);
        connManager.setDefaultSocketConfig(socketConfig);
        logger.info("httpclient 客户端连接池初始化成功," + connManager.toString());
    }

    /**
     * 获取http/https连接对象
     *
     * @param retryTimes 单个https连接请求重试次数
     * @param timeOut    连接超时时间
     */
    public CloseableHttpClient getHttpClient(final int retryTimes, int timeOut) {

        logger.info("从httpclient客户端连接池中获取连接!");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setRedirectsEnabled(false)
                .build();
        //请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= retryTimes) {
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return !(request instanceof HttpEntityEnclosingRequest);
        };

        //构建客户端
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
        logger.info("获取httpclient连接成功," + httpClient.toString());
        return httpClient;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getInitPoolSize() {
        return initPoolSize;
    }

    public void setInitPoolSize(int initPoolSize) {
        this.initPoolSize = initPoolSize;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public int getSocketSoTimeOut() {
        return socketSoTimeOut;
    }

    public void setSocketSoTimeOut(int socketSoTimeOut) {
        this.socketSoTimeOut = socketSoTimeOut;
    }
}
