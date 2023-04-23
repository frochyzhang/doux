package com.allinfinance.dev.gateway.raft;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.allinfinance.dev.gateway.raft.service.GatewayService;
import com.allinfinance.dev.rpc.scaffold.api.dto.raft.ExporterRegistrarRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/11/23 14:50
 */
@Component
public class AppRegistrarProcessor implements RpcProcessor<ExporterRegistrarRequest> {
    private static final Logger logger = LoggerFactory.getLogger(AppRegistrarProcessor.class);

    @Autowired
    private GatewayService gatewayService;

    /**
     * Async to handle request with {@link RpcContext}.
     *
     * @param rpcCtx  the-- rpc context
     * @param request the request
     */
    @Override
    public void handleRequest(RpcContext rpcCtx, ExporterRegistrarRequest request) {
        logger.info("接收到exporter注册请求, request: {}", request);
        GatewayClosure gatewayClosure = new GatewayClosure() {
            /**
             * Called when task is done.
             *
             * @param status the task status.
             */
            @Override
            public void run(Status status) {
                logger.info("exporter注册请求处理完成, result: {}, status: {}", getResult(), status);
                rpcCtx.sendResponse(getResult());
            }
        };
        gatewayService.register(request.getBootstrap(), gatewayClosure);
    }

    /**
     * The class name of user request.
     * Use String type to avoid loading class.
     *
     * @return interested request's class name
     */
    @Override
    public String interest() {
        return ExporterRegistrarRequest.class.getName();
    }
}
