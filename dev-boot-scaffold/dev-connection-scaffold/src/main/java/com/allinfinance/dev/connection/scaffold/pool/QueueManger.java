package com.allinfinance.dev.connection.scaffold.pool;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.connection.scaffold.netty.connection.ClientConnection;
import com.allinfinance.dev.connection.scaffold.netty.context.RequestContext;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/6/22 17:01
 * @description
 */
@Component
public class QueueManger implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(QueueManger.class);

    @Autowired
    private List<QueueServerMetadata> queueServerMetadataList;

    public static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP = new DefaultEventLoop(null, new NamedThreadFactory("NettyResponsePromiseNotify", false));

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(ClientConnection connection, QueueServerMetadata serverMetadata) {
        connection.setLastUpdateTime(System.currentTimeMillis());
        serverMetadata.pushConnection(connection);
    }

    /**
     * 获取连接
     */
    protected ClientConnection popConnection() {
        logger.info("从连接池中获取连接...");
        ClientConnection conn = null;

        // 轮询遍历各个连接池，直到找到空闲连接
        while (conn == null) {
            // 先遍历一遍，优先使用空闲连接
            for (QueueServerMetadata serverMetadata : queueServerMetadataList) {
                conn = serverMetadata.popConnection();
                if (conn != null) {
                    if (System.currentTimeMillis() - conn.getLastUpdateTime() > 5 * 60 * 1000) {
                        if (serverMetadata.pingConnection(conn)) {
                            logger.info("老头连接有效，返回该连接：{}", conn.hashCode());
                            pushConnection(conn, serverMetadata);
                            break;
                        }
                    } else {
                        logger.info("小鲜肉连接，返回该连接：{}", conn.hashCode());
                        pushConnection(conn, serverMetadata);
                        break;

                    }
                }
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
    public String writeAndFlush(String msg) {
        ClientConnection realConnection = popConnection();

        synchronized (realConnection) {
            UUID uuid = UUID.fastUUID();
            logger.info("{} 发送请求：{}", uuid, msg);
            Channel channel = realConnection.getChannelFuture().channel();

            Promise<String> defaultPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newProgressivePromise();

            RequestContext context = new RequestContext(uuid.toString(), defaultPromise);
            channel.attr(ClientConnection.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(context);

            channel.writeAndFlush(msg);

            String response = realConnection.get(defaultPromise);
            logger.info("{} 接受到响应：{}", uuid, response);
            return response;
        }
    }


    @Override
    public void destroy() {
        queueServerMetadataList.forEach(QueueServerMetadata::forceCloseAll);
    }
}
