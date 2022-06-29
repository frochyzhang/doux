package com.allinfinance.dev.connection.scaffold.netty.handler;

import com.allinfinance.dev.connection.scaffold.netty.connection.NettyClientConnection;
import com.allinfinance.dev.connection.scaffold.netty.context.RequestContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qipeng
 * @date 2022/6/16 15:56
 * @description
 */
public class DefaultHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("新建连接...");
        String channelId = ctx.channel().id().asLongText();
        if (logger.isDebugEnabled()) {
            logger.debug("通道id：{}", channelId);
            logger.debug("连接服务端成功，远程地址：{}", ctx.channel().remoteAddress());
            logger.debug("连接服务端成功，本地地址：{}", ctx.channel().localAddress());
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestContext requestContext = ctx.channel().attr(NettyClientConnection.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).get();
        Promise<String> promise = requestContext.getResponsePromise();

        try {
            promise.setSuccess((String) msg);
        } catch (Exception e) {
            logger.error("{} 异常:", ctx.channel().attr(NettyClientConnection.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).get().getRequestId(), e);
        }
    }
}
