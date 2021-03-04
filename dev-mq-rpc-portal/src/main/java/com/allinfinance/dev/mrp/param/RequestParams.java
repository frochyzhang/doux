package com.allinfinance.dev.mrp.param;

public class RequestParams {

    public static final String MQ_SWITCH = "MQ";
    public static final String RPC_SWITCH = "RPC";

    private String rpcInterface;
    private String rpcMethod;

    private String routingKey;
    private String exchangeName;

    public String getRpcInterface() {
        return rpcInterface;
    }

    public void setRpcInterface(String rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    public String getRpcMethod() {
        return rpcMethod;
    }

    public void setRpcMethod(String rpcMethod) {
        this.rpcMethod = rpcMethod;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "rpcInterface='" + rpcInterface + '\'' +
                ", rpcMethod='" + rpcMethod + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", exchangeName='" + exchangeName + '\'' +
                '}';
    }
}
