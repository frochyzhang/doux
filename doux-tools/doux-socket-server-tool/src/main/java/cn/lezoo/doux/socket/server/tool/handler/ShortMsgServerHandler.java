package cn.lezoo.doux.socket.server.tool.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huanghf
 * @date 2024/10/11 14:57
 */
@Slf4j
@RequiredArgsConstructor
public class ShortMsgServerHandler extends ChannelInboundHandlerAdapter {
    private final String name;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String reqXml = (String) msg;
        log.info("请求报文：{}", reqXml);

        String respXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SMS>\n" +
                "    <ORG_ID>000064500000</ORG_ID>\n" +
                "    <SMS_NO>000000000053</SMS_NO>\n" +
                "    <SMS_TYPE>00</SMS_TYPE>\n" +
                "    <TEL>182****2031</TEL>\n" +
                "    <CONTENT>****</CONTENT>\n" +
                "    <REQUEST_TIME>20241011144544</REQUEST_TIME>\n" +
                "    <SEND_TIME>null</SEND_TIME>\n" +
                "    <RESERVED></RESERVED>\n" +
                "    <RESP_FLAG>00</RESP_FLAG>\n" +
                "    <RESP_DETAIL>发送成功</RESP_DETAIL>\n" +
                "</SMS>";
        log.info("响应报文：{}", respXml);
        ctx.writeAndFlush(respXml);
    }
}
