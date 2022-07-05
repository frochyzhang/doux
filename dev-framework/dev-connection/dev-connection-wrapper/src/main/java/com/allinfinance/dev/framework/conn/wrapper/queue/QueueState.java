package com.allinfinance.dev.framework.conn.wrapper.queue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
public class QueueState {
    /**
     * 连接队列元数据
     */
    protected QueueServerMetadata serverMetadata;
    /**
     * 连接队列，用于存放长连接
     */
    protected LinkedBlockingQueue<QueueConnection> queue;

    public QueueState(QueueServerMetadata serverMetadata, int queueSize) {
        this.serverMetadata = serverMetadata;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public LinkedBlockingQueue<QueueConnection> getQueue() {
        return queue;
    }
}
