package cn.lezoo.doux.socket.server.tool.hsp;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/6/16 10:23
 * @description
 */
public class ByteToHexDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ByteToHexDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        long requestId = in.readLong();
        RequestIdContext.setRequestId(requestId);
        in.markReaderIndex();

        int size = in.readableBytes();
        byte[] bytes = new byte[size];
        in.readBytes(bytes);
        // String receive = new String(bytes);
        String receive = HexUtil.encodeHexStr(bytes);
        if (logger.isDebugEnabled()) {
            logger.debug("接收到的报文大小：{}字节，接收到的报文内容：{}", size, receive);
        }
        out.add(receive);
    }
}
