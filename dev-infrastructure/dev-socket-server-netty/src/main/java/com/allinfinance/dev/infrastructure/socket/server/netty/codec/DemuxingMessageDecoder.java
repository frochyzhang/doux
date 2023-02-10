package com.allinfinance.dev.infrastructure.socket.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 9:40
 */
public class DemuxingMessageDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(DemuxingMessageDecoder.class);

    private Integer msgLengthSize;
    private String msgEncode;

    public DemuxingMessageDecoder() {
        this.msgLengthSize = 0;
        this.msgEncode = "UTF-8";
    }

    public DemuxingMessageDecoder(Integer msgLengthSize, String msgEncode) {
        this.msgLengthSize = msgLengthSize;
        this.msgEncode = msgEncode;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() != 0 && this.getMsgLengthSize() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("开始对消息进行解码");
            }
            if (byteBuf.readableBytes() >= this.getMsgLengthSize()) {
                byteBuf.markReaderIndex();
                byte[] bLen = new byte[this.getMsgLengthSize()];
                byteBuf.readBytes(bLen, 0, this.getMsgLengthSize());
                int len = 0;
                try {
                    len = Integer.parseInt(new String(bLen));
                } catch (NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("报文长度含有非数字内容，关闭连接: {} ", bLen);
                    }
                    channelHandlerContext.channel().disconnect();
                    return;
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
                    logger.debug("解码结果,result={}", new String(bBody));
                }
                list.add(new String(bBody));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("报文长度未到齐:  " + byteBuf.readableBytes());
                }
            }
        }
    }

    public Integer getMsgLengthSize() {
        return msgLengthSize;
    }

    public void setMsgLengthSize(Integer msgLengthSize) {
        this.msgLengthSize = msgLengthSize;
    }

    public String getMsgEncode() {
        return msgEncode;
    }

    public void setMsgEncode(String msgEncode) {
        this.msgEncode = msgEncode;
    }
}
