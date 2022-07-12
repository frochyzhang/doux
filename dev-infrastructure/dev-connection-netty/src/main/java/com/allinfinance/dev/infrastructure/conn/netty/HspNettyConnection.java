package com.allinfinance.dev.infrastructure.conn.netty;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.infrastructure.conn.netty.codec.ByteToHexDecoder;
import com.allinfinance.dev.infrastructure.conn.netty.codec.HexToByteEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ProgressivePromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/12
 **/
@Extension("hsp")
public class HspNettyConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(HspNettyConnection.class);

    private final EventLoopGroup loopGroup = new NioEventLoopGroup(10,
            new NamedThreadFactory("hsp-netty-", false));

    private ChannelFuture channelFuture;

    private static final DefaultEventLoop NETTY_EVENT_LOOP = new DefaultEventLoop(null, new NamedThreadFactory("NETTY_EVENT_LOOP", false));

    private static final AttributeKey<RequestContext> REQUEST_CONTEXT_ATTRIBUTE_KEY = AttributeKey.valueOf("REQUEST_CONTEXT_ATTRIBUTE_KEY");

    @Override
    public void setNetworkTimeout(ExecutorService executor, Integer timeout) {

    }

    @Override
    public void close() {
        loopGroup.shutdownGracefully();
    }

    @Override
    public boolean isClosed() {
        return loopGroup.isShutdown();
    }

    @Override
    public String send(String msg) {
        Channel channel = channelFuture.channel();

        ProgressivePromise<String> promise = NETTY_EVENT_LOOP.newProgressivePromise();

        RequestContext requestContext = new RequestContext(UUID.fastUUID().toString(), promise);
        channel.attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).set(requestContext);

        channel.writeAndFlush(msg);

        // FIXME: 2022/6/30 记得处理我
        try {
            return promise.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("处理中断");
        } catch (ExecutionException e) {
            logger.error("处理异常");
        } catch (TimeoutException e) {
            logger.error("获取响应超时");
        }
        throw new RuntimeException("获取响应异常");
    }

    @Override
    public void connect(Properties properties) {
        String serverIp = properties.getProperty("serverIp");
        int serverPort = Integer.parseInt(properties.getProperty("serverPort"));
        int lengthField = Integer.parseInt(properties.getProperty("lengthField"));
        int bufferSize = Integer.parseInt(properties.getProperty("bufferSize"));

        try {
            channelFuture = new Bootstrap()
                    .group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            if (lengthField != 0) {
                                // 长度域不为0时才注册长度解析的decoder和encoder
                                ch.pipeline()
                                        .addLast(new LengthFieldBasedFrameDecoder(bufferSize, 0, lengthField, 0, 2))
                                        .addLast(new LengthFieldPrepender(lengthField))
                                        .addLast(new ByteToHexDecoder())
                                        .addLast(new HexToByteEncoder())
                                        .addLast(new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                RequestContext requestContext = ctx.channel().attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).get();
                                                requestContext.getRespPromise().setSuccess((String) msg);
                                            }
                                        });
                            } else {
                                ch.pipeline()
                                        .addLast(new ByteToHexDecoder())
                                        .addLast(new HexToByteEncoder())
                                        .addLast(new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                RequestContext requestContext = ctx.channel().attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).get();
                                                requestContext.getRespPromise().setSuccess((String) msg);
                                            }
                                        });
                            }
//                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                                @Override
//                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    RequestContext requestContext = ctx.channel().attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).get();
//                                    requestContext.getRespPromise().setSuccess((String) msg);
//                                }
//                            });
                        }
                    }).connect(serverIp, serverPort).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
