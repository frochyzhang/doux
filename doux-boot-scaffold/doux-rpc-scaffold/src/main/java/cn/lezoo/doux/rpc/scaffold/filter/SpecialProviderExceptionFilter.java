package cn.lezoo.doux.rpc.scaffold.filter;

import com.alipay.sofa.rpc.core.exception.RpcErrorType;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/5/31
 **/
@Extension(value = "specialProviderException")
@AutoActive(providerSide = true)
public class SpecialProviderExceptionFilter extends Filter {
    private static final Logger logger = LoggerFactory.getLogger(SpecialProviderExceptionFilter.class);

    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        try {
            // 业务异常已经被包在SofaResponse中，需要判断
            SofaResponse sofaResponse = invoker.invoke(request);
            if (ObjectUtils.isNotEmpty(sofaResponse) && sofaResponse.getAppResponse() instanceof Throwable) {
                //业务响应为异常或错误
                Throwable errorResponse = (Throwable) sofaResponse.getAppResponse();
                logger.error("服务提供方系统或业务异常", errorResponse);
            }
            return sofaResponse;
        } catch (SofaRpcException e) {
            logger.error("RPC调用异常", e);
            throw e;
        } catch (Throwable t) {
            logger.error("其他提供方异常", t);
            throw new SofaRpcException(RpcErrorType.SERVER_FILTER, t);
        }
    }
}
