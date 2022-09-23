package com.allinfinace.dev.infrastrustructure.socket.client.netty.codec;

import com.allinfinance.dev.common.util.convert.EncodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 16:26
 */
public class Message8583Decoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(Message8583Decoder.class);

    public Message8583Decoder(){
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() != 0){
            logger.debug("开始对消息进行解码");
            if (byteBuf.readableBytes() >= 4){
                byteBuf.markReaderIndex();
                byte[] bLen = new byte[4];
                byteBuf.readBytes(bLen, 0, 4);
                int len = 0;
                try {
                    len = Integer.parseInt(new String(bLen));
                }catch (NumberFormatException ex){
                    logger.debug("报文长度含有非数字内容，关闭连接:  " + Arrays.toString(bLen));
                    channelHandlerContext.channel().closeFuture();
                }
                if (byteBuf.readableBytes() < len) {
                    logger.debug("长度与消息真实长度不符，重置读");
                    byteBuf.resetReaderIndex();
                    return;
                }
                byte[] bBody = new byte[len];
                byteBuf.readBytes(bBody);
                logger.debug("解码结果,result={}",EncodeUtil.hex(bBody));
                list.add(EncodeUtil.hex(bBody));
            }else {
                logger.debug("报文长度未到齐:  " + byteBuf.readableBytes());
            }
        }
    }
}
