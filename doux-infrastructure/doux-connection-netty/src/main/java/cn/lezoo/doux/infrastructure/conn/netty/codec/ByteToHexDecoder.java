package cn.lezoo.doux.infrastructure.conn.netty.codec;

import cn.hutool.core.util.HexUtil;
import cn.lezoo.doux.infrastructure.conn.netty.HspNettyConnection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.Promise;
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
        Promise<String> stringPromise = HspNettyConnection.PROMISE_MAP.get(requestId);
        in.markReaderIndex();

        int size = in.readableBytes();
        byte[] bytes = new byte[size];
        in.readBytes(bytes);
        String receive = HexUtil.encodeHexStr(bytes);
        if (logger.isDebugEnabled()) {
            logger.debug("接收到的报文大小：{}字节，接收到的报文内容：{}", size, receive);
        }
        try {
            stringPromise.setSuccess(receive);
        } catch (IllegalStateException e) {
            logger.error("接收响应异常，requestId: {}", requestId, e);
        } catch (NullPointerException e) {
            logger.error("请求异常，requestId: {}", requestId);
        }
        out.add(receive);
    }
}
