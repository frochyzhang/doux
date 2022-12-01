package com.allinfinance.dev.gateway.raft.service;

import com.allinfinance.dev.gateway.raft.GatewayClosure;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;

/**
 * @author huanghf
 * @date 2022/11/24 15:43
 */
public interface GatewayService {
    void register(RpcConfigurationProperties.Bootstrap bootstrap, GatewayClosure closure);

    void offline(String appUniqueId, GatewayClosure closure);
}
