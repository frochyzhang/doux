/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.allinfinance.dev.gateway.netty.iohandler;


import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.gateway.netty.http.NettyHttpRequest;
import com.allinfinance.dev.gateway.netty.http.NettyHttpResponse;
import com.allinfinance.dev.rpc.scaffold.api.dto.HttpResponseDTO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/18 13:34
 */
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private static ExecutorService executor = null;

    private final String appUniqueId;

    private final Integer port;

    public HttpServerHandler(String uniqueId, int port) {
        this.appUniqueId = uniqueId;
        this.port = port;

        ThreadFactory threadFactory = new cn.hutool.core.thread.ThreadFactoryBuilder()
                .setNamePrefix(uniqueId + "-server-pool-")
                .setDaemon(true)
                .build();
        executor = new ThreadPoolExecutor(30, 30, 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        FullHttpRequest copyRequest = request.copy();
        executor.execute(() -> onReceivedRequest(ctx, new NettyHttpRequest(copyRequest)));
    }


    private void onReceivedRequest(ChannelHandlerContext context, NettyHttpRequest request) {
        FullHttpResponse response = handleHttpRequest(request);
        context.writeAndFlush(response).addListener(future -> logger.info("Response sended and flushed"));
        ReferenceCountUtil.release(request);
    }

    private FullHttpResponse handleHttpRequest(NettyHttpRequest request) {
        System.out.println("requested!");
        HttpResponseDTO httpResponseDTO;
        try {
            httpResponseDTO = AppProcessFactory.httpProcessed(appUniqueId, request, port);
        } catch (Exception e) {
            logger.error("请求应用前置失败:", e);
            return NettyHttpResponse.makeError(e);
        }

        return NettyHttpResponse.ok(httpResponseDTO.getHeaders(), httpResponseDTO.getResponseMsg());
    }
}
