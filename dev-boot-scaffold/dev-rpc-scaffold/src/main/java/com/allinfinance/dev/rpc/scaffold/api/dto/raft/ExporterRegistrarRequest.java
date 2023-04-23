package com.allinfinance.dev.rpc.scaffold.api.dto.raft;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/11/29 19:51
 */
public class ExporterRegistrarRequest implements Serializable {
    private RpcConfigurationProperties.Bootstrap bootstrap;

    public ExporterRegistrarRequest(RpcConfigurationProperties.Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public RpcConfigurationProperties.Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(RpcConfigurationProperties.Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public String toString() {
        return "GatewayRegistrarRequest{" +
                "bootstrap=" + bootstrap +
                '}';
    }
}
