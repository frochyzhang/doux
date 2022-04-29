package com.allinfinance.dev.gateway.netty;

import com.allinfinance.dev.gateway.netty.iohandler.FilterLogginglHandler;
import com.allinfinance.dev.gateway.netty.iohandler.HttpServerHandler;
import com.allinfinance.dev.gateway.netty.iohandler.InterceptorHandler;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpMethod.PUT;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/18 13:34
 */
public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static final Map<String, HttpServer> APP_SERVER_MAP = new ConcurrentHashMap<>();

    public Boolean start(String uniqueId, int port, RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig httpConfig) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        /* 跨域处理开始 */
        AtomicReference<CorsConfigBuilder> corsConfigBuilder = new AtomicReference<>(CorsConfigBuilder
                .forAnyOrigin()
                .allowNullOrigin());

        Optional.ofNullable(System.getProperty("crossList"))
                .ifPresent(lst -> {
                    corsConfigBuilder.set(CorsConfigBuilder.forOrigins(lst.split(",")));
                });

        CorsConfig config = corsConfigBuilder.get().allowedRequestHeaders("Origin, X-Requested-With, Content-Type, Accept".split(","))
                .allowedRequestMethods(GET, POST, PUT)
                .allowCredentials().build();
        /* 跨域处理结束 */

        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(NioChannelOption.TCP_NODELAY, httpConfig.getTcpNoDelay());
        bootstrap.childOption(NioChannelOption.SO_REUSEADDR, httpConfig.getSoReUseAddr());
        bootstrap.childOption(NioChannelOption.SO_KEEPALIVE, httpConfig.getSoKeepAlive());
        bootstrap.childOption(NioChannelOption.SO_RCVBUF, httpConfig.getSoRcvBuf());
        bootstrap.childOption(NioChannelOption.SO_SNDBUF, httpConfig.getSoSndBuf());
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast("codec", new HttpServerCodec());
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(512 * 1024));
                ch.pipeline().addLast("corsHandler", new CorsHandler(config));
                ch.pipeline().addLast("logging", new FilterLogginglHandler());
                ch.pipeline().addLast("interceptor", new InterceptorHandler());
                ch.pipeline().addLast("bizHandler", new HttpServerHandler(uniqueId, port));
            }
        })
        ;
        ChannelFuture channelFuture = bootstrap.bind(port).syncUninterruptibly().addListener(future -> {
            String logBanner = "Netty Http Server started on port {}.";
            APP_SERVER_MAP.put(uniqueId, this);
            logger.info(logBanner, port);
        });
        channelFuture.channel().closeFuture().addListener(future -> {
            logger.info("Netty Http Server Start Shutdown ............");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        });
        return Boolean.TRUE;
    }

    public Boolean shutdown() {
        logger.info("Netty Http Server Start Shutdown ............");
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
        return Boolean.TRUE;
    }

    public static HttpServer getInstance(String appUniqueId) {
        return APP_SERVER_MAP.get(appUniqueId);
    }
}
