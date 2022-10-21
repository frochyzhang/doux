package com.allinfinance.dev.ccs.security.handler;

import com.allinfinance.dev.ccs.exception.TokenExpiredExeption;
import com.allinfinance.dev.common.dictionary.result.Result;
import com.allinfinance.dev.common.dictionary.result.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: liuqi
 * @Description: 登录失败处理逻辑
 * @Date Create in 2021/5/15 15:52
 */
@Component
public class AosAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        Result result = null;
        if (e instanceof BadCredentialsException) {
            //密码错误
            result = Result.failure(ResultCodeEnum.USER_LOGIN_ERROR);
        } else if (e instanceof CredentialsExpiredException) {
            //未授权
            result = Result.failure(ResultCodeEnum.USER_CREDENTIALS_EXPIRED);
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = Result.failure(ResultCodeEnum.USER_ACCOUNT_FORBIDDEN);
        }  else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = Result.failure(ResultCodeEnum.USER_NOT_EXIST);
        } else if (e instanceof TokenExpiredExeption) {
            //用户不存在
            result = Result.failure(ResultCodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
        }else{
            //其他错误
            result = Result.failure(ResultCodeEnum.SYSTEM_ERROR);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
