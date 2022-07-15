package com.allinfinance.dev.infrastructure.conn.netty.handler;

import com.allinfinance.dev.infrastructure.conn.netty.pojo.HspCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/14
 **/
public class HspCommandHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HspCommandHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        HspCommand hspCommand = (HspCommand) msg;
        logger.debug("接收到消息：{}", hspCommand);
        ctx.close();
    }
}
