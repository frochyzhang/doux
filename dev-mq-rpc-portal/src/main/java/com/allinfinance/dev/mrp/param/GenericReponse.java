package com.allinfinance.dev.mrp.param;

public class GenericReponse<T> {
    private Boolean respStatus;
    private T rpcInvokeData;

    public GenericReponse(Boolean respStatus) {
        this.respStatus = respStatus;
    }

    public Boolean getRespStatus() {
        return respStatus;
    }

    public void setRespStatus(Boolean respStatus) {
        this.respStatus = respStatus;
    }

    public T getRpcInvokeData() {
        return rpcInvokeData;
    }

    public void setRpcInvokeData(T rpcInvokeData) {
        this.rpcInvokeData = rpcInvokeData;
    }

    @Override
    public String toString() {
        return "GenericReponse{" +
                "respStatus=" + respStatus +
                ", rpcInvokeData=" + rpcInvokeData +
                '}';
    }
}
