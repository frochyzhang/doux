package com.allinfinance.dev.ccp;

import com.allinfinance.dev.core.util.socket.client.ClientIoHandler;
import com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder;
import com.allinfinance.dev.core.util.socket.codec.MessageCodecFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 15:29
 */
@SpringBootApplication
public class CcpApplication {
    public static void main(String[] args) {
        SpringApplication.run(CcpApplication.class, args);
    }

    @Bean
    public NioSocketConnector nioSocketConnector() {
        NioSocketConnector nioSocketConnector = new NioSocketConnector();
        nioSocketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        nioSocketConnector.getSessionConfig().setUseReadOperation(true);
        nioSocketConnector.setConnectTimeoutMillis(30 * 1000);
        nioSocketConnector.setHandler(new ClientIoHandler(false));

        return nioSocketConnector;
    }
}
