package com.allinfinance.dev.feign;

import java.nio.charset.Charset;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 15:38
 */
public class SynchronousMethodHandler implements InvocationHandlerFactory.MethodHandler {
    private final Target<?> target;
    private final Client client;
    private final Charset encoding;

    public SynchronousMethodHandler(Target<?> target, Client client, Charset encoding) {
        this.target = target;
        this.client = client;
        this.encoding = encoding;
    }

    @Override
    public Object invoke(Object[] argv) throws Throwable {
        Request request = Request.create(target.method(), target.url(), encoding, argv[0]);
       return client.execute(request);
    }

    static class Factory {
        private final Client client;
        //        private final Retryer retryer;
        //        private final List<RequestInterceptor> requestInterceptors;
        //        private final ResponseInterceptor responseInterceptor;

        public Factory(Client client) {
            this.client = client;
        }

        public InvocationHandlerFactory.MethodHandler create(Target<?> target,String encoding) {
            return new SynchronousMethodHandler(target, client, Charset.forName(encoding));
        }
    }
}
