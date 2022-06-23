package com.allinfinance.dev.connection.scaffold.pool;

import com.allinfinance.dev.connection.scaffold.netty.connection.ClientConnection;

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
    protected volatile List<ClientConnection> idleConnections = new ArrayList<>();
    /**
     * 活跃链接列表
     */
    protected volatile List<ClientConnection> activeConnections = new ArrayList<>();


    public PoolState(PooledServerMetadata metadata) {
        this.serverMetadata = metadata;
    }

    public List<ClientConnection> getIdleConnections() {
        return idleConnections;
    }

    public List<ClientConnection> getActiveConnections() {
        return activeConnections;
    }
}
