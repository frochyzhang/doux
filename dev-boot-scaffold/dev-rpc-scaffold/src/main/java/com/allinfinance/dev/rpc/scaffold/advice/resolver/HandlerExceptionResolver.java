package com.allinfinance.dev.rpc.scaffold.advice.resolver;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import org.springframework.lang.Nullable;

/**
 * Interface to be implemented by objects that can resolve exceptions thrown during
 * handler mapping or execution, in the typical case to error views. Implementors are
 * typically registered as beans in the application context.
 *
 * <p>Error views are analogous to JSP error pages but can be used with any kind of
 * exception including any checked exception, with potentially fine-grained mappings for
 * specific handlers.
 *
 * @author huanghf
 */
public interface HandlerExceptionResolver {

    /**
     * Try to resolve the given exception that got thrown during handler execution,
     * returning a {@link SofaResponse} that represents a specific error page if appropriate.
     *
     * @param sofaRequest    current sofa request
     * @param providerConfig current provider config
     * @param ex             the exception that got thrown during handler execution
     * @return a corresponding {@code SofaResponse} to forward to,
     * or {@code null} for default processing in the resolution chain
     */
    @Nullable
    SofaResponse resolveException(SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex);

}
