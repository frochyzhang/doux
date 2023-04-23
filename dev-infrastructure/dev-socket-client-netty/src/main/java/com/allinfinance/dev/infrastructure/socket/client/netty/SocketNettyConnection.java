package com.allinfinance.dev.infrastructure.socket.client.netty;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.socket.client.driver.Connection;
import com.allinfinance.dev.infrastructure.socket.client.netty.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.infrastructure.socket.client.netty.codec.DemuxingMessageEncoder;
import com.allinfinance.dev.infrastructure.socket.client.netty.codec.Message8583Decoder;
import com.allinfinance.dev.infrastructure.socket.client.netty.codec.Message8583Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ProgressivePromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/9/14 9:20
 */
@Extension("netty")
public class SocketNettyConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(SocketNettyConnection.class);

    private final EventLoopGroup loopGroup = new NioEventLoopGroup(16);

    private static final ThreadLocal<ChannelFuture> channelFuture = new ThreadLocal<>();

    private int timeout;

    private static final DefaultEventLoop NETTY_EVENT_LOOP = new DefaultEventLoop(null, new NamedThreadFactory("NETTY_EVENT_LOOP", false));

    private static final AttributeKey<NettyRequestContext> REQUEST_CONTEXT_ATTRIBUTE_KEY = AttributeKey.valueOf("REQUEST_CONTEXT_ATTRIBUTE_KEY");

    @Override
    public String send(String msg) {
        Channel channel = channelFuture.get().channel();
        channelFuture.remove();
        ProgressivePromise<String> promise = NETTY_EVENT_LOOP.newProgressivePromise();
        NettyRequestContext requestContext = new NettyRequestContext(UUID.fastUUID().toString(), promise);
        channel.attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).set(requestContext);
        ChannelFuture future = channel.writeAndFlush(msg);
        try {
            return promise.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("处理中断");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("处理异常", e);
        } catch (TimeoutException e) {
            logger.error("获取响应超时, 超时时间：{}ms", this.timeout);
        } finally {
            future.channel().close();
        }
        throw new RuntimeException("获取响应异常");
    }

    @Override
    public synchronized void connect(Properties properties) {
        String serverIp = properties.getProperty("remoteIp");
        int serverPort = Integer.parseInt(properties.getProperty("remotePort"));
        int msgLengthSize = Integer.parseInt(properties.getProperty("msgLengthSize"));
        String msgEncode = properties.getProperty("msgEncode");
        String clientAppName = properties.getProperty("clientAppName");
        String soLingerEnable = properties.getProperty("soLingerEnable");
        this.timeout = Integer.parseInt(properties.getProperty("timeout"));
        try {
            Bootstrap option = new Bootstrap()
                    .group(loopGroup)
                    .channel(NioSocketChannel.class)
//                    .option(ChannelOption.SO_REUSEADDR, true)  //快速复用端口，默认关闭
                    .option(ChannelOption.TCP_NODELAY, true);
            if ("true".equals(soLingerEnable)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("开启SO_LINGER");
                }
                option.option(ChannelOption.SO_LINGER, 0);
            }
            Bootstrap bootstrap = option
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            if ("8583".equals(clientAppName)) {
                                ch.pipeline().addLast(new Message8583Encoder())
                                        .addLast(new Message8583Decoder());
                            } else {
                                ch.pipeline().addLast(new DemuxingMessageDecoder(msgLengthSize, msgEncode))
                                        .addLast(new DemuxingMessageEncoder(msgLengthSize, msgEncode));
                            }
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    NettyRequestContext requestContext = ctx.channel().attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).get();
                                    requestContext.getRespPromise().setSuccess((String) msg);
                                }

                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                    ctx.channel().close();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                    Channel channel = ctx.channel();
                                    if (channel.isActive()) {
                                        ctx.close();
                                    }
                                }
                            });
                        }
                    });
            channelFuture.set(bootstrap.connect(serverIp, serverPort).sync());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
