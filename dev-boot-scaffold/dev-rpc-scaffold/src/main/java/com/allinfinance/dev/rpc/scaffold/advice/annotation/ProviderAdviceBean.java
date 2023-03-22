package com.allinfinance.dev.rpc.scaffold.advice.annotation;

import com.allinfinance.dev.rpc.scaffold.advice.handler.support.HandlerTypePredicate;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information about an {@link ProviderAdviceBean @ProviderAdviceBean}
 * Spring-managed bean without necessarily requiring it to be instantiated.
 *
 * <p>The {@link #findAnnotatedBeans(ApplicationContext)} method can be used to
 * discover such beans. However, a {@code ProviderAdviceBean} may be created
 * from any object, including ones without an {@code @ProviderAdvice} annotation.
 *
 * @author huanghf
 */
public class ProviderAdviceBean implements Ordered {
    /**
     * Reference to the actual bean instance or a {@code String} representing
     * the bean name.
     */
    private final Object beanOrName;

    private final boolean isSingleton;

    /**
     * Reference to the resolved bean instance, potentially lazily retrieved
     * via the {@code BeanFactory}.
     */
    @Nullable
    private Object resolvedBean;

    @Nullable
    private final Class<?> beanType;

    private final HandlerTypePredicate beanTypePredicate;

    @Nullable
    private final BeanFactory beanFactory;

    @Nullable
    private Integer order;


    /**
     * Create a {@code ProviderAdviceBean} using the given bean instance.
     *
     * @param bean the bean instance
     */
    public ProviderAdviceBean(Object bean) {
        Assert.notNull(bean, "Bean must not be null");
        this.beanOrName = bean;
        this.isSingleton = true;
        this.resolvedBean = bean;
        this.beanType = ClassUtils.getUserClass(bean.getClass());
        this.beanTypePredicate = createBeanTypePredicate(this.beanType);
        this.beanFactory = null;
    }

    /**
     * Create a {@code ProviderAdviceBean} using the given bean name and
     * {@code BeanFactory}.
     *
     * @param beanName    the name of the bean
     * @param beanFactory a {@code BeanFactory} to retrieve the bean type initially
     *                    and later to resolve the actual bean
     */
    public ProviderAdviceBean(String beanName, BeanFactory beanFactory) {
        this(beanName, beanFactory, null);
    }

    /**
     * Create a {@code ProviderAdviceBean} using the given bean name,
     * {@code BeanFactory}, and {@link ProviderAdvice @ProviderAdvice}
     * annotation.
     *
     * @param beanName       the name of the bean
     * @param beanFactory    a {@code BeanFactory} to retrieve the bean type initially
     *                       and later to resolve the actual bean
     * @param providerAdvice the {@code @ProviderAdvice} annotation for the
     *                       bean, or {@code null} if not yet retrieved
     * @since 5.2
     */
    public ProviderAdviceBean(String beanName, BeanFactory beanFactory, @Nullable ProviderAdvice providerAdvice) {
        Assert.hasText(beanName, "Bean name must contain text");
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        Assert.isTrue(beanFactory.containsBean(beanName), () -> "BeanFactory [" + beanFactory +
                "] does not contain specified provider advice bean '" + beanName + "'");

        this.beanOrName = beanName;
        this.isSingleton = beanFactory.isSingleton(beanName);
        this.beanType = getBeanType(beanName, beanFactory);
        this.beanTypePredicate = (providerAdvice != null ? createBeanTypePredicate(providerAdvice) :
                createBeanTypePredicate(this.beanType));
        this.beanFactory = beanFactory;
    }


    /**
     * Get the order value for the contained bean.
     * <p>As of Spring Framework 5.2, the order value is lazily retrieved using
     * the following algorithm and cached. Note, however, that a
     * {@link ProviderAdvice @ProviderAdvice} bean that is configured as a
     * scoped bean &mdash; for example, as a request-scoped or session-scoped
     * bean &mdash; will not be eagerly resolved. Consequently, {@link Ordered} is
     * not honored for scoped {@code @ProviderAdvice} beans.
     * <ul>
     * <li>If the {@linkplain #resolveBean resolved bean} implements {@link Ordered},
     * use the value returned by {@link Ordered#getOrder()}.</li>
     * <li>If the {@linkplain #getBeanType() bean type} is known, use the value returned
     * by {@link OrderUtils#getOrder(Class, int)} with {@link Ordered#LOWEST_PRECEDENCE}
     * used as the default order value.</li>
     * <li>Otherwise use {@link Ordered#LOWEST_PRECEDENCE} as the default, fallback
     * order value.</li>
     * </ul>
     *
     * @see #resolveBean()
     */
    @Override
    public int getOrder() {
        if (this.order == null) {
            Object resolvedBean = null;
            if (this.beanFactory != null && this.beanOrName instanceof String) {
                String beanName = (String) this.beanOrName;
                String targetBeanName = ScopedProxyUtils.getTargetBeanName(beanName);
                boolean isScopedProxy = this.beanFactory.containsBean(targetBeanName);
                // Avoid eager @ProviderAdvice bean resolution for scoped proxies,
                // since attempting to do so during context initialization would result
                // in an exception due to the current absence of the scope. For example,
                // an HTTP request or session scope is not active during initialization.
                if (!isScopedProxy && !ScopedProxyUtils.isScopedTarget(beanName)) {
                    resolvedBean = resolveBean();
                }
            } else {
                resolvedBean = resolveBean();
            }

            if (resolvedBean instanceof Ordered) {
                this.order = ((Ordered) resolvedBean).getOrder();
            } else if (this.beanType != null) {
                this.order = OrderUtils.getOrder(this.beanType, Ordered.LOWEST_PRECEDENCE);
            } else {
                this.order = Ordered.LOWEST_PRECEDENCE;
            }
        }
        return this.order;
    }

    /**
     * Return the type of the contained bean.
     * <p>If the bean type is a CGLIB-generated class, the original user-defined
     * class is returned.
     */
    @Nullable
    public Class<?> getBeanType() {
        return this.beanType;
    }

    /**
     * Get the bean instance for this {@code ProviderAdviceBean}, if necessary
     * resolving the bean name through the {@link BeanFactory}.
     * <p>As of Spring Framework 5.2, once the bean instance has been resolved it
     * will be cached if it is a singleton, thereby avoiding repeated lookups in
     * the {@code BeanFactory}.
     */
    public Object resolveBean() {
        if (this.resolvedBean == null) {
            // this.beanOrName must be a String representing the bean name if
            // this.resolvedBean is null.
            Object resolvedBean = obtainBeanFactory().getBean((String) this.beanOrName);
            // Don't cache non-singletons (e.g., prototypes).
            if (!this.isSingleton) {
                return resolvedBean;
            }
            this.resolvedBean = resolvedBean;
        }
        return this.resolvedBean;
    }

    private BeanFactory obtainBeanFactory() {
        Assert.state(this.beanFactory != null, "No BeanFactory set");
        return this.beanFactory;
    }

    /**
     * Check whether the given bean type should be advised by this
     * {@code ProviderAdviceBean}.
     *
     * @param beanType the type of the bean to check
     * @see ProviderAdvice
     * @since 4.0
     */
    public boolean isApplicableToBeanType(@Nullable Class<?> beanType) {
        return this.beanTypePredicate.test(beanType);
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProviderAdviceBean)) {
            return false;
        }
        ProviderAdviceBean otherAdvice = (ProviderAdviceBean) other;
        return (this.beanOrName.equals(otherAdvice.beanOrName) && this.beanFactory == otherAdvice.beanFactory);
    }

    @Override
    public int hashCode() {
        return this.beanOrName.hashCode();
    }

    @Override
    public String toString() {
        return this.beanOrName.toString();
    }


    /**
     * Find beans annotated with {@link ProviderAdvice @ProviderAdvice} in the
     * given {@link ApplicationContext} and wrap them as {@code ProviderAdviceBean}
     * instances.
     * <p>As of Spring Framework 5.2, the {@code ProviderAdviceBean} instances
     * in the returned list are sorted using {@link OrderComparator#sort(List)}.
     *
     * @see #getOrder()
     * @see OrderComparator
     * @see Ordered
     */
    public static List<ProviderAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        List<ProviderAdviceBean> adviceBeans = new ArrayList<>();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, Object.class)) {
            if (!ScopedProxyUtils.isScopedTarget(name)) {
                ProviderAdvice providerAdvice = context.findAnnotationOnBean(name, ProviderAdvice.class);
                if (providerAdvice != null) {
                    // Use the @ProviderAdvice annotation found by findAnnotationOnBean()
                    // in order to avoid a subsequent lookup of the same annotation.
                    adviceBeans.add(new ProviderAdviceBean(name, context, providerAdvice));
                }
            }
        }
        OrderComparator.sort(adviceBeans);
        return adviceBeans;
    }

    @Nullable
    private static Class<?> getBeanType(String beanName, BeanFactory beanFactory) {
        Class<?> beanType = beanFactory.getType(beanName);
        return (beanType != null ? ClassUtils.getUserClass(beanType) : null);
    }

    private static HandlerTypePredicate createBeanTypePredicate(@Nullable Class<?> beanType) {
        ProviderAdvice providerAdvice = (beanType != null ?
                AnnotatedElementUtils.findMergedAnnotation(beanType, ProviderAdvice.class) : null);
        return createBeanTypePredicate(providerAdvice);
    }

    private static HandlerTypePredicate createBeanTypePredicate(@Nullable ProviderAdvice providerAdvice) {
        if (providerAdvice != null) {
            return HandlerTypePredicate.builder()
                    .basePackage(providerAdvice.basePackages())
                    .basePackageClass(providerAdvice.basePackageClasses())
                    .assignableType(providerAdvice.assignableTypes())
                    .annotation(providerAdvice.annotations())
                    .build();
        }
        return HandlerTypePredicate.forAnyHandlerType();
    }
}
