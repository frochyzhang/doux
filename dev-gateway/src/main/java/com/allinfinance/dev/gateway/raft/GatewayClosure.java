package com.allinfinance.dev.gateway.raft;

import com.alipay.sofa.jraft.Closure;

/**
 * @author huanghf
 * @date 2022/11/24 9:30
 */
public abstract class GatewayClosure implements Closure {

    private Boolean result;

    private GatewayOperation gatewayOperation;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public GatewayOperation getGatewayOperation() {
        return gatewayOperation;
    }

    public void setGatewayOperation(GatewayOperation gatewayOperation) {
        this.gatewayOperation = gatewayOperation;
    }

    public void failure() {
        setResult(false);
    }

    public void success() {
        setResult(true);
    }
}
