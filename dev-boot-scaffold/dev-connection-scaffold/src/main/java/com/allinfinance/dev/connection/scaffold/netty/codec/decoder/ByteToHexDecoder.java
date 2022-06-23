package com.allinfinance.dev.connection.scaffold.netty.codec.decoder;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/6/16 10:23
 * @description
 */
public class ByteToHexDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // TODO: 2022/6/16 怎么解析要再研究下
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        out.add(HexUtil.encodeHexStr(bytes));
    }


}
