package cn.lezoo.doux.rpc.scaffold.filter;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.rpc.scaffold.advice.resolver.ExceptionHandlerExceptionResolver;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author huanghf
 * @date 2023/3/20 16:57
 */
@Extension(value = "providerAdvice", order = -19800)
@AutoActive(providerSide = true)
public class ProviderAdviceFilter extends Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderAdviceFilter.class);

    private final ExceptionHandlerExceptionResolver resolver;

    public ProviderAdviceFilter() {
        resolver = SpringUtil.getBean(ExceptionHandlerExceptionResolver.class);
    }

    @Override
    public boolean needToLoad(FilterInvoker invoker) {
        return invoker.getConfig() instanceof ProviderConfig;
    }

    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        SofaResponse sofaResponse = invoker.invoke(request);
        if (sofaResponse.getAppResponse() instanceof Exception) {
            return Optional.ofNullable(resolver.resolveException(request, (ProviderConfig<?>) invoker.getConfig(), (Exception) sofaResponse.getAppResponse()))
                    .orElse(sofaResponse);
        } else {
            return sofaResponse;
        }
    }
}
