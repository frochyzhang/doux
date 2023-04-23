package com.allinfinance.dev.infrastructure.socket.server.netty;

import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.socket.server.driver.SocketServer;
import com.allinfinance.dev.infrastructure.socket.server.netty.handler.IdleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
@Extension("netty")
public class NettySocketServer implements SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(NettySocketServer.class);

    private static final ConcurrentHashMap<Integer, List<EventLoopGroup>> EVENT_LOOP_GROUP_MAP = new ConcurrentHashMap<>();

    /**
     * 根据传入properties配置开启具体服务端口
     *
     * @param properties 服务端口配置参数
     */
    @Override
    public void start(Properties properties) {
        String name = properties.getProperty("name");
        int port = Integer.parseInt(properties.getProperty("port"));
        String encodeCharset = properties.getProperty("encodeCharset");
        String decodeCharset = properties.getProperty("decodeCharset");
        String encoderClassName = properties.getProperty("encoderClassName");
        String decoderClassName = properties.getProperty("decoderClassName");
        String handlerClassName = properties.getProperty("handlerClassName");
        int readerIdleTime = Integer.parseInt(properties.getProperty("readerIdleTime"));
        int writerIdleTime = Integer.parseInt(properties.getProperty("writerIdleTime"));
        int decodeMsgLength = Integer.parseInt(properties.getProperty("decodeMsgLength"));
        int encodeMsgLength = Integer.parseInt(properties.getProperty("encodeMsgLength"));

        List<EventLoopGroup> nioEventLoopGroupList = new ArrayList<>();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)   //关闭nagle算法,其受TCP延迟确认影响, 会导致相继两次向连接发送请求包, 读数据时会有一个最多达500毫秒的延时
//                .childOption(ChannelOption.SO_LINGER, 0)  //关闭socket的延时时间，默认关闭
//                .childOption(ChannelOption.SO_KEEPALIVE, true)    //通过发送数据包方式检测socket连接有效性
//                .childOption(ChannelOption.SO_REUSEADDR, true)    //快速复用端口，默认关闭
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast((ByteToMessageDecoder) Class.forName(decoderClassName)
                                .getConstructor(Integer.class, String.class)
                                .newInstance(decodeMsgLength, decodeCharset))
                                .addLast((MessageToByteEncoder) Class.forName(encoderClassName)
                                        .getConstructor(Integer.class, String.class)
                                        .newInstance(encodeMsgLength, encodeCharset))
                                .addLast((ChannelInboundHandlerAdapter) Class.forName(handlerClassName)
                                        .getConstructor(String.class)
                                        .newInstance(name))
                                .addLast(new IdleStateHandler(readerIdleTime, writerIdleTime, 0, TimeUnit.MILLISECONDS))
                                .addLast(new IdleHandler());
                    }
                });
        nioEventLoopGroupList.add(workerGroup);
        nioEventLoopGroupList.add(bossGroup);
        EVENT_LOOP_GROUP_MAP.putIfAbsent(port, nioEventLoopGroupList);
        if (logger.isDebugEnabled()) {
            logger.debug("{}-Netty服务端初始化完成", name);
        }
        try {
            serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            logger.error("[ {}] 启动服务失败! 参数为{}", name, properties, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 关闭服务端口
     *
     * @param port 关闭端口号
     */
    @Override
    public void close(Integer port) {
        logger.info("开始关闭端口：{}", port);
        Optional.ofNullable(EVENT_LOOP_GROUP_MAP.get(port))
                .ifPresent(eventLoopGroups -> {
                    for (EventLoopGroup eventLoopGroup : eventLoopGroups) {
                        eventLoopGroup.shutdownGracefully();
                    }
                    EVENT_LOOP_GROUP_MAP.remove(port);
                    logger.info("Netty socket server port = {} has been shutdown ...", port);
                });
    }
}
