package com.allinfinance.dev.connection.scaffold.pool;

import com.allinfinance.dev.connection.scaffold.api.ClientConnection;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author qipeng
 * @date 2022/6/22 16:43
 * @description
 */
public class QueueState {
    /**
     * 连接队列元数据
     */
    protected QueueServerMetadata serverMetadata;
    /**
     * 连接队列，用于存放长连接
     */
    protected LinkedBlockingQueue<ClientConnection> queue;

    public QueueState(QueueServerMetadata serverMetadata, int queueSize) {
        this.serverMetadata = serverMetadata;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public LinkedBlockingQueue<ClientConnection> getQueue() {
        return queue;
    }
}
