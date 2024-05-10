package cn.lezoo.doux.feign;

import cn.lezoo.doux.common.socket.api.client.dto.SocketRequestDTO;

import java.lang.reflect.Type;

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
    public <T> T invoke(Object[] argv, Type returnType) throws Throwable {
        SocketRequestDTO requestDTO;

        if (argv.length > 1 && argv[0] instanceof SocketRequestDTO) {
            requestDTO = ((SocketRequestDTO) argv[0]);
        } else {
            requestDTO = new SocketRequestDTO(
                    target.url().split(":")[0],
                    target.url().split(":")[1],
                    target.name(),
                    String.valueOf(target.msgLengthSize()),
                    target.msgEncode()
            );
        }

        return client.execute(requestDTO, argv[argv.length - 1], returnType);
    }

    static class Factory {
        private final Client client;
        //        private final Retryer retryer;
        //        private final List<RequestInterceptor> requestInterceptors;
        //        private final ResponseInterceptor responseInterceptor;

        Factory(Client client) {
            this.client = client;
        }

        public InvocationHandlerFactory.MethodHandler create(Target<?> target) {
            return new SynchronousMethodHandler(target, client);
        }
    }
}
