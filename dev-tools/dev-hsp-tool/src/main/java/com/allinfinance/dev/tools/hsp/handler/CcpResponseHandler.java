package com.allinfinance.dev.tools.hsp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 17:30
 */
public class CcpResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CcpResponseHandler.class);

    private String appName;

    public CcpResponseHandler(String appName) {
        this.appName = appName;
    }

    public void channelActive() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String reqMsg = (String) msg;
        logger.info("服务端收到请求消息:{}", reqMsg);
        ctx.writeAndFlush(reqMsg);

//        String responseMsg1 ="<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//                "  <SERVICE_HEADER>\n" +
//                "    <SERVICE_SN>2022101910152998160</SERVICE_SN>\n" +
//                "    <SERVICE_ID>11010</SERVICE_ID>\n" +
//                "    <test>11111</test>\n" +
//                "    <ORG>000066666666</ORG>\n" +
//                "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
//                "    <OP_ID />\n" +
//                "    <REQUST_TIME>20221019101529</REQUST_TIME>\n" +
//                "    <VERSION_ID>01</VERSION_ID>\n" +
//                "    <SERV_RESPONSE>\n" +
//                "      <STATUS>S</STATUS>\n" +
//                "      <CODE>SSSS</CODE>\n" +
//                "      <DESC>交易成功</DESC>\n";
//        String responseMsg2 ="<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//                "  <SERVICEddded_HEADER>\n" +
//                "    <SERVICE_SN>2022101910152998160</SERVICE_SN>\n" +
//                "    <SERVICE_ID>11010</SERVICE_ID>\n" +
//                "    <test>22222</test>\n" +
//                "    <ORG>000066666666</ORG>\n" +
//                "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
//                "    <OP_ID />\n" +
//                "    <REQUST_TIME>20221019101529</REQUST_TIME>\n" +
//                "    <VERSION_ID>01</VERSION_ID>\n" +
//                "    <SERV_RESPONSE>\n" +
//                "      <STATUS>S</STATUS>\n" +
//                "      <CODE>SSSS</CODE>\n" +
//                "      <DESC>交易成功</DESC>\n";
//
//        String labelValue = StrUtils.getLabelValue(reqMsg, "<test>");
//        if ("00001".equals(labelValue)){
//            ctx.writeAndFlush(responseMsg1);
//            logger.info("返回应答消息:{}|{}", responseMsg1, responseMsg1.getBytes(StandardCharsets.UTF_8).length);
//        }else if ("00002".equals(labelValue)){
//            ctx.writeAndFlush(responseMsg2);
//            logger.info("返回应答消息:{}|{}", responseMsg2, responseMsg2.getBytes(StandardCharsets.UTF_8).length);
//        }else {
//            ctx.writeAndFlush("接收信息内容有误！！！");
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
