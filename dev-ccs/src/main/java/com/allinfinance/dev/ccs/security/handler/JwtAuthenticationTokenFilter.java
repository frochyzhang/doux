package com.allinfinance.dev.ccs.security.handler;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader( AosContent.AOS_TOKEN);
        //String refresh_token = request.getHeader( AosContent.AOS_REFRESH_TOKEN);
         String uri = request.getRequestURI();
         if("/login/account".equals(uri) ||"/getPublicKey".equals(uri) ){
            chain.doFilter(request, response);
            return;
        }
//        if(StringUtils.isBlank(token)){
//            //如果刷新的token不存在 也过期了表示用户长时间未操作
//            if(StringUtils.isBlank(refresh_token) ||!JwtUtil.verify(token)){
//                response.setStatus(HttpServletResponse.SC_OK);
//                Result result = Result.failure(ResultCodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
//                ObjectMapper objectMapper = new ObjectMapper();
//                response.setContentType("text/json;charset=utf-8");
//                response.getWriter().write(objectMapper.writeValueAsString(result));
//                return ;
//            }
//        }
        if(StringUtils.isNotBlank(token)) {
            String username = JwtUtil.getUsername(token);
            String org = JwtUtil.getOrg(token);
            String userId = JwtUtil.getUserId(token);
            String roleId = JwtUtil.getRole(token);
            if(JwtUtil.isJwtExpired(token)){
                response.setStatus(HttpServletResponse.SC_OK);
                Result result = Result.failure(ResultCodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return ;
            }
            if(JwtUtil.isWillExpired(token)){
                String sign = JwtUtil.sign(username, userId, roleId, org);
                response.setHeader("Access-control-Expose-Headers",AosContent.AOS_TOKEN);
                response.setHeader(AosContent.AOS_TOKEN,sign);
            }
            if (JwtUtil.verify(token)) {
                log.info("用户名为：{}", username);
                 if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    Result result = Result.failure(ResultCodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(objectMapper.writeValueAsString(result));
                    return ;
                }
            }else{
                response.setStatus(HttpServletResponse.SC_OK);
                Result result = Result.failure(ResultCodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return ;
            }
        }else{
            response.setStatus(HttpServletResponse.SC_OK);
            Result result = Result.failure(ResultCodeEnum.USER_NOT_LOGGED_IN);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return ;
        }
        chain.doFilter(request, response);
    }
}