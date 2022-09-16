package com.allinfinance.dev.connection.scaffold.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @author qipeng
 * @date 2022/9/15 9:41
 * @desc
 */
public class DefaultEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    }
}
