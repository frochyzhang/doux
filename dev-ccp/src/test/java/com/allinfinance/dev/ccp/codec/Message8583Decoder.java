package com.allinfinance.dev.ccp.codec;

import cn.hutool.core.convert.Convert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 16:26
 */
public class Message8583Decoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(Message8583Decoder.class);

    public Message8583Decoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("开始对消息进行解码");
            }
            if (byteBuf.readableBytes() >= 4) {
                byteBuf.markReaderIndex();
                byte[] bLen = new byte[4];
                byteBuf.readBytes(bLen, 0, 4);
                int len = 0;
                try {
                    len = Integer.parseInt(new String(bLen));
                } catch (NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("报文长度含有非数字内容，关闭连接:  " + Arrays.toString(bLen));
                    }
                    channelHandlerContext.channel().closeFuture();
                }
                if (byteBuf.readableBytes() < len) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("长度与消息真实长度不符，重置读");
                    }
                    byteBuf.resetReaderIndex();
                    return;
                }
                byte[] bBody = new byte[len];
                byteBuf.readBytes(bBody);
                if (logger.isDebugEnabled()) {
                    logger.debug("解码结果,result={}", Convert.toHex(bBody));
                }
                list.add(Convert.toHex(bBody));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("报文长度未到齐:  " + byteBuf.readableBytes());
                }
            }
        }
    }
}
