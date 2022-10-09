package com.allinfinance.dev.infrastructure.socket.server.netty;

import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.socket.server.driver.SocketServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        int decodeMsgLength = Integer.parseInt(properties.getProperty("decodeMsgLength"));
        int encodeMsgLength = Integer.parseInt(properties.getProperty("encodeMsgLength"));

        List<EventLoopGroup> nioEventLoopGroupList = new ArrayList<>();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_LINGER, 0)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
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
                                        .newInstance(name));
                    }
                });
        nioEventLoopGroupList.add(workerGroup);
        nioEventLoopGroupList.add(bossGroup);
        EVENT_LOOP_GROUP_MAP.putIfAbsent(port, nioEventLoopGroupList);
        if (logger.isDebugEnabled()) {
            logger.debug("{}Netty服务端初始化完成", name);
        }
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("[ {}] 启动服务失败! 参数为{}", name, properties, e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
