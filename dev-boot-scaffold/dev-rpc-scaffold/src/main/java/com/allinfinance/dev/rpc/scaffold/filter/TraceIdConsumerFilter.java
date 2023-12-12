package com.allinfinance.dev.rpc.scaffold.filter;

import com.alipay.sofa.rpc.context.RpcInvokeContext;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.allinfinance.dev.rpc.scaffold.log.MDCUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author huanghf
 * @date 2023/12/12 17:18
 */
@Extension(value = "traceIdConsumerFilter", order = -19900)
@AutoActive(consumerSide = true)
public class TraceIdConsumerFilter extends Filter {
    @Override
    public SofaResponse invoke(FilterInvoker filterInvoker, SofaRequest sofaRequest) throws SofaRpcException {
        String tid = MDCUtils.getTid();
        Map<String, String> headerMap = RpcInvokeContext.getContext().getCustomHeader();
        if (StringUtils.isNotBlank(tid) && StringUtils.isBlank(headerMap.get(MDCUtils.TRACE_ID))) {
            headerMap.put(MDCUtils.TRACE_ID, tid);
        }
        return filterInvoker.invoke(sofaRequest);
    }
}
