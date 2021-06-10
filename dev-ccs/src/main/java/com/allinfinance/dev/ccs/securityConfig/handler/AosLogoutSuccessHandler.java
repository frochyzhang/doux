package com.allinfinance.dev.ccs.securityConfig.handler;

import com.allinfinance.dev.ccs.dal.respdto.LogoutSeccessReapDto;
import com.allinfinance.dev.ccs.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: liuqi
 * @Description: 登出成功处理逻辑
 * @Date Create in 2021/5/15 10:17
 */
@Component
public class AosLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Result result = Result.success();
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
