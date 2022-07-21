package com.allinfinance.dev.connection.pool.scaffold.service;

import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @author qipeng
 * @date 2022/6/14 18:49
 * @description 提供多个池的统一连接处理
 */
@Component
public class MessagePorterImpl implements MessagePorter {
    private static final Logger logger = LoggerFactory.getLogger(MessagePorterImpl.class);

    @Autowired
    private List<ServerMetadata> serverMetadataList;

    private final Random random = new Random();

    /**
     * 获取连接
     */
    protected Connection popConnection() {
        if (logger.isDebugEnabled()) {
            logger.debug("从连接池中获取连接...");
        }
        Connection conn = null;

        // 轮询遍历各个连接池，直到找到空闲连接
        while (conn == null) {
            int index = random.nextInt(serverMetadataList.size());
            ServerMetadata serverMetadata = serverMetadataList.get(index);
            conn = serverMetadata.getConnection();
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
        if (logger.isDebugEnabled()) {
            logger.debug("发送请求：{}", msg);
        }
        Connection connection = popConnection();

        synchronized (connection) {
            try {
                String response = connection.send(msg);
                if (logger.isDebugEnabled()) {
                    logger.debug("接收到响应：{}", response);
                }
                return response;
            } catch (Throwable e) {
                return null;
            } finally {
                connection.close();
            }
        }
    }
}
