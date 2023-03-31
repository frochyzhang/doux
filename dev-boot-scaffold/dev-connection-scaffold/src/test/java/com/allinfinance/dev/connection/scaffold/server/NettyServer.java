package com.allinfinance.dev.connection.scaffold.server;

import com.allinfinance.dev.connection.scaffold.netty.codec.decoder.ByteToHexDecoder;
import com.allinfinance.dev.connection.scaffold.netty.codec.encoder.HexToByteEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/28
 **/
public class NettyServer {

    private int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(100);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //更换对应的handler
                            ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(1024 * 64));

                            ch.pipeline()
//                                    .addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 2, 0, 2))
//                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new ByteToHexDecoder())
                                    .addLast(new HexToByteEncoder())
                                    .addLast(new EchoServerHandler());
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

    public static void main(String[] args) throws Exception {
        int port = 9001;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new NettyServer(port).run();
        System.out.println(Integer.MAX_VALUE);
    }
}
