package com.allinfinance.dev.connection.scaffold.pool;

import com.allinfinance.dev.connection.scaffold.netty.connection.AbstractClientConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qipeng
 * @date 2022/6/14 10:39
 * @description 连接池状态维护
 */
public class PoolState {
    /**
     * 池化的服务端元数据
     */
    protected PooledServerMetadata serverMetadata;
    /**
     * 空闲链接列表
     */
    protected volatile List<AbstractClientConnection> idleConnections = new ArrayList<>();
    /**
     * 活跃链接列表
     */
    protected volatile List<AbstractClientConnection> activeConnections = new ArrayList<>();


    public PoolState(PooledServerMetadata metadata) {
        this.serverMetadata = metadata;
    }

    public List<AbstractClientConnection> getIdleConnections() {
        return idleConnections;
    }

    public List<AbstractClientConnection> getActiveConnections() {
        return activeConnections;
    }
}
