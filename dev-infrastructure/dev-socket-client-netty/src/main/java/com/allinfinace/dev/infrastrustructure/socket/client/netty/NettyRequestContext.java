package com.allinfinace.dev.infrastrustructure.socket.client.netty;

import io.netty.util.concurrent.Promise;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 10:26
 */
public class NettyRequestContext {
    private final String requestId;

    private final Promise<String> respPromise;


    public NettyRequestContext(String requestId, Promise<String> respPromise) {
        this.requestId = requestId;
        this.respPromise = respPromise;
    }

    public String getRequestId() {
        return requestId;
    }

    public Promise<String> getRespPromise() {
        return respPromise;
    }


}
