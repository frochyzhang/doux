package com.allinfinance.dev.common.socket.server.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.allinfinance.dev.common.socket.server.Bean.NettySocketBean;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/08 10:00
 */
@Configuration
public class NettyShortSwitchServer implements DisposableBean, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(NettyShortSwitchServer.class);

    @Autowired
    private List<NettySocketBean> nettySocketBeans;

    private static ThreadPoolExecutor threadPoolExecutor;

    private void initNettyServer(NettySocketBean nettySocketBean) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        /**
         * 解码器配置
         */
        ByteToMessageDecoder messageDecoder = (ByteToMessageDecoder) Class.forName(nettySocketBean.getDecoderClassName())
                .getConstructor(Integer.class, String.class)
                .newInstance(nettySocketBean.getDecodeMsgLength(), nettySocketBean.getDecodeCharset());
        /**
         * 编码器配置
         */
        MessageToByteEncoder messageEncoder = (MessageToByteEncoder) Class.forName(nettySocketBean.getEncoderClassName())
                .getConstructor(Integer.class, String.class)
                .newInstance(nettySocketBean.getEncodeMsgLength(), nettySocketBean.getEncodeCharset());
        /**
         * 处理器配置
         */
        ChannelInboundHandlerAdapter handler = (ChannelInboundHandlerAdapter) Class.forName(nettySocketBean.getHandlerClassName()).getConstructor(String.class)
                .newInstance(nettySocketBean.getName());
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
            ChannelFuture future = serverBootstrap.bind(nettySocketBean.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("[ {}] 启动服务失败! 参数为{}", nettySocketBean.getName(), nettySocketBean, e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public void destroy() throws Exception {
        threadPoolExecutor.shutdown();
        logger.info("socket NettyServer thread pool is shutting down!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(nettySocketBeans.size());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("netty-server-pool-")
                .setDaemon(true)
                .build();
        threadPoolExecutor = new ThreadPoolExecutor(nettySocketBeans.size(), nettySocketBeans.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        logger.info("正在启动应用，请稍后!");
        nettySocketBeans.forEach(nettySocketBean -> threadPoolExecutor.submit(() -> {
            try {
                initNettyServer(nettySocketBean);
            } catch (Exception e) {
                logger.error("[ {}] 启动服务失败! 参数为{}", nettySocketBean.getName(), nettySocketBean, e);
                System.exit(0);
            }
            countDownLatch.countDown();
            logger.info("{}-Server Thread start!", nettySocketBean.getName());
        }));
    }
}
