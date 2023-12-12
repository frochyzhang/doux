package com.allinfinance.dev.rpc.scaffold.filter;

import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.allinfinance.dev.rpc.scaffold.log.MDCUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huanghf
 * @date 2023/12/12 16:19
 */
@Extension(value = "traceIdProviderFilter", order = -19900)
@AutoActive(providerSide = true)
public class TraceIdProviderFilter extends Filter {
    @Override
    public SofaResponse invoke(FilterInvoker filterInvoker, SofaRequest sofaRequest) throws SofaRpcException {
        try {
            String tid = (String) sofaRequest.getRequestProp(MDCUtils.TRACE_ID);
            if (StringUtils.isNotBlank(tid)) {
                MDCUtils.addTid(tid);
            } else {
                MDCUtils.addTid();
            }
            return filterInvoker.invoke(sofaRequest);
        } finally {
            MDCUtils.removeTid();
        }
    }
}
