package com.allinfinance.dev.gateway.netty;

import com.allinfinance.dev.gateway.netty.iohandler.FilterLoggingHandler;
import com.allinfinance.dev.gateway.netty.iohandler.HttpServerHandler;
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
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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

    private Integer port;

    private static final Map<String, List<HttpServer>> APP_SERVER_MAP = new ConcurrentHashMap<>();

    public void start(String uniqueId, int port, RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig httpConfig) {
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
        FilterLoggingHandler loggingHandler = new FilterLoggingHandler();

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
                ch.pipeline().addLast("logging", loggingHandler);
                ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(httpConfig.getReadTimeout()));
                ch.pipeline().addLast("bizHandler", new HttpServerHandler(uniqueId, port, httpConfig.getThreadCount()));
            }
        })
        ;
        ChannelFuture channelFuture = bootstrap.bind(port).syncUninterruptibly().addListener(future -> {
            String logBanner = "Netty Http Server started on port {}.";
            if (APP_SERVER_MAP.get(uniqueId) == null) {
                List<HttpServer> httpServerList = new ArrayList<>();
                httpServerList.add(this);
                APP_SERVER_MAP.put(uniqueId, httpServerList);
            } else {
                APP_SERVER_MAP.get(uniqueId).add(this);
            }
            this.port = port;
            logger.info(logBanner, port);
        });
        channelFuture.channel().closeFuture().addListener(future -> {
            logger.info("Netty Http Server Has Been Shutdown ............");
        });
    }

    public void shutdown(String uniqueId) {
        logger.info("Netty Http Server Start Shutdown ............");
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
        APP_SERVER_MAP.get(uniqueId).remove(this);
        if (CollectionUtils.isEmpty(APP_SERVER_MAP.get(uniqueId))) {
            APP_SERVER_MAP.remove(uniqueId);
        }
    }

    public static List<HttpServer> getInstance(String appUniqueId) {
        return APP_SERVER_MAP.get(appUniqueId);
    }

    public Integer getPort() {
        return port;
    }

    public static HttpServer getHttpServer(int port) {
        for (List<HttpServer> httpServers : APP_SERVER_MAP.values()) {
            for (HttpServer httpServer : httpServers) {
                if (httpServer.getPort().equals(port)) {
                    return httpServer;
                }
            }
        }
        return null;
    }

    public static void saveHttpServer(String uniqueId, HttpServer httpServer) {
        if (APP_SERVER_MAP.get(uniqueId) == null) {
            List<HttpServer> httpServerList = new ArrayList<>();
            httpServerList.add(httpServer);
            APP_SERVER_MAP.put(uniqueId, httpServerList);
        } else {
            APP_SERVER_MAP.get(uniqueId).add(httpServer);
        }
    }
}
