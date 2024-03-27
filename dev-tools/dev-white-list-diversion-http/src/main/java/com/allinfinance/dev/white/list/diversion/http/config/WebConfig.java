package com.allinfinance.dev.white.list.diversion.http.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghf
 * @date 2024/3/18 20:58
 */
// @Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                BackupFlagContext.clearBackupFlag();
                ForwardFlagContext.clearForwardFlag();
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            }
        });
    }
}
