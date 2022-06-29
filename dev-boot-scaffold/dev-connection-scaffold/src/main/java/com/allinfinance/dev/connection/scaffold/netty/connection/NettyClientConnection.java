package com.allinfinance.dev.connection.scaffold.netty.connection;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.connection.scaffold.netty.codec.decoder.ByteToHexDecoder;
import com.allinfinance.dev.connection.scaffold.netty.codec.encoder.HexToByteEncoder;
import com.allinfinance.dev.connection.scaffold.netty.context.RequestContext;
import com.allinfinance.dev.connection.scaffold.netty.handler.DefaultHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/6/14 10:44
 */
public class NettyClientConnection extends AbstractClientConnection {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientConnection.class);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP = new DefaultEventLoop(null,
            new NamedThreadFactory("NettyResponsePromiseNotify", false));

    public static final AttributeKey<RequestContext> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    /**
     * 创建长连接
     *
     * @param remoteIp   服务端ip
     * @param remotePort 服务端port
     * @param retryTimes 重试次数
     */
    @Override
    public void connect(String remoteIp, int remotePort, int retryTimes, int bufferSize, int lengthField) {
        this.retryTimes.set(retryTimes);

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
                ch.pipeline()
                        .addLast(new LengthFieldBasedFrameDecoder(bufferSize, 0, lengthField, 0, 2))
                        .addLast(new LengthFieldPrepender(lengthField))
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

    /**
     * 关闭连接
     */
    @Override
    public void close() {
        logger.info("连接 {} 关闭中...", this.hashCode());
        workerGroup.shutdownGracefully();
    }


    /**
     * 发送请求
     *
     * @param msg
     * @return
     */
    @Override
    public String send(String msg) throws SocketException {
        UUID uuid = UUID.fastUUID();
        if (logger.isDebugEnabled()) {
            logger.debug("{}发送请求：{}", uuid, msg);
        }
        Channel channel = this.getChannelFuture().channel();
        if (!channel.isActive()) {
            throw new SocketException("连接异常");
        }

        Promise<String> defaultPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newProgressivePromise();

        RequestContext context = new RequestContext(uuid.toString(), defaultPromise);
        channel.attr(NettyClientConnection.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(context);

        channel.writeAndFlush(msg);

        String resp = get(defaultPromise);
        if (logger.isDebugEnabled()) {
            logger.debug("{}接收到响应：{}", uuid, resp);
        }
        return resp;
    }

    /**
     * 同步获取响应
     *
     * @param future 请求的future
     * @param <V>    请求体类型
     * @return 响应体
     */
    private <V> V get(Future<V> future) {
        logger.info("同步等待响应...");
        if (future instanceof Promise) {
            Promise<V> promise = (Promise<V>) future;
            if (!promise.isDone()) {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                // 监听到响应之后计数器减一
                promise.addListener(resultFuture -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("接收到响应，countDown -1");
                    }
                    if (resultFuture.isDone()) {
                        countDownLatch.countDown();
                    }
                });

                boolean interrupted = false;
                if (!promise.isDone()) {
                    try {
                        countDownLatch.await(this.getRequestTimeout(), TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        logger.error("系统中断异常", e);
                        interrupted = true;
                    }

                }

                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }

            if (promise.isSuccess()) {
                return promise.getNow();
            }
            logger.error("服务响应超时！");
            return null;
        }
        throw new ClassCastException("请求Future类型转换异常，需要：io.netty.util.concurrent.Promise，实际上是：" + future.getClass().getName());
    }
}
