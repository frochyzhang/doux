package com.allinfinance.dev.rpc.scaffold.advice.handler.support;

import com.alipay.sofa.rpc.core.request.SofaRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

/**
 * Strategy interface for resolving method parameters into argument values in
 * the context of a given request.
 *
 * @author huanghf
 * @see HandlerMethodReturnValueHandler
 */
public interface HandlerMethodArgumentResolver {

    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     *
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * Resolves a method parameter into an argument value from a given request.
     *
     * @param parameter   the method parameter to resolve. This parameter must
     *                    have previously been passed to {@link #supportsParameter} which must
     *                    have returned {@code true}.
     * @param sofaRequest the current request
     * @return the resolved argument value, or {@code null} if not resolvable
     * @throws Exception in case of errors with the preparation of argument values
     */
    @Nullable
    Object resolveArgument(MethodParameter parameter, SofaRequest sofaRequest) throws Exception;

}
