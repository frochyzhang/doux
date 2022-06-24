package com.allinfinance.dev.connection.scaffold.pool;

import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionStatus;
import com.allinfinance.dev.connection.scaffold.netty.connection.AbstractClientConnection;
import io.netty.channel.DefaultEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @author qipeng
 * @date 2022/6/14 18:49
 * @description 提供多个池的统一连接处理
 */
@ConditionalOnBean(name = {"pooledServerMetadataList"})
@Component
public class PoolManager implements MessagePorter, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(PoolManager.class);

    @Autowired
    private List<PooledServerMetadata> pooledServerMetadataList;

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(AbstractClientConnection connection) {
        // 并不真正回收，而是将标志位设置为PENDING，等待下次使用时在回收
        connection.setStatus(ConnectionStatus.PENDING);
    }

    /**
     * 获取连接
     */
    protected AbstractClientConnection popConnection() {
        logger.info("从连接池中获取连接...");
        AbstractClientConnection conn = null;

        // 轮询遍历各个连接池，直到找到空闲连接
        while (conn == null) {
            // 先遍历一遍，优先使用空闲连接
            for (PooledServerMetadata serverMetadata : pooledServerMetadataList) {
                conn = serverMetadata.popConnectionIfIdle();
                if (conn != null) {
                    break;
                }
            }
            // 没找到空闲连接时，再遍历一遍，找到能重连的超时/无效连接
            if (conn == null) {
                PooledServerMetadata pooledServerMetadata = pooledServerMetadataList.get(new Random().nextInt(pooledServerMetadataList.size()));
                conn = pooledServerMetadata.popConnection();
            }
        }

        return conn;
    }

    /**
     * 发送业务请求
     *
     * @param msg
     * @return
     */
    @Override
    public String writeAndFlush(String msg) {
        logger.info("发送请求：{}", msg);
        AbstractClientConnection realConnection = popConnection();

        synchronized (realConnection) {
            try {
                String response = realConnection.send(msg);
                logger.info("接受到响应：{}", response);
                return response;
            } finally {
                pushConnection(realConnection);
            }
        }
    }


    public List<PooledServerMetadata> getServerMetadataList() {
        return pooledServerMetadataList;
    }

    public void setServerMetadataList(List<PooledServerMetadata> serverMetadataList) {
        this.pooledServerMetadataList = serverMetadataList;
    }

    @Override
    public void destroy() throws Exception {
        pooledServerMetadataList.forEach(PooledServerMetadata::forceCloseAll);
    }
}
