package com.allinfinance.dev.infrastructure.conn.netty.codec;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.infrastructure.conn.netty.pojo.HspCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/14
 **/
public class HspCommandEncoder extends MessageToByteEncoder<HspCommand> {
    private static final Logger logger = LoggerFactory.getLogger(HspCommandEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, HspCommand hspCommand, ByteBuf out) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("编码前消息：{}", hspCommand);
        }
        out.writeBytes(HexUtil.decodeHex(hspCommand.getLengthField()));
        out.writeBytes(HexUtil.decodeHex(hspCommand.getMessageHeader()));
        out.writeBytes(HexUtil.decodeHex(hspCommand.getCode()));
        out.writeBytes(HexUtil.decodeHex(hspCommand.getContent()));
        // TODO: 2022/7/14 需要判空？
        out.writeBytes(HexUtil.decodeHex(hspCommand.getMessageTail()));
    }
}
