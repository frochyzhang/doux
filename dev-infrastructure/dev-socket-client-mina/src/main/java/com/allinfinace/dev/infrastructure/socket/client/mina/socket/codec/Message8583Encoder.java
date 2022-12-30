package com.allinfinace.dev.infrastructure.socket.client.mina.socket.codec;

import com.allinfinance.dev.common.util.convert.simple8583.util.EncodeUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message8583Encoder
 *
 * @author hongmr
 * @date 2017/6/1
 */
public class Message8583Encoder implements MessageEncoder {
    private static final Logger logger = LoggerFactory.getLogger(Message8583Encoder.class);

    @Override
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
        logger.debug("编码前消息：content["
                + message + "]");
        out.write(IoBuffer.wrap(EncodeUtil.bcd((String) message)));
//        out.flush();
    }
}
