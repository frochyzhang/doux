package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.content.RSAKeyProperties;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.service.LoginService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    LoginService loginService;

    @Autowired
    @Qualifier(value = "rsaKeyProperties")
    private RSAKeyProperties rsaProperties;

    @RequestMapping(path = "login/reLogin", method = RequestMethod.POST)
    @ResponseBody
    @OperLog(operModul = "系统登录-OTP二次验证", operType = AosContent.QUERY, operDesc = "OTP二次验证")
    public Result reLogin(@RequestBody SecondCheckPassVo checkPassVo, HttpServletResponse httpServletResponse) {
        logger.info("**********OTP二次验证Controller开始，接受到的参数:userName-->{},checkCode-->{}**********", checkPassVo.getUserName(), checkPassVo.getCheckCode());
        Result result = loginService.reLogin(checkPassVo);
        if(!result.getSuccess()){
            return result;
        }
        TblUser currentUser = (TblUser) result.getData();
        String sign = JwtUtil.sign(currentUser.getUserName(), String.valueOf(currentUser.getUserId()), currentUser.getRoleId(), currentUser.getOrg(),"3");
        httpServletResponse.setHeader("Access-control-Expose-Headers", AosContent.AOS_TOKEN);
        httpServletResponse.setHeader(AosContent.AOS_TOKEN, sign);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        logger.debug("OTP二次验证成功，返回签名：【{}】", sign);
        logger.info("**********OTP二次验证Controller结束**************");

        return result;
    }


    @RequestMapping(path = "currentUser", method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "系统登录-当前用户", operType = AosContent.QUERY, operDesc = "获取当前登录的用户")
    public Result getCurrentUser(HttpServletRequest request) {
        return loginService.getCurrentUser(request);
    }

    @RequestMapping(path = "getQRCodeUrl", method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "系统登录-获取绑定OTP二维码", operType = AosContent.QUERY, operDesc = "获取绑定OTP二维码")
    public Result getQrCodeUrl(String userName, String userId, HttpServletRequest request, HttpServletResponse response) {
        return loginService.getQrCodeUrl(userName, userId, request, response);
    }

    @RequestMapping(path = "getPublicKey", method = RequestMethod.POST)
    @ResponseBody
    @OperLog(operModul = "系统登录-获取登加密密钥", operType = AosContent.QUERY, operDesc = "获取登加密密钥")
    public Result getPublicKey() {
        return Result.success(rsaProperties.getPublicKey().getEncoded());
    }
}

