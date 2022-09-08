package com.allinfinance.dev.common.socket.server;

import com.allinfinance.dev.common.socket.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.common.socket.codec.DemuxingMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/07 9:50
 */
public class ISocketServer {
    private static Logger logger = LoggerFactory.getLogger(ISocketServer.class);

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DemuxingMessageDecoder(4, "UTF-8"))
                                .addLast(new DemuxingMessageEncoder(4, "UTF-8"));
//                        pipeline.addLast(new IdleStateHandler(5, 10, 20, TimeUnit.SECONDS));
//                        pipeline.addLast(new HreatBeatServerHandler());
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
            ChannelFuture future = serverBootstrap.bind(4396).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}


