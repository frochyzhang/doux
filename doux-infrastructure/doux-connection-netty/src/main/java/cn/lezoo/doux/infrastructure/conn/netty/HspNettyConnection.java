package cn.lezoo.doux.infrastructure.conn.netty;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.extension.annotation.Extension;
import cn.lezoo.doux.infrastructure.conn.netty.codec.ByteToHexDecoder;
import cn.lezoo.doux.infrastructure.conn.netty.codec.HexToByteEncoder;
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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/12
 **/
@Extension("hsp")
public class HspNettyConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(HspNettyConnection.class);

    private static final EventLoopGroup loopGroup = new NioEventLoopGroup(16,
            new NamedThreadFactory("hsp-netty-", false));

    private ChannelFuture channelFuture;

    public static final ConcurrentHashMap<Long, Promise<String>> PROMISE_MAP = new ConcurrentHashMap<>();

    private static final DefaultEventLoop nettyEventLoop = new DefaultEventLoop(null, new NamedThreadFactory("NETTY_EVENT_LOOP", false));

    private int timeout;

    private final Bootstrap bootstrap = new Bootstrap()
            .group(loopGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true);

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    @Override
    public void close() {
        Channel channel = channelFuture.channel();
        if (channel != null && channel.isActive()) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("关闭连接时中断", e);
            }
        }
    }

    @Override
    public boolean isClosed() {
        return channelFuture.channel() == null || !channelFuture.channel().isActive();
    }

    @Override
    public String send(String msg) {
        Channel channel = channelFuture.channel();

        Promise<String> promise = nettyEventLoop.newPromise();

        long requestId = ATOMIC_LONG.addAndGet(1);
        msg = String.format("%016x", requestId) + msg;
        PROMISE_MAP.put(requestId, promise);
        channel.writeAndFlush(msg);
        try {
            return promise.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("处理中断", e);
        } catch (ExecutionException e) {
            logger.error("处理异常", e);
        } catch (TimeoutException e) {
            logger.error("获取响应超时, 超时时间：{}ms", this.timeout);
        } finally {
            PROMISE_MAP.remove(requestId);
        }
        throw new RuntimeException("获取响应异常");
    }

    @Override
    public void connect(Properties properties) {
        String serverIp = properties.getProperty("serverIp");
        int serverPort = Integer.parseInt(properties.getProperty("serverPort"));
        int lengthField = Integer.parseInt(properties.getProperty("lengthField"));
        int bufferSize = Integer.parseInt(properties.getProperty("bufferSize"));
        this.timeout = Integer.parseInt(properties.getProperty("defaultNetworkTimeout"));
        int connectTimeout = Integer.parseInt(properties.getProperty("connectTimeout"));

        try {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
            channelFuture = bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    if (lengthField != 0) {
                        // 长度域不为0时才注册长度解析的decoder和encoder
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(bufferSize, 0, lengthField, 0, 2))
                                .addLast(new LengthFieldPrepender(lengthField));
                    }
                    ch.pipeline()
                            .addLast(new ByteToHexDecoder())
                            .addLast(new HexToByteEncoder())
                            .addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ctx.fireChannelReadComplete();
                                }
                            });
                }
            }).connect(serverIp, serverPort).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isValid(int validationTimeoutSeconds, String connectionTestQuery) {
        return send(connectionTestQuery) != null;
    }
}
