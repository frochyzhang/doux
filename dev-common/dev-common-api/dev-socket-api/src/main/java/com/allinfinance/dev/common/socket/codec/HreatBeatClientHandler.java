package com.allinfinance.dev.common.socket.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/07 14:24
 */
public class HreatBeatClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HreatBeatClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf)msg;
        String s = byteBuf.toString(StandardCharsets.UTF_8);
        if ("pong".equals(s)){
            logger.info("接收服务端发送的心跳检测返回消息：{}",s);
            System.out.println(s);
            byteBuf.release();
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE:
                    System.out.println("读空闲");
                    ping(ctx);
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲");
                    ping(ctx);
                    break;
                case ALL_IDLE:
                    System.out.println("读写空闲");
                    ping(ctx);
                    break;
            }
        }else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    private void ping(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("ping".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
