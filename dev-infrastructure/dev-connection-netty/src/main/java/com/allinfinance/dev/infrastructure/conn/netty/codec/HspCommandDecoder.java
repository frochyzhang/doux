package com.allinfinance.dev.infrastructure.conn.netty.codec;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.infrastructure.conn.netty.pojo.HspCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/14
 **/
public class HspCommandDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ByteToMessageDecoder.class);

    /**
     * 最小长度，这里是长度域，默认2字节
     */
    private int lessLength = 2;
    /**
     * 消息头长度，默认8字节
     */
    private int messageHeaderLength = 8;
    /**
     * 消息尾长度，默认不送
     */
    private int messageTailLength = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO: 2022/7/14 可以写得更优雅，但是场景已经确定，没太大必要
        if (in.readableBytes() < lessLength) {
            return;
        }
        in.markReaderIndex();
        // 长度域为2，直接转成short
        short lengthFiled = in.readShort();
        in.resetReaderIndex();
        long messageHeader;
        if (messageHeaderLength == 8) {
            messageHeader = in.readLong();
        }
        HspCommand hspCommand = new HspCommand();
        int remainedLength = lengthFiled - messageHeaderLength;
        if (in.readableBytes() >= remainedLength && remainedLength > 0) {
            //读取消息头后面的数据
            in.markReaderIndex();
            byte[] responseBytes = new byte[remainedLength];
            in.readBytes(responseBytes);
            in.resetReaderIndex();
            // 先读取应答码
            hspCommand.setCode(HexUtil.encodeHexStr(Arrays.copyOfRange(responseBytes, 0, 1)));
            if (messageTailLength == 0) {
                // 消息尾长度为0，直接读取内容
                hspCommand.setContent(HexUtil.encodeHexStr(Arrays.copyOfRange(responseBytes, 1, responseBytes.length - 1)));
            } else {
                hspCommand.setContent(HexUtil.encodeHexStr(Arrays.copyOfRange(responseBytes, 1, responseBytes.length - messageTailLength)));
                hspCommand.setMessageTail(HexUtil.encodeHexStr(Arrays.copyOfRange(responseBytes,
                        responseBytes.length - messageTailLength, responseBytes.length - 1)));
            }
            out.add(hspCommand);
        } else {
            in.resetReaderIndex();
            return;
        }
    }

    public int getLessLength() {
        return lessLength;
    }

    public void setLessLength(int lessLength) {
        this.lessLength = lessLength;
    }

    public int getMessageHeaderLength() {
        return messageHeaderLength;
    }

    public void setMessageHeaderLength(int messageHeaderLength) {
        this.messageHeaderLength = messageHeaderLength;
    }

    public int getMessageTailLength() {
        return messageTailLength;
    }

    public void setMessageTailLength(int messageTailLength) {
        this.messageTailLength = messageTailLength;
    }
}
