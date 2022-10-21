package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.service.TblAccountAervice;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.utils.RSAUtils;
import com.allinfinance.dev.common.dictionary.result.Result;
import com.allinfinance.dev.common.dictionary.result.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class TblAccountServiceImpl implements TblAccountAervice {
    private static final Logger logger = LoggerFactory.getLogger(TblAccountServiceImpl.class);

    @Autowired
    TblUserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   @Override
    public Result updateNewPass(UpdatePasswordParam passwordParam) {
       logger.info("*****************新账户密码Service开始**************");
        String userId = passwordParam.getUserId();
        String username = passwordParam.getUserName();
        String newPassword = passwordParam.getNewPassword();
        if(StringUtils.isEmpty(userId)){
            logger.debug("参数【userId】缺失！");
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        TblUser tblUser = userService.selectByPrimaryKey(userId);
        if(tblUser==null){
            logger.debug("查询用户信息不存在！");
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        if(StringUtils.isNotBlank(passwordParam.getOldPassword())){
            boolean b = checkOldPass(passwordParam.getOldPassword(), tblUser.getUserPass());
            if(!b){
                logger.debug("原始密码和新密码一样，不允许修改！");
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
            logger.info("数据更新成功#@#");
        } catch (RuntimeException e) {
            logger.error("更新密码异常异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
       logger.info("*****************新账户密码Service结束**************");
        return Result.success();
    }

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
}
