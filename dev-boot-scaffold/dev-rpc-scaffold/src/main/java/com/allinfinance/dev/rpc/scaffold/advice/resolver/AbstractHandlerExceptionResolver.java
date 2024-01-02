package com.allinfinance.dev.rpc.scaffold.advice.resolver;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Abstract base class for {@link HandlerExceptionResolver} implementations.
 *
 * <p>Supports mapped {@linkplain #setMappedHandlers handlers} and
 * {@linkplain #setMappedHandlerClasses handler classes} that the resolver
 * should be applied to and implements the {@link Ordered} interface.
 *
 * @author huanghf
 */
public abstract class AbstractHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandlerExceptionResolver.class);

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";

    private int order = Ordered.LOWEST_PRECEDENCE;

    @Nullable
    private Set<?> mappedHandlers;

    @Nullable
    private Class<?>[] mappedHandlerClasses;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    /**
     * Specify the set of handlers that this exception resolver should apply to.
     * <p>The exception mappings and the default error view will only apply to the specified handlers.
     * <p>If no handlers or handler classes are set, the exception mappings and the default error
     * view will apply to all handlers. This means that a specified default error view will be used
     * as a fallback for all exceptions; any further HandlerExceptionResolvers in the chain will be
     * ignored in this case.
     */
    public void setMappedHandlers(Set<?> mappedHandlers) {
        this.mappedHandlers = mappedHandlers;
    }

    /**
     * Specify the set of classes that this exception resolver should apply to.
     * <p>The exception mappings and the default error view will only apply to handlers of the
     * specified types; the specified types may be interfaces or superclasses of handlers as well.
     * <p>If no handlers or handler classes are set, the exception mappings and the default error
     * view will apply to all handlers. This means that a specified default error view will be used
     * as a fallback for all exceptions; any further HandlerExceptionResolvers in the chain will be
     * ignored in this case.
     */
    public void setMappedHandlerClasses(Class<?>... mappedHandlerClasses) {
        this.mappedHandlerClasses = mappedHandlerClasses;
    }

    /**
     * Check whether this resolver is supposed to apply (i.e. if the supplied handler
     * matches any of the configured {@linkplain #setMappedHandlers handlers} or
     * {@linkplain #setMappedHandlerClasses handler classes}), and then delegate
     * to the {@link #doResolveException} template method.
     */
    @Override
    @Nullable
    public SofaResponse resolveException(
            SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex) {

        SofaResponse result = doResolveException(sofaRequest, providerConfig, ex);
        if (result != null) {
            // Print debug message when warn logger is not enabled.
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(buildLogMessage(ex, sofaRequest) + (result.getAppResponse() == null ? "" : " to " + result));
            }
            // Explicitly configured warn logger in logException method.
            logException(ex, sofaRequest);
        }
        return result;
    }

    /**
     * Check whether this resolver is supposed to apply to the given handler.
     * <p>The default implementation checks against the configured
     * {@linkplain #setMappedHandlers handlers} and
     * {@linkplain #setMappedHandlerClasses handler classes}, if any.
     *
     * @param request current HTTP request
     * @param handler the executed handler, or {@code null} if none chosen
     *                at the time of the exception (for example, if multipart resolution failed)
     * @return whether this resolved should proceed with resolving the exception
     * for the given request and handler
     * @see #setMappedHandlers
     * @see #setMappedHandlerClasses
     */
    protected boolean shouldApplyTo(SofaRequest request, @Nullable Object handler) {
        if (handler != null) {
            if (this.mappedHandlers != null && this.mappedHandlers.contains(handler)) {
                return true;
            }
            if (this.mappedHandlerClasses != null) {
                for (Class<?> handlerClass : this.mappedHandlerClasses) {
                    if (handlerClass.isInstance(handler)) {
                        return true;
                    }
                }
            }
        }
        // Else only apply if there are no explicit handler mappings.
        return (this.mappedHandlers == null && this.mappedHandlerClasses == null);
    }

    /**
     * Log the given exception at warn level
     *
     * @param ex      the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @see #buildLogMessage
     */
    protected void logException(Exception ex, SofaRequest request) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(buildLogMessage(ex, request));
        }
    }

    /**
     * Build a log message for the given exception, occurred during processing the given request.
     *
     * @param ex      the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @return the log message to use
     */
    protected String buildLogMessage(Exception ex, SofaRequest request) {
        return "Resolved [" + LogFormatUtils.formatValue(ex, -1, true) + "]";
    }

    /**
     * Actually resolve the given exception that got thrown during handler execution,
     * returning a {@link SofaResponse} that represents a specific error page if appropriate.
     * <p>May be overridden in subclasses, in order to apply specific exception checks.
     * Note that this template method will be invoked <i>after</i> checking whether this
     * resolved applies ("mappedHandlers" etc), so an implementation may simply proceed
     * with its actual exception handling.
     *
     * @param sofaRequest    current sofa request
     * @param providerConfig current provider config
     * @param ex             the exception that got thrown during handler execution
     * @return a corresponding {@code SofaResponse} to forward to,
     * or {@code null} for default processing in the resolution chain
     */
    @Nullable
    protected abstract SofaResponse doResolveException(SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex);

}
