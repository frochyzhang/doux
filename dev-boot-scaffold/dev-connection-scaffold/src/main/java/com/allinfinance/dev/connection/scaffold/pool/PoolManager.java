package com.allinfinance.dev.connection.scaffold.pool;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionStatus;
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
import java.util.Random;

/**
 * @author qipeng
 * @date 2022/6/14 18:49
 * @description 提供多个池的统一连接处理
 */
@Component
public class PoolManager implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(PoolManager.class);

    @Autowired
    private List<PooledServerMetadata> pooledServerMetadataList;

    public static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP = new DefaultEventLoop(null, new NamedThreadFactory("NettyResponsePromiseNotify", false));

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(ClientConnection connection) {
        // 并不真正回收，而是将标志位设置为PENDING，等待下次使用时在回收
        connection.setStatus(ConnectionStatus.PENDING);
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
    public String writeAndFlush(String msg) {
        logger.info("发送请求：{}", msg);
        ClientConnection realConnection = popConnection();

        synchronized (realConnection) {
            try {
                Channel channel = realConnection.getChannelFuture().channel();

                Promise<String> defaultPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newPromise();

                RequestContext context = new RequestContext(UUID.fastUUID().toString(), defaultPromise);
                channel.attr(ClientConnection.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(context);

                channel.writeAndFlush(msg);

                String response = realConnection.get(defaultPromise);
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
