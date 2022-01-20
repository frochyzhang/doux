package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.respdto.QrCodeResDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.utils.cas.CASUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Base64;
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
    private static String desePath;
    @Autowired
    TblUserService userService;
    @Autowired
    TblRoleService roleService;


    @RequestMapping(path = "login/reLogin", method = RequestMethod.POST)
    @OperLog(operModul = "系统登录-二次认证", operType = AosContent.QUERY, operDesc = "二次验证登录")
    public Result getMenusList(@RequestBody SecondCheckPassVo checkPassVo) {
        logger.info("接受到的参数:userName-->{},checkCode-->{}", checkPassVo.getUserName(), checkPassVo.getCheckCode());
        String userid = CASUtil.getUserId();
        String org = CASUtil.getOrg();
        logger.info("获取当前用户信息:userId-->{}", userid);
        TblUser currentUser = userService.selectByPrimaryKey(userid);
        if (StringUtils.isBlank(checkPassVo.getCheckCode())) {
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        try {
            String secret = currentUser.getReservedField2();
            int code = Integer.valueOf(checkPassVo.getCheckCode());
            boolean validate = GoogleAuthenticator.validateCurrentNumber(secret, code, -1);
            if (!validate) {
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

    @RequestMapping(path = "currentUser", method = RequestMethod.GET)
    @OperLog(operModul = "系统登录-当前用户", operType = AosContent.QUERY, operDesc = "获取当前登录的用户")
    public Result getCurrentUser(HttpServletRequest request) {
        String userName = CASUtil.getUserName();
        TblUser currentUser = null;
        if (StringUtils.isNotBlank(userName)) {
            currentUser = userService.selectCurrentUser(userName);
            if (currentUser != null) {
                request.getSession().setAttribute("currentUserId", currentUser.getUserId());
                TblRole tblRole = roleService.selectByPrimaryKey(currentUser.getRoleId());
                if (tblRole != null) {
                    request.getSession().setAttribute("roleWeight", tblRole.getWeight());
                }
            }
        }
        logger.info("获取当前用户信息:currentUser-->{}", currentUser.toString());
        return Result.success(currentUser);
    }

    @RequestMapping(path = "getQRCodeUrl", method = RequestMethod.GET)
    @OperLog(operModul = "系统登录-动态口令", operType = AosContent.QUERY, operDesc = "动态口令验证")
    public Result getQrCodeUrl() {
        String userId = CASUtil.getUserId();
        String org = CASUtil.getOrg();
        QrCodeResDto qrCodeResDto = new QrCodeResDto();
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        if (AosContent.IS_BIND.equals(currentUser.getReservedField1())) {
            return Result.success(qrCodeResDto);
        }
        String secret = currentUser.getReservedField2();
        String issuer = currentUser.getReservedField3();
        String cuiwy = GoogleAuthenticator.generateOtpAuthUrl(userId, secret, issuer);
        String encodePath = "";
        String qrcodePath = desePath + File.separator + userId;
        String stringImg = "";
        try {
            encodePath = QRCodeUtils.encode(cuiwy, null, qrcodePath, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            File file = new File(qrcodePath + File.separator + encodePath);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bit = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bit)) != -1) {
                outputStream.write(bit, 0, len);
            }
            stringImg = "data:image/gif;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            logger.error("生成二维码异常", e);
        }
        qrCodeResDto.setQrCode(stringImg);
        return Result.success(qrCodeResDto);
    }
}

