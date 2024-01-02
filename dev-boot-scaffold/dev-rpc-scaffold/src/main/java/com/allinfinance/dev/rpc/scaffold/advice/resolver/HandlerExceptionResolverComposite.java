package com.allinfinance.dev.rpc.scaffold.advice.resolver;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * A {@link HandlerExceptionResolver} that delegates to a list of other
 * {@link HandlerExceptionResolver HandlerExceptionResolvers}.
 *
 * @author huanghf
 */
public class HandlerExceptionResolverComposite implements HandlerExceptionResolver, Ordered {

	@Nullable
	private List<HandlerExceptionResolver> resolvers;

	private int order = Ordered.LOWEST_PRECEDENCE;


	/**
	 * Set the list of exception resolvers to delegate to.
	 */
	public void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		this.resolvers = exceptionResolvers;
	}

	/**
	 * Return the list of exception resolvers to delegate to.
	 */
	public List<HandlerExceptionResolver> getExceptionResolvers() {
		return (this.resolvers != null ? Collections.unmodifiableList(this.resolvers) : Collections.emptyList());
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}


	/**
	 * Resolve the exception by iterating over the list of configured exception resolvers.
	 * <p>The first one to return a {@link SofaResponse} wins. Otherwise {@code null} is returned.
	 */
	@Override
	@Nullable
	public SofaResponse resolveException(
			SofaRequest sofaRequest, ProviderConfig<?> providerConfig, Exception ex) {

		if (this.resolvers != null) {
			for (HandlerExceptionResolver handlerExceptionResolver : this.resolvers) {
				SofaResponse sofaResponse = handlerExceptionResolver.resolveException(sofaRequest, providerConfig, ex);
				if (sofaResponse != null) {
					return sofaResponse;
				}
			}
		}
		return null;
	}

}
