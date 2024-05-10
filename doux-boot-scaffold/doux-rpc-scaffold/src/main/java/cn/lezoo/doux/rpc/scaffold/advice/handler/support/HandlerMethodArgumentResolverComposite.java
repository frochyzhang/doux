package cn.lezoo.doux.rpc.scaffold.advice.handler.support;

import com.alipay.sofa.rpc.core.request.SofaRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves method parameters by delegating to a list of registered
 * {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}.
 * Previously resolved method parameters are cached for faster lookups.
 *
 * @author huanghf
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);


    /**
     * Add the given {@link HandlerMethodArgumentResolver}.
     */
    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }

    /**
     * Add the given {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}.
     */
    public HandlerMethodArgumentResolverComposite addResolvers(
            @Nullable HandlerMethodArgumentResolver... resolvers) {

        if (resolvers != null) {
            Collections.addAll(this.argumentResolvers, resolvers);
        }
        return this;
    }

    /**
     * Add the given {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}.
     */
    public HandlerMethodArgumentResolverComposite addResolvers(
            @Nullable List<? extends HandlerMethodArgumentResolver> resolvers) {

        if (resolvers != null) {
            this.argumentResolvers.addAll(resolvers);
        }
        return this;
    }

    /**
     * Return a read-only list with the contained resolvers, or an empty list.
     */
    public List<HandlerMethodArgumentResolver> getResolvers() {
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    /**
     * Clear the list of configured resolvers.
     */
    public void clear() {
        this.argumentResolvers.clear();
    }


    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by any registered {@link HandlerMethodArgumentResolver}.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return getArgumentResolver(parameter) != null;
    }

    /**
     * Iterate over registered
     * {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}
     * and invoke the one that supports it.
     *
     * @throws IllegalArgumentException if no suitable argument resolver is found
     */
    @Override
    @Nullable
    public Object resolveArgument(MethodParameter parameter, SofaRequest sofaRequest) throws Exception {

        HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("Unsupported parameter type [" +
                    parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
        }
        return resolver.resolveArgument(parameter, sofaRequest);
    }

    /**
     * Find a registered {@link HandlerMethodArgumentResolver} that supports
     * the given method parameter.
     */
    @Nullable
    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
                if (resolver.supportsParameter(parameter)) {
                    result = resolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }

}

