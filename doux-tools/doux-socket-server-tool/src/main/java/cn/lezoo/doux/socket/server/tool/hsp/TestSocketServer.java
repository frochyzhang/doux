package cn.lezoo.doux.socket.server.tool.hsp;

import cn.hutool.core.net.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class TestSocketServer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(TestSocketServer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        int port = 6666;
        String name = "hsp-test";
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)   // 关闭nagle算法,其受TCP延迟确认影响, 会导致相继两次向连接发送请求包, 读数据时会有一个最多达500毫秒的延时
//                .childOption(ChannelOption.SO_LINGER, 0)  //关闭socket的延时时间，默认关闭
//                .childOption(ChannelOption.SO_KEEPALIVE, true)    //通过发送数据包方式检测socket连接有效性
//                .childOption(ChannelOption.SO_REUSEADDR, true)    //快速复用端口，默认关闭
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2))
                                .addLast(new LengthFieldPrepender(2))
                                .addLast(new ByteToHexDecoder())
                                .addLast(new HexToByteEncoder())
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    private final Logger logger = LoggerFactory.getLogger(TestSocketServer.class);

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        String reqMsg = (String) msg;
                                        logger.info("服务端收到请求消息:{}", reqMsg);
                                        String responseXml = "00aabb";
                                        ctx.writeAndFlush(responseXml);
                                    }
                                });
                    }
                });
        try {
            serverBootstrap.bind(port).sync();
            if (NetUtil.isUsableLocalPort(port)) {
                logger.error("[ {} ] 服务端口：{}未处于监听状态，请检查", name, port);
                throw new RuntimeException("Netty服务端口未处于监听状态，请检查");
            } else {
                logger.info("服务端启动成功");
            }
        } catch (InterruptedException e) {
            logger.error("[ {} ] 服务启动失败!", name, e);
            Thread.currentThread().interrupt();
        }
    }
}
