//package com.allinfinance.dev.connection.pool.scaffold.pool;
//
//import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
//import com.allinfinance.dev.framework.conn.driver.Connection;
//import com.allinfinance.dev.framework.conn.wrapper.constant.enums.ConnectionStatus;
//import com.allinfinance.dev.framework.conn.wrapper.pooled.PooledServerMetadata;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.net.SocketException;
//import java.util.List;
//import java.util.Random;
//
///**
// * @author qipeng
// * @date 2022/6/14 18:49
// * @description 提供多个池的统一连接处理
// */
//@Component
//@ConditionalOnProperty(prefix = "com.allinfinance.connection.pool", name = "connectionPoolType", havingValue = "pool")
//public class PoolManager implements MessagePorter {
//    private static final Logger logger = LoggerFactory.getLogger(PoolManager.class);
//
//    @Autowired
//    private List<PooledServerMetadata> pooledServerMetadataList;
//
//    /**
//     * 回收连接
//     *
//     * @param connection
//     */
//    protected void pushConnection(PooledConnection connection) {
//        // 并不真正回收，而是将标志位设置为PENDING，等待下次使用时在回收
//
//    }
//
//    /**
//     * 获取连接
//     */
//    protected PooledC popConnection() {
//        if (logger.isDebugEnabled()) {
//            logger.debug("从连接池中获取连接...");
//        }
//        Connection conn = null;
//
//        // 轮询遍历各个连接池，直到找到空闲连接
//        while (conn == null) {
//            // 先遍历一遍，优先使用空闲连接
//            for (PooledServerMetadata serverMetadata : pooledServerMetadataList) {
//                conn = serverMetadata.popConnectionIfIdle();
//                if (conn != null) {
//                    break;
//                }
//            }
//            // 没找到空闲连接时，再遍历一遍，找到能重连的超时/无效连接
//            if (conn == null) {
//                PooledServerMetadata pooledServerMetadata = pooledServerMetadataList.get(new Random().nextInt(pooledServerMetadataList.size()));
//                conn = pooledServerMetadata.popConnection();
//            }
//        }
//
//        return conn;
//    }
//
//    /**
//     * 发送业务请求
//     *
//     * @param msg
//     * @return
//     */
//    @Override
//    public String writeAndFlush(String msg) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("发送请求：{}", msg);
//        }
//        Connection realConnection = popConnection();
//
//        synchronized (realConnection) {
//            try {
//                String response = realConnection.send(msg);
//                if (logger.isDebugEnabled()) {
//                    logger.debug("接收到响应：{}", response);
//                }
//                return response;
//            } catch (SocketException e) {
//                realConnection.setStatus(ConnectionStatus.INACTIVE);
//                return null;
//            } finally {
//                pushConnection(realConnection);
//            }
//        }
//    }
//
//
//    public List<PooledServerMetadata> getServerMetadataList() {
//        return pooledServerMetadataList;
//    }
//
//    public void setServerMetadataList(List<PooledServerMetadata> serverMetadataList) {
//        this.pooledServerMetadataList = serverMetadataList;
//    }
//
//
//}
