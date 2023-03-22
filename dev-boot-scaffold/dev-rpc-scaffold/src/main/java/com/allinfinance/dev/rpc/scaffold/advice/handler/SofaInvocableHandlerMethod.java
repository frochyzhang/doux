package com.allinfinance.dev.rpc.scaffold.advice.handler;

import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodReturnValueHandler;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodReturnValueHandlerComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Extends {@link InvocableHandlerMethod} with the ability to handle return
 * values through a registered {@link HandlerMethodReturnValueHandler} and
 * also supports setting the response status based on a method-level
 * {@code @ResponseStatus} annotation.
 *
 * <p>A {@code null} return value (including void) may be interpreted as the
 * end of request processing in combination with a {@code @ResponseStatus}
 * annotation, a not-modified check condition
 *
 * @author huanghf
 */
public class SofaInvocableHandlerMethod extends InvocableHandlerMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(SofaInvocableHandlerMethod.class);

    private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call");

    @Nullable
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    /**
     * Creates an instance from the given handler and method.
     */
    public SofaInvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    /**
     * Create an instance from a {@code HandlerMethod}.
     */
    public SofaInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }


    /**
     * Register {@link HandlerMethodReturnValueHandler} instances to use to
     * handle return values.
     */
    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }


    /**
     * Invoke the method and handle the return value through one of the
     * configured {@link HandlerMethodReturnValueHandler HandlerMethodReturnValueHandlers}.
     *
     * @param sofaRequest  the current request
     * @param providedArgs "given" arguments matched by type (not resolved)
     */
    public SofaResponse invokeAndHandle(SofaRequest sofaRequest, Object... providedArgs) throws Exception {

        Object returnValue = invokeForRequest(sofaRequest, providedArgs);
        Assert.state(this.returnValueHandlers != null, "No return value handlers");
        try {
            return this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue), sofaRequest);
        } catch (Exception ex) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(formatErrorForReturnValue(returnValue), ex);
            }
            throw ex;
        }
    }

    private String formatErrorForReturnValue(@Nullable Object returnValue) {
        return "Error handling return value=[" + returnValue + "]" +
                (returnValue != null ? ", type=" + returnValue.getClass().getName() : "") +
                " in " + toString();
    }
}
