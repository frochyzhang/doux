package com.allinfinance.dev.white.list.diversion.http.handler;

import cn.hutool.core.io.IoUtil;
import com.allinfinance.dev.common.util.common.DateUtils;
import com.allinfinance.dev.white.list.diversion.http.backup.BackupInfo;
import com.allinfinance.dev.white.list.diversion.http.backup.MessageInfo;
import com.allinfinance.dev.white.list.diversion.http.backup.TradeDirection;
import com.allinfinance.dev.white.list.diversion.http.config.BackupFlagContext;
import com.allinfinance.dev.white.list.diversion.http.config.RequestBodyRequestWrapper;
import com.allinfinance.dev.white.list.diversion.http.config.TrafficBackupConfig;
import com.allinfinance.dev.white.list.diversion.http.service.HttpRequestDTO;
import com.allinfinance.dev.white.list.diversion.http.service.TrafficBackupService;
import com.allinfinance.dev.white.list.diversion.http.util.RequestMsgUtils;
import com.allinfinance.dev.white.list.diversion.http.util.TrafficBackupUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author huanghf
 * @date 2024/3/26 17:33
 */
@Order(-2)
@Component
public class TrafficBackupHandlerMapping implements HandlerMapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficBackupHandlerMapping.class);

    private final TrafficBackupConfig trafficBackupConfig;
    private final TrafficBackupService trafficBackupService;

    public TrafficBackupHandlerMapping(TrafficBackupConfig trafficBackupConfig, @Autowired(required = false) TrafficBackupService trafficBackupService) {
        this.trafficBackupConfig = trafficBackupConfig;
        this.trafficBackupService = trafficBackupService;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (Boolean.TRUE.equals(BackupFlagContext.getBackupFlag())) {
            return null;
        }
        if (!trafficBackupConfig.getOnFlag()) {
            LOGGER.info("流量备份功能未开启");
            return null;
        }
        return new HandlerExecutionChain(new TrafficBackupHandler(), new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                try {
                    LOGGER.info("流量备份功能已开启");
                    if (trafficBackupService == null) {
                        throw new RuntimeException("Traffic backup required a bean of type 'com.allinfinance.dev.white.list.diversion.http.service.TrafficBackupService' that could not be found.");
                    }
                    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                    String body = IoUtil.read(requestWrapper.getReader());
                    Map<String, String> headers = RequestMsgUtils.getHeader(requestWrapper);
                    String requestURI = requestWrapper.getRequestURI();
                    String method = requestWrapper.getMethod();
                    Map<String, String> parameters = RequestMsgUtils.getParameters(requestWrapper);
                    HttpRequestDTO httpRequestDTO = new HttpRequestDTO(requestURI, headers, body, method, parameters);

                    MessageInfo messageInfo = new MessageInfo()
                            .setMethod(method)
                            .setUrl(requestURI)
                            .setHeader(headers)
                            .setData(body);
                    BackupInfo backupInfo = new BackupInfo()
                            .setTradeId(trafficBackupService.getTradeId(httpRequestDTO))
                            .setTradeDirection(TradeDirection.IN)
                            .setTimeStamp(DateUtils.getCurrentDateTime())
                            .setSource(trafficBackupService.getSource(httpRequestDTO))
                            .setDestination(trafficBackupService.getDestination(httpRequestDTO))
                            .setMessage(messageInfo);
                    TrafficBackupUtils.writeLog(backupInfo);

                    BackupFlagContext.setBackupFlag(true);
                    RequestBodyRequestWrapper requestBodyRequestWrapper = new RequestBodyRequestWrapper(requestWrapper);
                    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
                    requestBodyRequestWrapper.getRequestDispatcher(requestURI).forward(requestBodyRequestWrapper, responseWrapper);
                    messageInfo.setData(new String(responseWrapper.getContentAsByteArray()));
                    if (responseWrapper.getContentSize() > 0) {
                        responseWrapper.copyBodyToResponse();
                    }
                    backupInfo.setTradeDirection(TradeDirection.OUT)
                            .setTimeStamp(DateUtils.getCurrentDateTime());
                    TrafficBackupUtils.writeLog(backupInfo);
                    return false;
                } finally {
                    BackupFlagContext.clearBackupFlag();
                }
            }
        });
    }
}
