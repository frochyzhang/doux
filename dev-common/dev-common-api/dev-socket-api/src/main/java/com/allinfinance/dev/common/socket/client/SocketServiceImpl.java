package com.allinfinance.dev.common.socket.client;

import com.allinfinance.dev.common.socket.client.pojo.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.pojo.SocketResponseDTO;
import com.allinfinance.dev.common.socket.codec.*;
import com.allinfinance.dev.common.socket.server.HreatBeatServerHandler;
import com.allinfinance.dev.core.util.socket.client.ISocketClientService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:24
 */
@Service("socketService")
public class SocketServiceImpl implements ISocketClient {
    private Logger logger = LoggerFactory.getLogger(com.allinfinance.dev.core.util.socket.client.SocketServiceImpl.class);

    @Override
    public SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ArrayBlockingQueue<SocketResponseDTO> queue = new ArrayBlockingQueue<>(1);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    if ("8583".equals(socketRequestDTO.getClientAppName())) {
                        ch.pipeline().addLast(new Message8583Encoder());
                        ch.pipeline().addLast(new Message8583Decoder(queue));
                    } else {
                        ch.pipeline().addLast(new DemuxingMessageDecoder(socketRequestDTO.getMsgLengthSize(), socketRequestDTO.getMsgEncode(), queue))
                                .addLast(new DemuxingMessageEncoder(socketRequestDTO.getMsgLengthSize(), socketRequestDTO.getMsgEncode()));
                    }
                    ch.pipeline().addLast(new IdleStateHandler(5, 10, 20, TimeUnit.SECONDS));
                    ch.pipeline().addLast(new HreatBeatClientHandler());
                }
            });
            ChannelFuture f = b.connect(socketRequestDTO.getRemoteIp(), socketRequestDTO.getRemotePort()).sync();
            f.channel().writeAndFlush(socketRequestDTO.getMessage());
            SocketResponseDTO result = queue.poll(socketRequestDTO.getTimeOutSeconds(), TimeUnit.SECONDS);
            logger.info("客户端收到消息=={}", result);
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new SocketResponseDTO();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            SocketServiceImpl client = new SocketServiceImpl();
            String reqMess = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SMS>\n" +
                    "    <ORG_ID>000064163056</ORG_ID>\n" +
                    "    <SMS_NO>113133000326</SMS_NO>\n" +
                    "    <SMS_TYPE>00</SMS_TYPE>\n" +
                    "    <TEL>13585961521</TEL>\n" +
                    "    <CONTENT>您好,您用于开通快捷支付的验证码为[226230].</CONTENT>\n" +
                    "    <REQUEST_TIME>20170612113133</REQUEST_TIME>\n" +
                    "    <RESERVED></RESERVED>\n" +
                    "</SMS>";
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", 4396, "sms", 30, false, reqMess, 4, "UTF-8", false);
            SocketResponseDTO response1 = client.clientRequest(socketRequestDTO);
            System.out.println("resultResp:" + response1);
        } finally {
            System.out.println("end");
        }
    }
}
