package cn.lezoo.doux.infrastructure.socket.client.netty.codec;

import cn.hutool.core.codec.BCD;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 10:26
 */
public class Message8583Encoder extends MessageToByteEncoder {
    private static final Logger logger = LoggerFactory.getLogger(Message8583Decoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (StringUtils.isEmpty(msg.toString())) {
            if (logger.isDebugEnabled()) {
                logger.debug("消息为空，直接返回");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("编码前消息：content[" + msg.toString() + "]");
        }
        byte[] body = BCD.strToBcd((String) msg);
        byteBuf.writeBytes(body);
    }
}
