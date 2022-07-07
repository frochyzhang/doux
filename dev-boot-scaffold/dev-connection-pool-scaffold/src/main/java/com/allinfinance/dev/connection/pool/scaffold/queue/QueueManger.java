//package com.allinfinance.dev.connection.pool.scaffold.queue;
//
//import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
//import com.allinfinance.dev.framework.conn.wrapper.queue.QueueConnection;
//import com.allinfinance.dev.framework.conn.wrapper.queue.QueueServerMetadata;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.net.SocketException;
//import java.util.List;
//
///**
// * @author qipeng
// * @date 2022/6/22 17:01
// * @description
// */
//@Component
//@ConditionalOnProperty(prefix = "com.allinfinance.connection.pool", name = "connectionPoolType", havingValue = "queue")
//public class QueueManger implements MessagePorter {
//    private static final Logger logger = LoggerFactory.getLogger(QueueManger.class);
//
//    @Autowired
//    private List<QueueServerMetadata> queueServerMetadataList;
//
//    /**
//     * 回收连接
//     *
//     * @param connection
//     */
//    protected void pushConnection(QueueConnection connection, QueueServerMetadata serverMetadata) {
//        connection.setLastUpdateTime(System.currentTimeMillis());
//        serverMetadata.pushConnection(connection);
//    }
//
//    /**
//     * 获取连接
//     */
//    protected QueueConnection popConnection() {
//        logger.info("从连接池中获取连接...");
//        QueueConnection conn = null;
//
//        // 轮询遍历各个连接池，直到找到空闲连接
//        while (conn == null) {
//            // 先遍历一遍，优先使用空闲连接
//            for (QueueServerMetadata serverMetadata : queueServerMetadataList) {
//                conn = serverMetadata.popConnection();
//                if (conn != null) {
//                    if (System.currentTimeMillis() - conn.getLastUpdateTime() > serverMetadata.idleConnectionCheckoutTime) {
//                        if (serverMetadata.pingConnection(conn)) {
//                            if (logger.isDebugEnabled()) {
//                                logger.debug("老头连接有效，返回该连接：{}", conn.hashCode());
//                            }
//                            pushConnection(conn, serverMetadata);
//                            break;
//                        } else if (ConnectionStatus.TIMEOUT.equals(conn.getStatus())) {
//                            // ping连接失败
//                            if (logger.isDebugEnabled()) {
//                                logger.debug("老头连接超时，等待重试：{}", conn.hashCode());
//                            }
//                            conn = null;
//                        } else if (ConnectionStatus.INACTIVE.equals(conn.getStatus())) {
//                            // 连接重试失败，重新创建新连接
//                            if (logger.isDebugEnabled()) {
//                                logger.debug("连接失效，新建连接");
//                            }
//                            serverMetadata.addConnection();
//                            conn = null;
//                        }
//                    } else {
//                        if (logger.isDebugEnabled()) {
//                            logger.debug("小鲜肉连接，返回该连接：{}", conn.hashCode());
//                        }
//                        pushConnection(conn, serverMetadata);
//                        break;
//                    }
//                }
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
//        AbstractClientConnection realConnection = popConnection();
//
//        synchronized (realConnection) {
//            String response = null;
//            try {
//                response = realConnection.send(msg);
//            } catch (SocketException e) {
//                realConnection.setStatus(ConnectionStatus.INACTIVE);
//                logger.error("发送请求异常", e);
//                return null;
//            }
//            if (logger.isDebugEnabled()) {
//                logger.debug("接受到响应：{}", response);
//            }
//            return response;
//        }
//    }
//
//}
