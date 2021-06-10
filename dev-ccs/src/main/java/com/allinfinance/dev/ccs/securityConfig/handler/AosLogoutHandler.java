package com.allinfinance.dev.ccs.securityConfig.handler;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class AosLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String token = httpServletRequest.getHeader( AosContent.AOS_TOKEN);
        SecurityContextHolder.clearContext();
        //User details = (User) authentication.getDetails();
        //生成一个新的token  防止 通过其他方式伪造访问
        //JwtUtil.sign(JwtUtil.getUsername(token), JwtUtil.getRole(token), JwtUtil.getUserId(token), JwtUtil.getOrg(token));

    }
}
