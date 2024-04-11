package com.allinfinance.dev.white.list.diversion.http.handler;

import cn.hutool.core.io.IoUtil;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import com.allinfinance.dev.white.list.diversion.http.client.ForwardClientService;
import com.allinfinance.dev.white.list.diversion.http.config.ForwardFlagContext;
import com.allinfinance.dev.white.list.diversion.http.config.RequestBodyRequestWrapper;
import com.allinfinance.dev.white.list.diversion.http.config.WhiteListConfig;
import com.allinfinance.dev.white.list.diversion.http.service.HttpRequestDTO;
import com.allinfinance.dev.white.list.diversion.http.service.WhiteListService;
import com.allinfinance.dev.white.list.diversion.http.util.RequestMsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author huanghf
 * @date 2024/3/20 20:15
 */
@Order(-1)
@Component
public class WhiteListHandlerMapping implements HandlerMapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteListHandlerMapping.class);

    private final WhiteListConfig whiteListConfig;
    private final WhiteListService whiteListService;
    private final ForwardClientService forwardClientService;

    public WhiteListHandlerMapping(WhiteListConfig whiteListConfig, @Autowired(required = false) WhiteListService whiteListService,
                                   ForwardClientService forwardClientService) {
        this.whiteListConfig = whiteListConfig;
        this.whiteListService = whiteListService;
        this.forwardClientService = forwardClientService;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (Boolean.TRUE.equals(ForwardFlagContext.getForwardFlag())) {
            return null;
        }
        if (!whiteListConfig.getOnFlag()) {
            LOGGER.info("白名单功能未开启");
            return null;
        }
        return new HandlerExecutionChain(new WhiteListHandler(), new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                try {
                    LOGGER.info("白名单功能已开启");
                    if (whiteListService == null) {
                        throw new RuntimeException("White list diversion required a bean of type 'com.allinfinance.dev.white.list.diversion.http.service.WhiteListService' that could not be found.");
                    }
                    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                    String body = IoUtil.read(requestWrapper.getReader());
                    Map<String, String> headers = RequestMsgUtils.getHeader(requestWrapper);
                    String requestURI = requestWrapper.getRequestURI();
                    if (requestURI.endsWith("/")) {
                        requestURI = requestURI.substring(0, requestURI.length() - 1);
                    }
                    String method = requestWrapper.getMethod();
                    Map<String, String> parameters = RequestMsgUtils.getParameters(requestWrapper);

                    HttpRequestDTO requestDTO = new HttpRequestDTO(requestURI, headers, body, method, parameters);
                    boolean isWhiteList = whiteListService.isWhiteList(requestDTO);
                    if (isWhiteList) {
                        LOGGER.info("在白名单中，转发到新服务");
                        HttpResponse forwardResponse = forwardClientService.forward(requestDTO, requestWrapper.getHeader("Content-Type"));
                        LOGGER.info("转发完成：响应内容：{}", forwardResponse);
                        forwardResponse.getHeaders().forEach((name, value) -> {
                            if (!"Transfer-Encoding".equalsIgnoreCase(name)) {
                                response.setHeader(name, value);
                            }
                        });
                        response.getWriter().write(forwardResponse.getResponse());
                        response.setStatus(forwardResponse.getHttpStatus());
                    } else {
                        LOGGER.info("不在白名单中，不进行转发");
                        ForwardFlagContext.setForwardFlag(true);
                        RequestBodyRequestWrapper requestBodyRequestWrapper = new RequestBodyRequestWrapper(requestWrapper);
                        requestBodyRequestWrapper.getRequestDispatcher(request.getRequestURI()).forward(requestBodyRequestWrapper, response);
                    }
                    return false;
                } finally {
                    ForwardFlagContext.clearForwardFlag();
                }
            }
        });
    }
}
