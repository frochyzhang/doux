package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.ccp.service.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.ccp.service.codec.DemuxingMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/28
 **/
public class NettyServer {

    private final int port;
    private final int msgLength;
    private final String charset;
    private final String responseSuffix;

    public NettyServer(int port, int msgLength, String charset, String reponseSuffix) {
        this.port = port;
        this.msgLength = msgLength;
        this.charset = charset;
        this.responseSuffix = reponseSuffix;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DemuxingMessageDecoder(msgLength, charset))
                                    .addLast(new DemuxingMessageEncoder(msgLength, charset))
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                            ctx.writeAndFlush(responseSuffix + msg);
                                            if (!StringUtils.contains((String) msg, "SMS")) {
                                                ctx.writeAndFlush("<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                                                        "  <SERVICE_HEADER>\n" +
                                                        "    <SERVICE_SN>20230228190606776222</SERVICE_SN>\n" +
                                                        "    <SERVICE_ID>16050</SERVICE_ID>\n" +
                                                        "    <ORG>000064500000</ORG>\n" +
                                                        "    <CHANNEL_ID>06</CHANNEL_ID>\n" +
                                                        "    <OP_ID />\n" +
                                                        "    <REQUST_TIME>20230228190606</REQUST_TIME>\n" +
                                                        "    <VERSION_ID>01</VERSION_ID>\n" +
                                                        "    <SERV_RESPONSE>\n" +
                                                        "      <STATUS>S</STATUS>\n" +
                                                        "      <CODE>SSSS</CODE>\n" +
                                                        "      <DESC>交易成功</DESC>\n" +
                                                        "    </SERV_RESPONSE>\n" +
                                                        "    <RES_SERVICE_SN>20230228190606776222</RES_SERVICE_SN>\n" +
                                                        "    <RES_SERVICE_TIME>20230228191348</RES_SERVICE_TIME>\n" +
                                                        "  </SERVICE_HEADER>\n" +
                                                        "  <SERVICE_BODY>\n" +
                                                        "    <RESPONSE/>\n" +
                                                        "  </SERVICE_BODY>\n" +
                                                        "</SERVICE>\n");
                                            } else {
                                                ctx.writeAndFlush("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                        "<SMS>\n" +
                                                        "<ORG_ID>000064500000</ORG_ID>\n" +
                                                        "<SMS_NO>5109863901</SMS_NO>\n" +
                                                        "<SMS_TYPE>00</SMS_TYPE>\n" +
                                                        "<TEL>151****3366</TEL>\n" +
                                                        "<CONTENT>****</CONTENT>\n" +
                                                        "<REQUEST_TIME>20230412155109</REQUEST_TIME>\n" +
                                                        "<SEND_TIME>20230412155109</SEND_TIME>\n" +
                                                        "<RESERVED> </RESERVED>\n" +
                                                        "<RESP_FLAG>00</RESP_FLAG>\n" +
                                                        "<RESP_DETAIL>发送成功</RESP_DETAIL>\n" +
                                                        "</SMS>\n");
                                            }
                                        }

                                        @Override
                                        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                            ctx.channel().close();
                                        }
                                    });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 8)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            //bind()可以根据需要多次调用该方法（使用不同的绑定地址）
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(()->{
            try {
                new NettyServer(7000, 6, "UTF-8", "").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                new NettyServer(7001, 4, "UTF-8", "").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
