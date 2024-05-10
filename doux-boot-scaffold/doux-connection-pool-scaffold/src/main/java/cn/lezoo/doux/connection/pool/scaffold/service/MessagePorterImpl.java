package cn.lezoo.doux.connection.pool.scaffold.service;

import cn.lezoo.doux.connection.pool.scaffold.api.MessagePorter;
import cn.lezoo.doux.connection.pool.scaffold.configure.ConnectionPoolConfigure;
import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    private ConnectionPoolConfigure connectionPoolConfigure;

    private final Random random = new Random();

    /**
     * 获取连接
     */
    protected Connection popConnection() {
        if (logger.isDebugEnabled()) {
            logger.debug("从连接池中获取连接...");
        }
        Connection conn = null;

        // TODO: 2023/5/29 重试次数控制
        AtomicInteger fetchCount = new AtomicInteger(Integer.parseInt(connectionPoolConfigure.getFetchTimes()));
        // 轮询遍历各个连接池，直到找到空闲连接
        while (conn == null && fetchCount.decrementAndGet() >= 0) {
            int index = random.nextInt(serverMetadataList.size());
            ServerMetadata serverMetadata = serverMetadataList.get(index);
            try {
                conn = serverMetadata.getConnection();
            } catch (Exception ignore) {

            }
        }
        if (ObjectUtils.isEmpty(conn)) {
            logger.warn("重试{}次获取连接失败", connectionPoolConfigure.getFetchTimes());
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

        if (ObjectUtils.isNotEmpty(connection)) {
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
        logger.warn("当前连接池繁忙，未取到连接");
        return null;
    }
}
