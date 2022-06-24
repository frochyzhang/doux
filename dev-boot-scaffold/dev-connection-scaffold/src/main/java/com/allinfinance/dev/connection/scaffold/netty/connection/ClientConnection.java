package com.allinfinance.dev.connection.scaffold.netty.connection;

import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionStatus;
import com.allinfinance.dev.connection.scaffold.netty.codec.decoder.ByteToHexDecoder;
import com.allinfinance.dev.connection.scaffold.netty.codec.encoder.HexToByteEncoder;
import com.allinfinance.dev.connection.scaffold.netty.context.RequestContext;
import com.allinfinance.dev.connection.scaffold.netty.handler.DefaultHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qipeng
 * @date 2022/6/14 10:44
 */
public class ClientConnection {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    private ConnectionStatus status;

    /**
     * 连接最后更新时间
     */
    private Long lastUpdateTime = System.currentTimeMillis();

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public AtomicInteger retryTimes = new AtomicInteger(0);

    public static final AttributeKey<RequestContext> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    /**
     * 创建长连接
     *
     * @param remoteIp   服务端ip
     * @param remotePort 服务端port
     * @param retryTimes 重试次数
     */
    public void connect(String remoteIp, int remotePort, int retryTimes) {
        this.retryTimes.set(retryTimes);
//        try {
        Bootstrap b = new Bootstrap();
        // boss-worker不适用于客户端，不需要指定多个group
        b.group(workerGroup);
        //不同于服务端，不用NioServerSocketChannel
        b.channel(NioSocketChannel.class);
        // 没有childOption，因为客户端不区分boss和worker
        b.option(ChannelOption.SO_KEEPALIVE, true);

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(64 * 1024));
                ch.pipeline()
                        .addLast(new ByteToHexDecoder())
                        .addLast(new HexToByteEncoder())
                        .addLast(new DefaultHandler());
            }
        });

        // 客户端调用connect方法，而不是bind绑定端口
        try {
            channelFuture = b.connect(remoteIp, remotePort).sync();
        } catch (InterruptedException e) {
            logger.error("请求服务端连接异常", e);
        }
    }

    public void close() {
        logger.info("连接 {} 关闭中...", this.hashCode());
        workerGroup.shutdownGracefully();
    }

    /**
     * 同步获取响应
     *
     * @param future 请求的future
     * @param <V>    请求体类型
     * @return 响应体
     */
    public <V> V get(Promise<V> future) {
        if (!future.isDone()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // 监听到响应之后计数器减一
            future.addListener(future1 -> {
                if (logger.isDebugEnabled()) {
                    logger.debug("接收到响应，countDown -1");
                }
                if (future1.isDone()) {
                    countDownLatch.countDown();
                }
            });

            boolean interrupted = false;
            if (!future.isDone()) {
                try {
                    countDownLatch.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error("系统中断异常", e);
                    interrupted = true;
                }

            }

            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }

        if (future.isSuccess()) {
            return future.getNow();
        }
        logger.error("服务响应超时！");
        return null;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }
}
