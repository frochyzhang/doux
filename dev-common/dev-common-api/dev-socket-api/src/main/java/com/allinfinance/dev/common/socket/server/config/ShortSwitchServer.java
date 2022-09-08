package com.allinfinance.dev.common.socket.server.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.allinfinance.dev.common.socket.server.Bean.NettySocketBean;
import com.allinfinance.dev.common.socket.server.HreatBeatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.mina.core.service.IoAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/08 10:00
 */
@Configuration
public class ShortSwitchServer implements DisposableBean{

    private static final Logger logger = LoggerFactory.getLogger(ShortSwitchServer.class);

    @Autowired
    private List<NettySocketBean> socketBeans;

    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 根据SocketBeanLoader中解析配置文件获取到的配置信息，依次开启对应服务端口并监听
     */
    @Bean
    @Order
    public void init() {

        CountDownLatch countDownLatch = new CountDownLatch(socketBeans.size());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("socket-server-pool-")
                .setDaemon(true)
                .build();
        threadPoolExecutor = new ThreadPoolExecutor(socketBeans.size(), socketBeans.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        logger.info("正在启动应用，请稍后!");
        socketBeans.forEach(nettySocketBean -> {
            threadPoolExecutor.submit(() -> {
                try {
                    initNettyServer(nettySocketBean);
                } catch (Exception e) {
                    logger.error("[ {}] 启动服务失败! 参数为{}", nettySocketBean.getName(), nettySocketBean, e);
                    System.exit(0);
                }

                countDownLatch.countDown();
                logger.info("{}-Server Thread start!", nettySocketBean.getName());
            });
        });
    }

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
        ChannelInboundHandlerAdapter Handler = (ChannelInboundHandlerAdapter) Class.forName(nettySocketBean.getHandlerClassName()).getConstructor(String.class)
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
                        if (nettySocketBean.getKeepAlive()){
                            logger.info("开启服务端保持连接!");
                            pipeline.addLast(new IdleStateHandler(5, 10, 0, TimeUnit.SECONDS))
                                    .addLast(new HreatBeatServerHandler());
                        }
                        pipeline.addLast(messageDecoder)
                                .addLast(messageEncoder)
                                .addLast(Handler);


                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ctx.writeAndFlush("收到消息！！");
                            }
                        });
                    }
                });
        logger.debug("服务端初始化完成");
        try {
            ChannelFuture future = serverBootstrap.bind(nettySocketBean.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public void destroy() throws Exception {

    }
}
