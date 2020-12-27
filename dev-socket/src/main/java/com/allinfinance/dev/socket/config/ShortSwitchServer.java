package com.allinfinance.dev.socket.config;

import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.core.util.socket.codec.MessageCodecFactory;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
@Configuration
@ImportResource(locations = {"classpath:socket-default-context.xml"})
public class ShortSwitchServer {

    private static final Logger logger = LoggerFactory.getLogger(ShortSwitchServer.class);
    @Autowired
    private List<MinaSocketBean> socketBeans;

    /**
     * 根据SocketBeanLoader中解析配置文件获取到的配置信息，依次开启对应服务端口并监听
     *
     * @author 张勇
     * @date 2020-11-28 01:38
     */
    @Bean
    public void init() {
        logger.info("正在启动应用，请稍后!");
        socketBeans.forEach(minaSocketBean -> {
            try {
                MessageDecoder messageDecoder = (MessageDecoder) Class.forName(minaSocketBean.getDecoderClassName())
                        .getConstructor(Integer.class, String.class)
                        .newInstance(minaSocketBean.getDecodeMsgLength(), minaSocketBean.getDecodeCharset());
                MessageEncoder messageEncoder = (MessageEncoder) Class.forName(minaSocketBean.getEncoderClassName())
                        .getConstructor(Integer.class, String.class)
                        .newInstance(minaSocketBean.getEncodeMsgLength(), minaSocketBean.getEncodeCharset());
                IoAcceptor acceptor = new NioSocketAcceptor(minaSocketBean.getProcessorCount());
                acceptor.getFilterChain().addLast("MsgCodec",
                        new ProtocolCodecFilter(new MessageCodecFactory(messageDecoder, messageEncoder)));
                acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
                acceptor.setHandler((IoHandler) Class.forName(minaSocketBean.getHandlerClassName())
                        .cast(SpringConfigTool.getBeanByClassName(minaSocketBean.getHandlerClassName())));
                acceptor.getSessionConfig().setReadBufferSize(minaSocketBean.getBufferSize());
                acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, minaSocketBean.getTimeOut());
                acceptor.setCloseOnDeactivation(true);

                acceptor.bind(new InetSocketAddress(minaSocketBean.getPort()));
                logger.info("[ {} ] 服务端启动成功，线程数为：{}，端口号为：{}", minaSocketBean.getName(), minaSocketBean.getProcessorCount(), minaSocketBean.getPort());
            } catch (Exception e2) {
                logger.error("[ {}] 启动服务失败!", minaSocketBean.getName(), e2);
                System.exit(0);
            }
        });
    }
}
