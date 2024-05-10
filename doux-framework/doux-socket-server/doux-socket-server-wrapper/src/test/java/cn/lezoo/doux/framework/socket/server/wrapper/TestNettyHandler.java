package cn.lezoo.doux.framework.socket.server.wrapper;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date
 */
@ChannelHandler.Sharable
public class TestNettyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TestNettyHandler.class);

    private String appName;

    public TestNettyHandler(String appName) {
        this.appName = appName;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String reqMsg = (String) msg;
        logger.info("收到请求消息:{}", reqMsg);
        String respMsg = "AppProcessFactory.processed(this.getAppName(), reqMsg)";
        logger.info("返回应答消息:{}|{}", respMsg, respMsg.getBytes(StandardCharsets.UTF_8).length);
        ctx.writeAndFlush(respMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
