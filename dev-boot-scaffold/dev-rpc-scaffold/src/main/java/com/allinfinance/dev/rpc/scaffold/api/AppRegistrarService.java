package com.allinfinance.dev.rpc.scaffold.api;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 10:25
 */
public interface AppRegistrarService {
    Boolean register(RpcConfigurationProperties.Bootstrap bootstrap);
}
