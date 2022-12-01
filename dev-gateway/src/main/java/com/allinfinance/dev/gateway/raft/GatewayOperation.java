package com.allinfinance.dev.gateway.raft;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/11/24 14:20
 */
public class GatewayOperation implements Serializable {
    public static final byte REGISTER = 0x01;
    public static final byte OFFLINE = 0x02;

    private byte operation;
    private RpcConfigurationProperties.Bootstrap bootstrap;
    private String appUniqueId;

    public GatewayOperation(byte operation, RpcConfigurationProperties.Bootstrap bootstrap) {
        this.operation = operation;
        this.bootstrap = bootstrap;
    }

    public GatewayOperation(byte operation, String appUniqueId) {
        this.operation = operation;
        this.appUniqueId = appUniqueId;
    }

    public byte getOperation() {
        return operation;
    }

    public RpcConfigurationProperties.Bootstrap getBootstrap() {
        return bootstrap;
    }

    public String getAppUniqueId() {
        return appUniqueId;
    }

    public static GatewayOperation createRegister(RpcConfigurationProperties.Bootstrap bootstrap) {
        return new GatewayOperation(REGISTER, bootstrap);
    }

    public static GatewayOperation createOffline(String appUniqueId) {
        return new GatewayOperation(OFFLINE, appUniqueId);
    }
}
