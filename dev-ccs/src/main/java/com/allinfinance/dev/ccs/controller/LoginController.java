package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.content.RSAKeyProperties;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.respdto.OrgResultDto;
import com.allinfinance.dev.ccs.dal.respdto.QrCodeResDto;
import com.allinfinance.dev.ccs.dal.service.TblBankService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.AosAuthenticationSuccessHandler;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.net.BCodec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

    @Autowired
    @Qualifier(value = "rsaKeyProperties")
    private RSAKeyProperties rsaProperties;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
            String secret=currentUser.getReservedField2();
            int code=Integer.valueOf(checkPassVo.getCheckCode());
            boolean validate = GoogleAuthenticator.validateCurrentNumber(secret, code, -1);
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
        currentUser.setUserPass("[PROTOC]");
        logger.info("获取当前用户信息:currentUser-->{}", currentUser.toString());
        return Result.success(currentUser);
    }

    @RequestMapping(path = "getQRCodeUrl" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getQRCodeUrl(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        String userId = JwtUtil.getUserId(token);
        String org = JwtUtil.getOrg(token);
        QrCodeResDto qrCodeResDto = new QrCodeResDto();
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        if(AosContent.IS_BIND.equals(currentUser.getReservedField1())){
          return Result.success(qrCodeResDto);
         }
        String secret=currentUser.getReservedField2();
        String issuer=currentUser.getReservedField3();
        String cuiwy = GoogleAuthenticator.generateOtpAuthUrl(userName,secret ,issuer);
        String encodePath="";
        String qrcodePath=this.desePath + File.separator + userName;
        String stringImg="";
        try {
            encodePath = QRCodeUtils.encode(cuiwy, null, qrcodePath, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            response.setHeader("Content-type","image/jpg");
            File file = new File(qrcodePath+File.separator+encodePath);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bit=new byte[1024];
            int len=0;
            while ((len=inputStream.read(bit))!=-1){
                outputStream.write(bit,0,len);
            }
             stringImg = "data:image/gif;base64,"+ Base64.getEncoder().encodeToString(outputStream.toByteArray());
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            logger.error("生成二维码异常",e);
        }
        qrCodeResDto.setQrCode(stringImg);
        return Result.success(qrCodeResDto);
    }
    @RequestMapping(path = "getPublicKey" ,method = RequestMethod.POST)
    @ResponseBody
    public Result getPublicKey(HttpServletRequest request){
        return Result.success(rsaProperties.getPublicKey().getEncoded());
    }

    private static String desePath;
    @Value("${qrCode.path:/home/aos/qrcode/}")
    public  void setDesePath(String desePath) {
        this.desePath = desePath;
    }
}

