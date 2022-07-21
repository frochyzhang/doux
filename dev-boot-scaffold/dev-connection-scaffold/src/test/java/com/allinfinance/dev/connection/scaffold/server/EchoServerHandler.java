package com.allinfinance.dev.connection.scaffold.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/28
 **/
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接建立成功：" + ctx.channel().id().asLongText());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String request = (String) msg;
        try {
            if (request.startsWith("d30a") && request.length() == 3651 * 2) {
                logger.info("摘要请求内容:{}", request);
                ctx.channel().writeAndFlush("4100205b2512bc9fec384b55349ab4af07d65e50af7aa2a3c066a0b9b53dee0b944269");
            } else if (request.startsWith("d306") && request.length() == 332) {
                logger.info("签名请求内容：{}", request);
                ctx.channel().writeAndFlush("41f9a73559fe88063294208eaa39094a2c6025badbb77eb25d8dc4b536fb5f0a71066c2ff68cf6115db1e4759353f83bb7a4ecbed72041eb50fd87fb98ba3b47d4");
            } else if (request.startsWith("d307") && request.length() == 358) {
                logger.info("验签请求内容：{}", request);
                ctx.channel().writeAndFlush("41");
            } else {
                logger.info("异常内容：{}", request);
                ctx.channel().writeAndFlush("4568");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭连接：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
