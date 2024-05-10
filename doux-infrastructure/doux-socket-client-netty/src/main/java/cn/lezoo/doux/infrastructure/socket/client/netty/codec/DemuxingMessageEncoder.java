package cn.lezoo.doux.infrastructure.socket.client.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 9:40
 */
public class DemuxingMessageEncoder extends MessageToByteEncoder<String> {
    private static final Logger logger = LoggerFactory.getLogger(DemuxingMessageEncoder.class);

    private Integer msgLengthSize;
    private String msgEncode;

    public DemuxingMessageEncoder() {
        this.msgLengthSize = 0;
        this.msgEncode = "UTF-8";
    }

    public DemuxingMessageEncoder(Integer msgLengthSize, String msgEncode) {
        this.msgLengthSize = msgLengthSize;
        this.msgEncode = msgEncode;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, ByteBuf byteBuf) throws Exception {
        if (StringUtils.isEmpty(msg)) {
            if (this.getMsgLengthSize() != 0) {
                byteBuf.writeBytes(String.format("%0" + this.getMsgLengthSize() + "d", 0).getBytes());
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("编码前消息：字符串length =  " + msg.length() + ", content[" + msg + "]");
        }
        byte[] body = msg.getBytes(this.getMsgEncode());
        int bodyLen = 0;
        if (this.getMsgLengthSize() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("对消息长度和消息进行编码发送");
            }
            bodyLen = body.length;
            byteBuf.writeBytes(String.format("%0" + this.getMsgLengthSize() + "d", bodyLen).getBytes());
            byteBuf.writeBytes(body);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("对消息进行编码发送");
            }
            byteBuf.writeBytes(body);
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
