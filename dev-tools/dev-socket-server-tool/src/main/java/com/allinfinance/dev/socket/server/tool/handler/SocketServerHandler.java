package com.allinfinance.dev.socket.server.tool.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2023/8/17 9:39
 */
public class SocketServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerHandler.class);

    private String appName;

    public SocketServerHandler(String appName) {
        this.appName = appName;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String reqMsg = (String) msg;
        LOGGER.info("服务端收到请求消息:{}", reqMsg);
        String responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SMS>\n" +
                "    <ORG_ID>000000000000</ORG_ID>\n" +
                "    <SMS_NO>000000043457</SMS_NO>\n" +
                "    <SMS_TYPE>00</SMS_TYPE>\n" +
                "    <TEL>17621997673</TEL>\n" +
                "    <CONTENT>您的验证码为189496</CONTENT>\n" +
                "    <REQUEST_TIME>20221021102955</REQUEST_TIME>\n" +
                "    <RESP_FLAG>00</RESP_FLAG>\n" +
                "    <RESP_DETAIL>发送成功</RESP_DETAIL>\n" +
                "</SMS>\n";
        ctx.writeAndFlush(responseXml);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
