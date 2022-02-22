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
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/18 13:34
 */
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(30, new ThreadFactoryBuilder().setNameFormat("NettyHttpHandler-%d").build());

//    static {
//        ThreadFactory threadFactory = ;
//        executor = new ThreadPoolExecutor(10, 200,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<>(0), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        FullHttpRequest copyRequest = request.copy();
//        onReceivedRequest(ctx, new NettyHttpRequest(copyRequest));
        executor.execute(() -> onReceivedRequest(ctx, new NettyHttpRequest(copyRequest)));
    }


    private void onReceivedRequest(ChannelHandlerContext context, NettyHttpRequest request) {
        FullHttpResponse response = handleHttpRequest(request);
        context.writeAndFlush(response).addListener(future -> logger.info("Response sended and flushed"));
//        context.writeAndFlush(response);
        ReferenceCountUtil.release(request);
    }

    private FullHttpResponse handleHttpRequest(NettyHttpRequest request) {
        // TODO: 2022/2/18 处理业务逻辑
        System.out.println("requested!");

        String uri = request.getUri();
        String requestMsg = request.contentText();

        String resp = AppProcessFactory.httpProcessed(uri, requestMsg);

        return NettyHttpResponse.ok(resp);
//        IFunctionHandler functionHandler = null;
//
//        try {
//            functionHandler = matchFunctionHandler(request);
//            Response response =  functionHandler.execute(request);
//            return NettyHttpResponse.ok(response.toJSONString());
//        }
//        catch (IllegalMethodNotAllowedException error){
//            return NettyHttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED);
//        }
//        catch (IllegalPathNotFoundException error){
//            return NettyHttpResponse.make(HttpResponseStatus.NOT_FOUND);
//        }
//        catch (Exception error){
//            LOGGER.error(functionHandler.getClass().getSimpleName() + " Error",error);
//            return NettyHttpResponse.makeError(error);
//        }
    }

    static class Test {
        private String uri;
        private String method;
        private String body;

        public Test(String uri, String method, String body) {
            this.uri = uri;
            this.method = method;
            this.body = body;
        }
    }
}
