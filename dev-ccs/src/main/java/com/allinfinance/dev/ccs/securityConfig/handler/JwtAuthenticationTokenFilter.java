package com.allinfinance.dev.ccs.securityConfig.handler;

import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
        String uri = request.getRequestURI();
        if("/login/account".equals(uri)){
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        if(StringUtils.isNotBlank(token)) {
            String username = JwtUtil.getUsername(token);
            String userId = JwtUtil.getUserId(token);
            if (JwtUtil.verify(token)) {
                log.info("用户名为：{}", username);
                if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    log.info("authenticated user:{}", username);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
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