package com.allinfinance.dev.infrastructure.socket.server.netty;

import com.allinfinance.dev.framework.socket.server.driver.SocketServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
public class NettySocketServer implements SocketServer {

    private Logger logger = LoggerFactory.getLogger(NettySocketServer.class);

    //private static final ConcurrentHashMap<Integer, IoAcceptor> IO_ACCEPTOR_MAP = new ConcurrentHashMap<>();
    @Override
    public void start(Properties properties) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String decoderClassName = properties.getProperty("decoderClassName");
        String decodeCharset = properties.getProperty("decodeCharset");
        String encoderClassName = properties.getProperty("encoderClassName");
        String encodeMsgLength = properties.getProperty("encodeMsgLength");
        String encodeCharset = properties.getProperty("encodeCharset");
        String handlerClassName = properties.getProperty("handlerClassName");
        String name = properties.getProperty("name");
        int port = Integer.parseInt(properties.getProperty("port"));
        int decodeMsgLength = Integer.parseInt(properties.getProperty("decodeMsgLength"));

        /**
         * 解码器配置
         */
        ByteToMessageDecoder messageDecoder = (ByteToMessageDecoder) Class.forName(decoderClassName)
                .getConstructor(Integer.class, String.class)
                .newInstance(decodeMsgLength, decodeCharset);
        /**
         * 编码器配置
         */
        MessageToByteEncoder messageEncoder = (MessageToByteEncoder) Class.forName(encoderClassName)
                .getConstructor(Integer.class, String.class)
                .newInstance(encodeMsgLength, encodeCharset);
        /**
         * 处理器配置
         */
        ChannelInboundHandlerAdapter handler = (ChannelInboundHandlerAdapter) Class.forName(handlerClassName).getConstructor(String.class)
                .newInstance(name);
        /**
         * 配置服务端并启动
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(messageDecoder)
                                .addLast(messageEncoder)
                                .addLast(handler);
                    }
                });
        logger.info("Netty服务端初始化完成");
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

    @Override
    public void close(Integer port) {

    }
}
