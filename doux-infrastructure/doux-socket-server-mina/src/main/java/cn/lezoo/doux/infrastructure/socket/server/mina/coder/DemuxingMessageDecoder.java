package cn.lezoo.doux.infrastructure.socket.server.mina.coder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
public class DemuxingMessageDecoder implements MessageDecoder {
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
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

        if (this.getMsgLengthSize() != 0) {
            if (in.remaining() < this.getMsgLengthSize()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("报文长度未到齐:  " + in.remaining());
                }
                return MessageDecoderResult.NEED_DATA;
            }

            byte[] temp = new byte[this.getMsgLengthSize()];
            in.get(temp, 0, this.getMsgLengthSize());
            int len = 0;
            try {
                len = Integer.parseInt(new String(temp));
            } catch (NumberFormatException ex) {
                // 长度头异常时会引发粘包问题，直接丢弃当前连接，等待新建连接
                if (logger.isDebugEnabled()) {
                    logger.debug("报文长度含有非数字内容，关闭连接:  {}", temp);
                }
                session.closeNow();
            }

            logger.info("剩余报文长度:{}", in.remaining());
            if (in.remaining() < len) {
                logger.error("报文数据未到齐: " + in.remaining() + ":" + len);
                return MessageDecoderResult.NEED_DATA;
            }
        }

        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        int len = 0;
        if (this.getMsgLengthSize() == 0) {
            // 如果长度域长度为0，直接读取当前缓冲区数据并抛弃后返回
            int blen = in.remaining();
            byte[] bBody = new byte[blen];
            in.get(bBody);
            out.write(new String(bBody, msgEncode));
            return MessageDecoderResult.OK;
        }

        if (this.getMsgLengthSize() != 0) {
            // 长度域不为0时，读取长度域内容
            byte[] bLen = new byte[this.getMsgLengthSize()];
            in.get(bLen, 0, this.getMsgLengthSize());
            len = Integer.parseInt(new String(bLen));
        }

        byte[] bBody = new byte[len];
        in.get(bBody);

        String result = new String(bBody, msgEncode);
        if (logger.isDebugEnabled()) {
            logger.debug("解码完成：字符串length= {},  content[{}]", result.length(), result);
        }
        out.write(result);

        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) {
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
