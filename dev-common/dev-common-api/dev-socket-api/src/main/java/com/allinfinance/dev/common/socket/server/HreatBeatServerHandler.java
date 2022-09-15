package com.allinfinance.dev.common.socket.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.CharSet;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/07 9:55
 *
 */
public class HreatBeatServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        count.set(0);
        ByteBuf byteBuf = (ByteBuf)msg;
        String s = byteBuf.toString(StandardCharsets.UTF_8);
        if ("ping".equals(s)){
            byteBuf.clear();
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
                    count.incrementAndGet();
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲");
                    break;
                case ALL_IDLE:
                    System.out.println("读写空闲");
                    break;
            }
            if (count.get()>3){
                System.out.println("3次没有读写时间，关闭连接");
                ctx.channel().close();
            }
        }else {
            ctx.fireUserEventTriggered(evt);
        }
    }
}

