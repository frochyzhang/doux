package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.respdto.QrCodeResDto;
import com.allinfinance.dev.ccs.dal.respdto.UserDto;
import com.allinfinance.dev.ccs.dal.service.LoginService;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.QRCodeUtils;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class LoginServiceIpml implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceIpml.class);
    @Autowired
    TblUserService userService;

    @Autowired
    TblBankManageService bankManageService;

    private static String DEST_PATH;

    @Value("${qrCode.path:/home/aos/qrcode/}")
    public void setDesePath(String desePath) {
        this.DEST_PATH = desePath;
    }


    @Override
    public Result reLogin(@RequestBody SecondCheckPassVo checkPassVo) {
        logger.info("**********OTP验证服务Service层开始:userId-->{}，验证码：【{}】*************", checkPassVo.getUserId(), checkPassVo.getCheckCode());
        TblUser currentUser = userService.selectByPrimaryKey(checkPassVo.getUserId());
        if (StringUtils.isBlank(checkPassVo.getCheckCode())) {
            logger.debug("OTP验证码不能为空！");
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        try {
            String secret = currentUser.getReservedField2();
            int code = Integer.valueOf(checkPassVo.getCheckCode());
            boolean validate = GoogleAuthenticator.validateCurrentNumber(secret, code, -1);
            if (!validate) {
                logger.debug("OTP验证码无效！");
                return Result.failure(ResultCodeEnum.USER_ACCOUNT_ODEERROR);
            }
        } catch (GeneralSecurityException e) {
            logger.error("OTP验证码验证异常！", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        currentUser.setReservedField1(AosContent.IS_BIND);
        currentUser.setUpdateTime(new Date());
        currentUser.setUpdateBy("登陆邦定动态验证码成功更新");
        userService.updateByPrimaryKey(currentUser);
        logger.debug("OTP验证码验证成功,更新ReservedField1标志成功");
        Result result = Result.success(currentUser);
        logger.info("**********OTP验证服务Service层结束:*************");

        return result;
    }


    @Override
    public Result getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String username = JwtUtil.getUsername(token);
        String userId = JwtUtil.getUserId(token);
        logger.info("获取当前用户信息:userName-->{},userId-->{}", username, userId);
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        currentUser.setUserPass("[PROTOC]");
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(currentUser, userDto);
        BankManageReqParam bankManageReqParam = new BankManageReqParam();
        bankManageReqParam.setOrg(currentUser.getOrg());
        bankManageService.selectBankInfo(bankManageReqParam).stream().findFirst().ifPresent(tblBankManage -> userDto.setOrgName(tblBankManage.getBankName()));
        logger.info("获取当前用户信息:currentUser-->{}", userDto.toString());
        return Result.success(userDto);
    }

    @Override
    public Result getQrCodeUrl(String userName, String userId, HttpServletRequest request, HttpServletResponse response) {
        logger.info("************获取OTP二维码服务Service层开始*************");
        QrCodeResDto qrCodeResDto = new QrCodeResDto();
        TblUser currentUser = userService.selectByPrimaryKey(userId);
        logger.debug("查询当前用户是否已经绑定过OPT");
        if (AosContent.IS_BIND.equals(currentUser.getReservedField1())) {
            logger.debug("当前用户已经绑定过OPT,无需重新生成二维码，直接返回");
            return Result.success(qrCodeResDto);
        }
        String secret = currentUser.getReservedField2();
        String issuer = currentUser.getReservedField3();
        String cuiwy = GoogleAuthenticator.generateOtpAuthUrl(userName, secret, issuer);
        String encodePath = "";
        String qrcodePath = DEST_PATH + File.separator + userName;
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
        logger.info("************获取OTP二维码服务Service层结束*************");

        return Result.success(qrCodeResDto);
    }
}
