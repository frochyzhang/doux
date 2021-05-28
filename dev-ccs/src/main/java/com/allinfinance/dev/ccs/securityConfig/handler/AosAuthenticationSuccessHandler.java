package com.allinfinance.dev.ccs.securityConfig.handler;


import ch.qos.logback.core.util.FileUtil;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.respdto.LoginSeccessReapDto;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TblUser tbluser = tbUserService.selectCurrentUser(userDetails.getUsername());
        tbluser.setLastLoginTime(new Date());
        tbluser.setUpdateBy(tbluser.getUserName());
        tbUserService.updateByPrimaryKey(tbluser);
         //返回json数据
        LoginSeccessReapDto seccessReapDto = new LoginSeccessReapDto();
        seccessReapDto.setCurrentAuthority(new String[]{tbluser.getRoleId()});
        seccessReapDto.setPassStatus(tbluser.getPassStatus());
        seccessReapDto.setUserName(tbluser.getUserName());
        seccessReapDto.setOrg(tbluser.getOrg());
        seccessReapDto.setIsFirstLogin(tbluser.getReservedField1());
        seccessReapDto.setToken(JwtUtil.sign(tbluser.getUserName(), String.valueOf(tbluser.getUserId()), tbluser.getRoleId(),tbluser.getOrg()));
        Result result = Result.success(seccessReapDto);
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
