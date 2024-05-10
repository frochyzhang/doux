package cn.lezoo.doux.rpc.scaffold.advice.handler.support;

import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

/**
 * Strategy interface to handle the value returned from the invocation of a
 * handler method .
 *
 * @author huanghf
 * @see HandlerMethodArgumentResolver
 */
public interface HandlerMethodReturnValueHandler {

    /**
     * Whether the given {@linkplain MethodParameter method return type} is
     * supported by this handler.
     *
     * @param returnType the method return type to check
     * @return {@code true} if this handler supports the supplied return type;
     * {@code false} otherwise
     */
    boolean supportsReturnType(MethodParameter returnType);

    /**
     * Handle the given return value by adding attributes to the model and
     * setting a view or setting the
     *
     * @param returnValue the value returned from the handler method
     * @param returnType  the type of the return value. This type must have
     *                    previously been passed to {@link #supportsReturnType} which must
     *                    have returned {@code true}.
     * @param sofaRequest the current request
     * @throws Exception if the return value handling results in an error
     */
    SofaResponse handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                                   SofaRequest sofaRequest) throws Exception;

}
