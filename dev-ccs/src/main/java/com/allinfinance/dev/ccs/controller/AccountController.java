package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.content.RSAKeyProperties;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.respdto.QrCodeResDto;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import com.allinfinance.dev.ccs.utils.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
@RequestMapping("/account")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    TblUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public boolean checkOldPass(String userPass,String encodePass){
        logger.info("验证原始密码是否正确开始");
        String decPass;
        try {
             decPass = RSAUtils.decrypt(userPass);
        } catch (Exception e) {
            logger.info("密文解密异常！");
            return false;
        }
        if(passwordEncoder.matches(decPass,encodePass)){
            logger.info("原始正确");
            return true;
        }
        logger.info("原始错误！");
        return false;
    }



    @RequestMapping(path = "updateNewPass", method = RequestMethod.POST)
    @ResponseBody
    public Result updateNewPass(@RequestBody UpdatePasswordParam passwordParam, HttpServletRequest request) {
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        String newPassword = passwordParam.getNewPassword();
        TblUser tblUser = userService.selectByPrimaryKey(userId);
        if(StringUtils.isNotBlank(passwordParam.getOldPassword())){
            boolean b = checkOldPass(passwordParam.getOldPassword(), tblUser.getUserPass());
            if(!b){
                return Result.failure(ResultCodeEnum.OLD_USER_PASS_ERROR);
            }
        }
        if(StringUtils.isBlank(newPassword)){
            logger.error("新密码不能为空!");
            return Result.failure(ResultCodeEnum.PARAM_IS_BLANK);
        }
        String decryptPass = null;
        try {
            decryptPass = RSAUtils.decrypt(newPassword);
        } catch (Exception e) {
            logger.error("密文解密错误！",e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        tblUser.setUserPass(passwordEncoder.encode(decryptPass));
        tblUser.setPassStatus(AosContent.NOT_FIRST_PASS);
        tblUser.setLastPassUpdateTime(new Date());
        tblUser.setUpdateBy(username);
        try {
            userService.updateByPrimaryKey(tblUser);
        } catch (RuntimeException e) {
            logger.error("更新密码异常异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
}

