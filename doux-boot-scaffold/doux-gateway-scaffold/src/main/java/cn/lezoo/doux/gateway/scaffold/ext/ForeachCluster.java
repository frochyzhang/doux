package cn.lezoo.doux.gateway.scaffold.ext;

import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.client.AbstractCluster;
import com.alipay.sofa.rpc.client.ProviderInfo;
import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.common.utils.CommonUtils;
import com.alipay.sofa.rpc.context.RpcInternalContext;
import com.alipay.sofa.rpc.core.exception.RpcErrorType;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.log.LogCodes;
import com.alipay.sofa.rpc.log.Logger;
import com.alipay.sofa.rpc.log.LoggerFactory;
import com.alipay.sofa.rpc.transport.ClientTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrn
 * @date 2022/2/9
 */
@Extension("foreach")
public class ForeachCluster extends AbstractCluster {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForeachCluster.class);

    public ForeachCluster(ConsumerBootstrap consumerBootstrap) {
        super(consumerBootstrap);
    }

    /**
     * 子类实现各自逻辑的调用，例如重试等
     *
     * @param request Request对象
     * @return 调用结果
     * @throws SofaRpcException rpc异常
     */
    @Override
    protected SofaResponse doInvoke(SofaRequest request) throws SofaRpcException {
        String methodName = request.getMethodName();
        int retries = consumerConfig.getMethodRetries(methodName);
        int time = 0;
        SofaRpcException throwable = null;
        List<ProviderInfo> invokedProviderInfos = new ArrayList<>(retries + 1);
        SofaResponse response = null;
        List<ProviderInfo> providerInfos = routerChain.route(request, null);
        if (CommonUtils.isEmpty(providerInfos)) {
            throw noAvailableProviderException(request.getInterfaceName());
        }
        for (ProviderInfo providerInfo : providerInfos) {

            ClientTransport clientTransport = selectByProvider(request, providerInfo);
            if (clientTransport == null) {
                // 指定的不存在或已死，抛出异常
                throw unavailableProviderException(request.getTargetServiceUniqueName(), providerInfo.getHost());
            }
            try {
                response = filterChain(providerInfo, request);
                if (response != null) {
                    if (throwable != null) {
                        if (LOGGER.isWarnEnabled(consumerConfig.getAppName())) {
                            LOGGER.warnWithApp(consumerConfig.getAppName(),
                                    LogCodes.getLog(LogCodes.WARN_SUCCESS_BY_RETRY,
                                            throwable.getClass() + ":" + throwable.getMessage(),
                                            invokedProviderInfos));
                        }
                    }

                    time++;
                } else {
                    throwable = new SofaRpcException(RpcErrorType.CLIENT_UNDECLARED_ERROR,
                            "Failed to call " + request.getInterfaceName() + "." + methodName
                                    + " on remote server " + providerInfo + ", return null");
                }
            } catch (SofaRpcException e) { // 服务端异常+ 超时异常 才发起rpc异常重试
                if (e.getErrorType() == RpcErrorType.SERVER_BUSY
                        || e.getErrorType() == RpcErrorType.CLIENT_TIMEOUT) {
                    throwable = e;
                } else {
                    throw e;
                }
            } catch (Exception e) { // 其它异常不重试
                throw new SofaRpcException(RpcErrorType.CLIENT_UNDECLARED_ERROR,
                        "Failed to call " + request.getInterfaceName() + "." + request.getMethodName()
                                + " on remote server: " + providerInfo + ", cause by unknown exception: "
                                + e.getClass().getName() + ", message is: " + e.getMessage(), e);
            } finally {
                if (RpcInternalContext.isAttachmentEnable()) {
                    RpcInternalContext.getContext().setAttachment(RpcConstants.INTERNAL_KEY_INVOKE_TIMES,
                            time + 1); // 重试次数
                }
            }
            invokedProviderInfos.add(providerInfo);
        }
        if (time > 0 && time == providerInfos.size()) {
            return response;
        }
        throwable = new SofaRpcException(RpcErrorType.CLIENT_UNDECLARED_ERROR,
                "Failed to call " + request.getInterfaceName() + "." + request.getMethodName());
        throw throwable;
    }
}
