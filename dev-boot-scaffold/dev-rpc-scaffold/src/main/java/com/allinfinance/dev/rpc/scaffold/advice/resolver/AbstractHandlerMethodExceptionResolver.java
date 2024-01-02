package com.allinfinance.dev.rpc.scaffold.advice.resolver;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.allinfinance.dev.rpc.scaffold.advice.handler.HandlerMethod;
import org.springframework.lang.Nullable;

/**
 * Abstract base class for
 * {@link HandlerExceptionResolver HandlerExceptionResolver}
 * implementations that support handling exceptions from handlers of type {@link HandlerMethod}.
 *
 * @author huanghf
 */
public abstract class AbstractHandlerMethodExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    @Nullable
    protected final SofaResponse doResolveException(SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex) {

        return doResolveHandlerMethodException(sofaRequest, providerConfig, ex);
    }

    /**
     * Actually resolve the given exception that got thrown during on handler execution,
     * returning a ModelAndView that represents a specific error page if appropriate.
     * <p>May be overridden in subclasses, in order to apply specific exception checks.
     * Note that this template method will be invoked <i>after</i> checking whether this
     * resolved applies ("mappedHandlers" etc), so an implementation may simply proceed
     * with its actual exception handling.
     *
     * @param sofaRequest    current sofa request
     * @param providerConfig current provider config
     * @param ex             the exception that got thrown during handler execution
     * @return a corresponding SofaResponse to forward to, or {@code null} for default processing
     */
    @Nullable
    protected abstract SofaResponse doResolveHandlerMethodException(
            SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex);

}
