package com.allinfinance.dev.common.socket.codec;

import com.allinfinance.dev.core.util.convert.simple8583.util.EncodeUtil;
import com.allinfinance.dev.core.util.socket.codec.Message8583Decoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:26
 */
public class Message8583Encoder  extends MessageToByteEncoder {
    private final Logger logger = LoggerFactory.getLogger(Message8583Decoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (StringUtils.isEmpty(msg.toString())) {
            byteBuf.writeBytes("0000".getBytes());
            return;
        }
        logger.debug("编码前消息：content[" + msg + "]");
        byte[] body = EncodeUtil.bcd((String) msg);
        int bodyLen = 0;
        bodyLen = body.length;
        byteBuf.writeBytes(String.format("%0" + 4 + "d", bodyLen).getBytes());
        byteBuf.writeBytes(body);
    }
}
