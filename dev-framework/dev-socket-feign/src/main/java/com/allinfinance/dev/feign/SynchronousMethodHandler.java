package com.allinfinance.dev.feign;

import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 15:38
 */
public class SynchronousMethodHandler implements InvocationHandlerFactory.MethodHandler {
    private final Target<?> target;
    private final Client client;

    public SynchronousMethodHandler(Target<?> target, Client client) {
        this.target = target;
        this.client = client;
    }

    @Override
    public <T> T invoke(Object[] argv, Class<T> returnType) throws Throwable {
        SocketRequestDTO requestDTO = new SocketRequestDTO(
            target.url().split(":")[0],
            target.url().split(":")[1],
            target.name(),
            String.valueOf(target.msgLengthSize()),
            target.msgEncode()
        );
        return client.execute(requestDTO, argv[0],returnType);
    }

    static class Factory {
        private final Client client;
        //        private final Retryer retryer;
        //        private final List<RequestInterceptor> requestInterceptors;
        //        private final ResponseInterceptor responseInterceptor;

        public Factory(Client client) {
            this.client = client;
        }

        public InvocationHandlerFactory.MethodHandler create(Target<?> target) {
            return new SynchronousMethodHandler(target, client);
        }
    }
}
