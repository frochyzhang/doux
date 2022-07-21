package com.allinfinance.dev.connection.scaffold.netty.connection;

import com.allinfinance.dev.connection.scaffold.api.ClientConnection;
import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionStatus;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/24
 **/
public abstract class AbstractClientConnection implements ClientConnection {

    private ConnectionStatus status;

    /**
     * 连接最后更新时间
     */
    private Long lastUpdateTime = System.currentTimeMillis();
    /**
     * 超时重试次数
     */
    public AtomicInteger retryTimes = new AtomicInteger(0);
    /**
     * 超时请求时间
     */
    private Integer requestTimeout;

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
