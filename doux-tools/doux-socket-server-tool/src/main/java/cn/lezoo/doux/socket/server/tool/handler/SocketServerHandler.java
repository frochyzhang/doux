package cn.lezoo.doux.socket.server.tool.handler;

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
        //language=XML
        String responseXml = "<SERVICE>\n"
            + "    <SERVICE_HEADER>\n"
            + "        <SERVICE_SN>ALSKDJFLAKSDJF</SERVICE_SN>\n"
            + "        <SERVICE_ID>16050</SERVICE_ID>\n"
            + "        <SERV_RESPONSE>\n"
            + "            <STATUS>S</STATUS>\n"
            + "        </SERV_RESPONSE>\n"
            + "    </SERVICE_HEADER>\n"
            + "    <SERVICE_BODY>\n"
            + "        <RESPONSE>\n"
            + "            <CARD_NO>6288888888888888</CARD_NO>\n"
            + "            <ID_TYPE>01</ID_TYPE>\n"
            + "            <ID_NO>410482198302100584</ID_NO>\n"
            + "            <CUST_NAME>张行行</CUST_NAME>\n"
            +
            "            <MOBILE_NO>13201569405</MOBILE_NO>\n"
            + "        </RESPONSE>\n"
            +
            "    </SERVICE_BODY>\n"
            + "</SERVICE>";
        ctx.writeAndFlush(responseXml);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
