/*
 * Copyright [2022/6/29] [<a href="mailto:frochyzhang@gmail.com>frochyZhang</a>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.lezoo.doux.infrastructure.conn.netty;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.extension.annotation.Extension;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ProgressivePromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/6/29 20:10
 */
@Extension(value = "default")
public class DefaultNettyConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(DefaultNettyConnection.class);

    private final EventLoopGroup loopGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    private static final DefaultEventLoop NETTY_EVENT_LOOP = new DefaultEventLoop(null, new NamedThreadFactory("NETTY_EVENT_LOOP", false));

    private static final AttributeKey<RequestContext> REQUEST_CONTEXT_ATTRIBUTE_KEY = AttributeKey.valueOf("REQUEST_CONTEXT_ATTRIBUTE_KEY");

    @Override
    public void close() {
        loopGroup.shutdownGracefully();
    }

    @Override
    public boolean isClosed() {
        return loopGroup.isShutdown();
    }

    @Override
    public String send(String msg) {
        Channel channel = channelFuture.channel();

        ProgressivePromise<String> promise = NETTY_EVENT_LOOP.newProgressivePromise();

        RequestContext requestContext = new RequestContext(UUID.fastUUID().toString(), promise);
        channel.attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).set(requestContext);

        channel.writeAndFlush(msg);

        try {
            return promise.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("处理中断");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("处理异常");
        } catch (TimeoutException e) {
            logger.error("获取响应超时");
        }
        throw new RuntimeException("获取响应异常");
    }

    @Override
    public void connect(Properties properties) {
        String serverIp = properties.getProperty("serverIp");
        int serverPort = Integer.parseInt(properties.getProperty("serverPort"));

        try {
            channelFuture = new Bootstrap()
                    .group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            RequestContext requestContext = ctx.channel().attr(REQUEST_CONTEXT_ATTRIBUTE_KEY).get();
                                            requestContext.getRespPromise().setSuccess((String) msg);
                                        }
                                    });
                        }
                    }).connect(serverIp, serverPort).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isValid(int validationTimeoutSeconds, String connectionTestQuery) {
        return true;
    }
}
