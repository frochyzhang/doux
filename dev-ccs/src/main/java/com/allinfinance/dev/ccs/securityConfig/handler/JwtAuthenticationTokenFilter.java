package com.allinfinance.dev.ccs.securityConfig.handler;

import com.allinfinance.dev.ccs.exception.TokenExpiredExeption;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(StringUtils.hasLength(token)) {
            String username = JwtUtil.getUsername(token);
            String userId = JwtUtil.getUserId(token);
            log.info("用户名为：{}", username);
            if(StringUtils.hasLength(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if(JwtUtil.verify(token)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }else{
            Jws<Claims> claimsJws = JwtUtil.psrserAuthenticteToken(token);
            boolean jwtExpired = JwtUtil.isJwtExpired(claimsJws);
            if(!jwtExpired){
                JwtUtil.setJwtExpired(claimsJws);
            }else{
                throw  new TokenExpiredExeption("token过期");
            }
       }
        chain.doFilter(request, response);
    }
}