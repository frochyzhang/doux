package com.allinfinace.dev.infrastructure.socket.client.mina.socket.codec;

import com.allinfinance.dev.common.util.convert.simple8583.util.EncodeUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message8583Decoder
 *
 * @author hongmr
 * @date 2017/6/1
 */
public class Message8583Decoder implements MessageDecoder {
    private Logger logger = LoggerFactory.getLogger(Message8583Decoder.class);

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

        if (in.remaining() < 4) {
            logger.debug("报文长度未到齐:  " + in.remaining());
            return MessageDecoderResult.NEED_DATA;
        }

        byte[] temp = new byte[4];
        in.get(temp, 0, 4);
        int len = 0;
        try {
            len = Integer.parseInt(new String(temp));
        } catch (NumberFormatException ex) {
            // 长度头异常时会引发粘包问题，直接丢弃当前连接，等待新建连接
            logger.debug("报文长度含有非数字内容，关闭连接:  " + temp);
            session.closeNow();
        }

        if (in.remaining() < len) {
            logger.debug("报文数据未到齐: " + in.remaining() + ":" + len);
            return MessageDecoderResult.NEED_DATA;
        }

        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        byte[] bLen = new byte[4];
        in.get(bLen, 0, 4);
        int len = Integer.parseInt(new String(bLen));
        logger.debug("解码消息：字节length = " + len + ", content[" + in.toString()
                + "]");
        if (len == 0) {
            out.write("0000");
            return MessageDecoderResult.OK;
        }
        int blen = in.remaining();
        byte[] bBody = new byte[blen];
        in.get(bBody);

        out.write(EncodeUtil.hex(bBody));

        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
