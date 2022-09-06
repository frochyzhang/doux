package com.allinfinance.dev.common.socket.codec;

import com.allinfinance.dev.core.util.convert.common.ConvertUtils;
import com.allinfinance.dev.core.util.convert.simple8583.util.EncodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:26
 */
public class Message8583Decoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(com.allinfinance.dev.core.util.socket.codec.Message8583Decoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() != 0){
            if (byteBuf.readableBytes() >= 4){
                byteBuf.markReaderIndex();
                byte[] bLen = new byte[4];
                byteBuf.readBytes(bLen, 0, 4);
                int len = 0;
                try {
                    len = Integer.parseInt(new String(bLen));
                }catch (NumberFormatException ex){
                    logger.debug("报文长度含有非数字内容，关闭连接:  " + bLen);
                    channelHandlerContext.channel().closeFuture();
                }
                if (len == 0) {
                    return;
                }
                if (byteBuf.readableBytes() < len) {
                    byteBuf.resetReaderIndex();
                    return;
                }
                byte[] bBody = new byte[len];
                byteBuf.readBytes(bBody);
                list.add(EncodeUtil.hex(bBody));
                channelHandlerContext.channel().closeFuture();
            }else {
                logger.debug("报文长度未到齐:  " + byteBuf.readableBytes());
            }
        }
    }
}
