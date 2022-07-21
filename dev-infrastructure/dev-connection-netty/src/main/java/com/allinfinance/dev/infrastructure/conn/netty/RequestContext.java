package com.allinfinance.dev.infrastructure.conn.netty;

import io.netty.util.concurrent.Promise;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/12
 **/
public class RequestContext {
    private final String requestId;

    private final Promise<String> respPromise;


    public RequestContext(String requestId, Promise<String> respPromise) {
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
