package com.allinfinance.dev.connection.scaffold.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author qipeng
 * @date 2022/9/15 9:30
 * @desc
 */
public class DefaultDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\">\n" +
                "    <SERVICE_HEADER>\n" +
                "        <SERV_RESPONSE>\n" +
                "            <STATUS>S</STATUS>\n" +
                "            <CODE>SSSS</CODE>\n" +
                "            <DESC>交易成功</DESC>\n" +
                "        </SERV_RESPONSE>\n" +
                "    </SERVICE_HEADER>\n" +
                "</SERVICE>";
        int length = response.getBytes(StandardCharsets.UTF_8).length;

        ctx.writeAndFlush(String.format("%06d", length) + response);
    }
}
