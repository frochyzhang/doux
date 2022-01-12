package com.allinfinance.dev.ccs.security.handler;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.respdto.LoginSeccessRespDto;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.core.util.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: liuqi
 * @Description: 登录成功处理逻辑
 * @Date Create in 2021/5/15 15:52
 */
@Component
public class AosAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(AosAuthenticationSuccessHandler.class);

    @Autowired
    TblUserService tbUserService;
    @Autowired
    private TblRoleService tblRoleService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TblUser tbluser = tbUserService.selectCurrentUser(userDetails.getUsername());
        tbluser.setLastLoginTime(new Date());
        tbluser.setUpdateBy(tbluser.getUserName());
        tbUserService.updateByPrimaryKey(tbluser);
        //返回json数据
        LoginSeccessRespDto successReapDto = new LoginSeccessRespDto();
        successReapDto.setCurrentAuthority(new String[]{tbluser.getRoleId()});
        successReapDto.setPassStatus(tbluser.getPassStatus());
        successReapDto.setUserName(tbluser.getUserName());
        successReapDto.setOrg(tbluser.getOrg());
        successReapDto.setIsFirstLogin(tbluser.getReservedField1());
        successReapDto.setUserId(tbluser.getUserId());
        successReapDto.setOptFlag(tbluser.getOtpFlag());
        Result result = Result.success(successReapDto);

        ObjectMapper objectMapper = new ObjectMapper();
        if (tbluser.getOtpFlag().equals(AosContent.IS_USE_N)) {
            TblRole tblRole = tblRoleService.selectByPrimaryKey(tbluser.getRoleId());
            String sign = JwtUtil.sign(tbluser.getUserName(), String.valueOf(tbluser.getUserId()), tbluser.getRoleId(), tbluser.getOrg(), tblRole.getWeight());
            httpServletResponse.setHeader("Access-control-Expose-Headers", AosContent.AOS_TOKEN);
            httpServletResponse.setHeader(AosContent.AOS_TOKEN, sign);
        }
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
