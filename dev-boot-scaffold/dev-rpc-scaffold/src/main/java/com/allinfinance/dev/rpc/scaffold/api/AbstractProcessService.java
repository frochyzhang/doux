package com.allinfinance.dev.rpc.scaffold.api;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/7 11:34
 */
public abstract class AbstractProcessService implements ProcessService {

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    /**
     * 注册到网关后的验证接口
     *
     * @return
     */
    @Override
    public Boolean verify() {
        return Boolean.TRUE;
    }

    /**
     * 返回应用参数
     *
     * @return
     */
    @Override
    public Integer init() {
        Integer listenPort = rpcConfigurationProperties.getBootstrap().getListenPort();
        return listenPort;
    }
}
