package cn.lezoo.doux.socket.server.tool.hsp;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qipeng
 * @date 2022/6/16 10:10
 * @description 将字符串转成hex
 */
public class HexToByteEncoder extends MessageToByteEncoder<String> {
    private static final Logger logger = LoggerFactory.getLogger(HexToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
        // 传过来的msg就是十六进制的字符串
        // byte[] bytes = msg.getBytes();
        Long requestId = RequestIdContext.getRequestId();
        msg = String.format("%016x", requestId) + msg;
        byte[] bytes = HexUtil.decodeHex(msg);
        if (logger.isDebugEnabled()) {
            logger.debug("编码后的请求内容：{}, 连接id: {}", bytes, ctx.channel().id().asShortText());
        }
        out.writeBytes(bytes);
    }
}
