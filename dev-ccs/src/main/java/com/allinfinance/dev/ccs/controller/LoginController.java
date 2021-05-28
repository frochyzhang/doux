package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.respdto.QrCodeResDto;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.AosAuthenticationSuccessHandler;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    TblUserService userService;
    @RequestMapping(path = "login/reLogin" ,method = RequestMethod.POST)
    @ResponseBody
    public Result getMenusList(@RequestBody SecondCheckPassVo checkPassVo,HttpServletRequest request){
        logger.info("接受到的参数:userName-->{},checkCode-->{}", checkPassVo.getUserName(), checkPassVo.getCheckCode());
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        logger.info("获取当前用户信息:userId-->{}", userId);
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        if(StringUtils.isBlank(checkPassVo.getCheckCode())){
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        try {
            boolean validate = GoogleAuthenticator.validateCurrentNumber(this.secret, Integer.valueOf(checkPassVo.getCheckCode()), -1);
            if(!validate){
                return Result.failure(ResultCodeEnum.USER_ACCOUNT_ODEERROR);
            }
        } catch (GeneralSecurityException e) {
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        currentUser.setReservedField1(AosContent.IS_BIND);
        currentUser.setUpdateTime(new Date());
        currentUser.setUpdateBy("登陆邦定动态验证码成功更新");
        userService.updateByPrimaryKey(currentUser);
        return Result.success(currentUser);
    }


    @RequestMapping(path = "currentUser" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getCurrentUser(HttpServletRequest request){
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String username = JwtUtil.getUsername(token);
        String userId = JwtUtil.getUserId(token);
        logger.info("获取当前用户信息:userName-->{},userId-->{}", username,userId);
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        logger.info("获取当前用户信息:currentUser-->{}", currentUser.toString());
        return Result.success(currentUser);
    }

    @RequestMapping(path = "getQRCodeUrl" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getQRCodeUrl(HttpServletRequest request){
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        String userId = JwtUtil.getUserId(token);
        String org = JwtUtil.getOrg(token);
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        if(AosContent.IS_BIND.equals(currentUser.getReservedField1())){
            return Result.success(new QrCodeResDto());
         }
        String cuiwy = GoogleAuthenticator.generateOtpAuthUrl(userId, this.secret, this.issuer);
        String encodePath="";
        String qrcodePath=this.desePath + File.separator + userName + org;
        try {
            ClassPathResource classPathResource = new ClassPathResource("logo.png");
            String path = classPathResource.getPath();
            encodePath = QRCodeUtils.encode(cuiwy, path, qrcodePath, true);
        } catch (Exception e) {
            logger.error("生成二维码异常",e);
        }
        QrCodeResDto qrCodeResDto = new QrCodeResDto();
        qrCodeResDto.setQrCode(qrcodePath+ File.separator+encodePath);
        return Result.success(qrCodeResDto);
    }



    public static String desePath;

    public static String secret;

    public static String issuer;

    @Value("${qrCode.path:/home/aos/qrcode/}")
    public  void setDesePath(String desePath) {
        this.desePath = desePath;
    }
    @Value("${qrCode.secret:2wsx3edc4rfv5tgb2wsx3edc4rfv5tgb}")
    public  void setSecret(String secret) {
        this.secret = secret;
    }
    @Value("${qrCode.issuer:allinfinance}")
    public  void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}

