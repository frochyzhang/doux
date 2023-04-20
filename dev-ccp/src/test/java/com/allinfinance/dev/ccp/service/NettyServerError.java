package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.ccp.service.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.ccp.service.codec.DemuxingMessageEncoder;
import com.allinfinance.dev.ccp.service.codec.DemuxingMessageEncoderError;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: 长度域直接拼n个0
 * @Author: qipeng
 * @Date: 2022/6/28
 **/
public class NettyServerError {

    private final int port;
    private final int msgLength;
    private final String charset;
    private final String responseSuffix;

    public NettyServerError(int port, int msgLength, String charset, String reponseSuffix) {
        this.port = port;
        this.msgLength = msgLength;
        this.charset = charset;
        this.responseSuffix = reponseSuffix;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DemuxingMessageDecoder(msgLength, charset))
                                    .addLast(new DemuxingMessageEncoderError(msgLength, charset))
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ctx.writeAndFlush(responseSuffix + msg);
                                        }

                                        @Override
                                        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                            ctx.channel().close();
                                        }
                                    });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 8)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            //bind()可以根据需要多次调用该方法（使用不同的绑定地址）
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
