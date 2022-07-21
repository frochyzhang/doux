package com.allinfinance.dev.rpc.scaffold.api;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/7 11:34
 */
public abstract class AbstractProcessService implements ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProcessService.class);
    private RpcConfigurationProperties rpcConfigurationProperties;


    /**
     * 注册到网关后的验证接口
     *
     * @return
     */
    @Override
    public Boolean verify() {
        logger.info("业务处理服务验证接口调用成功");
        return Boolean.TRUE;
    }

    /**
     * 返回应用参数
     *
     * @return
     */
    @Override
    public RpcConfigurationProperties.Bootstrap init() {
        return rpcConfigurationProperties.getBootstrap();
    }

    @Autowired
    public void setRpcConfigurationProperties(RpcConfigurationProperties rpcConfigurationProperties) {
        this.rpcConfigurationProperties = rpcConfigurationProperties;
    }
}
