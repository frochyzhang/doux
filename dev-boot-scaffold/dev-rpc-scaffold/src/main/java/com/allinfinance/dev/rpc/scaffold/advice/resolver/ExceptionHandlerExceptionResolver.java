package com.allinfinance.dev.rpc.scaffold.advice.resolver;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.allinfinance.dev.common.dictionary.dto.SofaResponseDTO;
import com.allinfinance.dev.rpc.scaffold.advice.annotation.ExceptionHandlerMethodResolver;
import com.allinfinance.dev.rpc.scaffold.advice.annotation.ProviderAdvice;
import com.allinfinance.dev.rpc.scaffold.advice.annotation.ProviderAdviceBean;
import com.allinfinance.dev.rpc.scaffold.advice.handler.SofaInvocableHandlerMethod;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodArgumentResolver;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodArgumentResolverComposite;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodReturnValueHandler;
import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerMethodReturnValueHandlerComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link AbstractHandlerMethodExceptionResolver} that resolves exceptions
 * through {@code @ProviderExceptionHandler} methods.
 *
 * <p>Support for custom argument and return value types can be added via
 * {@link #setCustomArgumentResolvers} and {@link #setCustomReturnValueHandlers}.
 * Or alternatively to re-configure all argument and return value types use
 * {@link #setArgumentResolverComposite} and {@link #setReturnValueHandlerComposite(List)}.
 *
 * @author huanghf
 */
public class ExceptionHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver
        implements ApplicationContextAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerExceptionResolver.class);

    @Nullable
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;

    @Nullable
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    @Nullable
    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    @Nullable
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    @Nullable
    private ApplicationContext applicationContext;

    private final Map<Class<?>, ExceptionHandlerMethodResolver> exceptionHandlerCache =
            new ConcurrentHashMap<>(64);

    private final Map<ProviderAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();

    /**
     * Provide resolvers for custom argument types. Custom resolvers are ordered
     * after built-in ones. To override the built-in support for argument
     * resolution use {@link #setArgumentResolverComposite} instead.
     */
    public void setCustomArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
        this.customArgumentResolvers = argumentResolvers;
    }

    /**
     * Return the custom argument resolvers, or {@code null}.
     */
    @Nullable
    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return this.customArgumentResolvers;
    }

    /**
     * Configure the complete list of supported argument types thus overriding
     * the resolvers that would otherwise be configured by default.
     */
    public void setArgumentResolverComposite(@Nullable List<HandlerMethodArgumentResolver> argumentResolverComposite) {
        if (argumentResolverComposite == null) {
            this.argumentResolverComposite = null;
        } else {
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolvers(argumentResolverComposite);
        }
    }

    /**
     * Return the configured argument resolvers, or possibly {@code null} if
     * not initialized yet via {@link #afterPropertiesSet()}.
     */
    @Nullable
    public HandlerMethodArgumentResolverComposite getArgumentResolverComposite() {
        return this.argumentResolverComposite;
    }

    /**
     * Provide handlers for custom return value types. Custom handlers are
     * ordered after built-in ones. To override the built-in support for
     * return value handling use {@link #setReturnValueHandlerComposite}.
     */
    public void setCustomReturnValueHandlers(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        this.customReturnValueHandlers = returnValueHandlers;
    }

    /**
     * Return the custom return value handlers, or {@code null}.
     */
    @Nullable
    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return this.customReturnValueHandlers;
    }

    /**
     * Configure the complete list of supported return value types thus
     * overriding handlers that would otherwise be configured by default.
     */
    public void setReturnValueHandlerComposite(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlerComposite) {
        if (returnValueHandlerComposite == null) {
            this.returnValueHandlerComposite = null;
        } else {
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addHandlers(returnValueHandlerComposite);
        }
    }

    /**
     * Return the configured handlers, or possibly {@code null} if not
     * initialized yet via {@link #afterPropertiesSet()}.
     */
    @Nullable
    public HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        return this.returnValueHandlerComposite;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nullable
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }


    @Override
    public void afterPropertiesSet() {
        // Do this first
        initExceptionHandlerAdviceCache();

        if (this.argumentResolverComposite == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
        if (this.returnValueHandlerComposite == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }
    }

    private void initExceptionHandlerAdviceCache() {
        if (getApplicationContext() == null) {
            return;
        }

        List<ProviderAdviceBean> adviceBeans = ProviderAdviceBean.findAnnotatedBeans(getApplicationContext());
        for (ProviderAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ProviderAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            int adviceCacheSize = this.exceptionHandlerAdviceCache.size();
            if (adviceCacheSize == 0) {
                LOGGER.debug("ProviderAdvice beans: none");
            } else {
                LOGGER.debug("ProviderAdvice beans: " + adviceCacheSize + " @ExceptionHandler, ");
            }
        }
    }

    /**
     * Return an unmodifiable Map with the {@link ProviderAdvice @ProviderAdvice}
     * beans discovered in the ApplicationContext. The returned map will be empty if
     * the method is invoked before the bean has been initialized via
     * {@link #afterPropertiesSet()}.
     */
    public Map<ProviderAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache() {
        return Collections.unmodifiableMap(this.exceptionHandlerAdviceCache);
    }

    /**
     * Return the list of argument resolvers to use including built-in resolvers
     * and custom resolvers provided via {@link #setCustomArgumentResolvers}.
     */
    protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // Custom arguments
        if (getCustomArgumentResolvers() != null) {
            resolvers.addAll(getCustomArgumentResolvers());
        }

        // default argument resolver
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return SofaRequest.class.isAssignableFrom(parameter.getParameterType());
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, SofaRequest sofaRequest) throws Exception {
                return sofaRequest;
            }
        });
        return resolvers;
    }

    /**
     * Return the list of return value handlers to use including built-in and
     * custom handlers provided via {@link #setReturnValueHandlerComposite}.
     */
    protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        // Custom return value types
        if (getCustomReturnValueHandlers() != null) {
            handlers.addAll(getCustomReturnValueHandlers());
        }

        // default return value handler
        handlers.add(new HandlerMethodReturnValueHandler() {
            @Override
            public boolean supportsReturnType(MethodParameter returnType) {
                return SofaResponseDTO.class.isAssignableFrom(returnType.getParameterType());
            }

            @Override
            public SofaResponse handleReturnValue(Object returnValue, MethodParameter returnType, SofaRequest sofaRequest) throws Exception {
                SofaResponse sofaResponse = new SofaResponse();
                sofaResponse.setAppResponse(returnValue);
                return sofaResponse;
            }
        });

        return handlers;
    }


    /**
     * Find an {@code @ProviderExceptionHandler} method and invoke it to handle the raised exception.
     */
    @Override
    @Nullable
    protected SofaResponse doResolveHandlerMethodException(SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception exception) {

        SofaInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(providerConfig, exception);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        if (this.argumentResolverComposite != null) {
            exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolverComposite);
        }
        if (this.returnValueHandlerComposite != null) {
            exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlerComposite);
        }

        SofaResponse sofaResponse;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Using @ProviderExceptionHandler " + exceptionHandlerMethod);
            }
            Throwable cause = exception.getCause();
            if (cause != null) {
                // Expose cause as provided argument as well
                sofaResponse = exceptionHandlerMethod.invokeAndHandle(sofaRequest, exception, cause);
            } else {
                // Otherwise, just the given exception as-is
                sofaResponse = exceptionHandlerMethod.invokeAndHandle(sofaRequest, exception);
            }
        } catch (Throwable invocationEx) {
            // Any other than the original exception (or its cause) is unintended here,
            // probably an accident (e.g. failed assertion or the like).
            if (invocationEx != exception && invocationEx != exception.getCause() && LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failure in @ProviderExceptionHandler " + exceptionHandlerMethod, invocationEx);
            }
            // Continue with default processing of the original exception...
            return null;
        }

        return sofaResponse;
    }

    /**
     * Find an {@code @ProviderExceptionHandler} method for the given exception. The default
     * implementation searches methods in the class hierarchy of the controller first
     * and if not found, it continues searching for additional {@code @ExceptionHandler}
     * methods assuming some {@linkplain ProviderAdvice @ProviderAdvice}
     * Spring-managed beans were detected.
     *
     * @param providerConfig current provider config
     * @param exception      the raised exception
     * @return a method to handle the exception, or {@code null} if none
     */
    @Nullable
    protected SofaInvocableHandlerMethod getExceptionHandlerMethod(ProviderConfig<?> providerConfig, Exception exception) {
        for (Map.Entry<ProviderAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ProviderAdviceBean advice = entry.getKey();
            if (advice.isApplicableToBeanType(providerConfig.getRef().getClass())) {
                ExceptionHandlerMethodResolver resolver = entry.getValue();
                Method method = resolver.resolveMethod(exception);
                if (method != null) {
                    return new SofaInvocableHandlerMethod(advice.resolveBean(), method);
                }
            }
        }

        return null;
    }

}
