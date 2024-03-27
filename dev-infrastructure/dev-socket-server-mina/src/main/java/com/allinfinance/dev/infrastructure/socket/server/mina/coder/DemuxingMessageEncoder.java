package com.allinfinance.dev.infrastructure.socket.server.mina.coder;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
public class DemuxingMessageEncoder implements MessageEncoder<String> {
    private static final Logger logger = LoggerFactory.getLogger(DemuxingMessageEncoder.class);

    private Integer msgLengthSize;
    private String msgEncode;

    DemuxingMessageEncoder() {
        this.msgLengthSize = 0;
        this.msgEncode = "UTF-8";
    }

    public DemuxingMessageEncoder(Integer msgLengthSize, String msgEncode) {
        this.msgLengthSize = msgLengthSize;
        this.msgEncode = msgEncode;
    }

    @Override
    public void encode(IoSession session, String message,
                       ProtocolEncoderOutput out) throws Exception {
        if (StringUtils.isEmpty(message)) {
            if (this.getMsgLengthSize() != 0) {
                IoBuffer buf = IoBuffer.allocate(this.getMsgLengthSize());
                buf.put(String.format("%0" + this.getMsgLengthSize() + "d", 0).getBytes());
                buf.flip();
                out.write(buf);
            }
            return;
        }
        byte[] body = message.getBytes(this.getMsgEncode());
        if (logger.isDebugEnabled()) {
            logger.debug("编码前消息：字符串字节长度 = {}, content=[{}]", body.length, message);
        }
        int bodyLen;
        bodyLen = body.length;
        IoBuffer buf = IoBuffer.allocate(bodyLen + this.getMsgLengthSize()).setAutoExpand(true);
        if (this.getMsgLengthSize() != 0) {
            buf.put(String.format("%0" + this.getMsgLengthSize() + "d", bodyLen).getBytes());
        }
        buf.put(body);
        buf.flip();
        session.write(buf);
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
