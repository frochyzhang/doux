package com.allinfinance.dev.connection.scaffold.netty.codec.encoder;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author qipeng
 * @date 2022/6/16 10:10
 * @description 将字符串转成hex
 */
public class HexToByteEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        // 传过来的msg就是十六进制的字符串
        out.writeBytes(HexUtil.decodeHex(msg));
    }
}
