package com.allinfinance.dev.connection.scaffold.netty.context;

import io.netty.util.concurrent.Promise;

/**
 * @author qipeng
 * @date 2022/6/17 15:56
 * @description
 */
public class RequestContext {
    private String requestId;

    private Promise<String> responsePromise;

    public RequestContext(String requestId, Promise<String> responsePromise) {
        this.requestId = requestId;
        this.responsePromise = responsePromise;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Promise<String> getResponsePromise() {
        return responsePromise;
    }

    public void setResponsePromise(Promise<String> responsePromise) {
        this.responsePromise = responsePromise;
    }
}
