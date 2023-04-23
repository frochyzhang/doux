package com.allinfinance.dev.gateway.raft;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.allinfinance.dev.gateway.raft.service.GatewayService;
import com.allinfinance.dev.rpc.scaffold.api.dto.raft.ExporterOfflineRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/11/25 16:08
 */
@Component
public class ExporterOfflineProcessor implements RpcProcessor<ExporterOfflineRequest> {
    private static final Logger logger = LoggerFactory.getLogger(ExporterOfflineProcessor.class);

    @Autowired
    private GatewayService gatewayService;

    /**
     * Async to handle request with {@link RpcContext}.
     *
     * @param rpcCtx  the rpc context
     * @param request the request
     */
    @Override
    public void handleRequest(RpcContext rpcCtx, ExporterOfflineRequest request) {
        logger.info("接收到exporter下线请求, request: {}", request);
        GatewayClosure closure = new GatewayClosure() {
            /**
             * Called when task is done.
             *
             * @param status the task status.
             */
            @Override
            public void run(Status status) {
                logger.info("exporter下线请求处理完成, result: {}, status: {}", getResult(), status);
                rpcCtx.sendResponse(getResult());
            }
        };
        gatewayService.offline(request.getAppUniqueId(), closure);
    }

    /**
     * The class name of user request.
     * Use String type to avoid loading class.
     *
     * @return interested request's class name
     */
    @Override
    public String interest() {
        return ExporterOfflineRequest.class.getName();
    }
}
