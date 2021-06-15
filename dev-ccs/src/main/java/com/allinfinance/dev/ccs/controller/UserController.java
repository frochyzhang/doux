package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * @author ：Lucas Li
 * @date ：2021/5/13 18:45
 * @description：TODO
 * @version: :1.0
 */
@RestController
@RequestMapping("/platform/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TblUserService tblUserService;

    @Autowired
    private TblBankService tblBankService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //Id查询用户
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Result selectUser(@PathVariable("userId") String userId) {
        TblUser tblUser;
        try {
            tblUser = tblUserService.selectByPrimaryKey(userId);
        } catch (Exception e) {
            logger.error("ID查询用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(tblUser);
    }

    /**
     * 分页带参数查新用户
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result selectUsers(UserReqParam userReqParam, HttpServletRequest request) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", userReqParam.getCurrent(), userReqParam.getPageSize());
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String org = JwtUtil.getOrg(token);
        //获取当前登录的用户id
        String userId = JwtUtil.getUserId(token);
        userReqParam.setUserId(userId);
        logger.info("获取当前操作用户的机构号:org-->{}", org);
        if (userReqParam.getOrg() == null || userReqParam.getOrg().equals("")) {
            //当当前的用户是超级管理员时显示所有列表
            if (org.equals("000000000000")) {
                userReqParam.setOrg(null);
            } else {
                userReqParam.setOrg(org);
            }
        }
        //剔除不可用的用户
        userReqParam.setIsAvailable("1");
        PageInfo<TblUser> users = null;
        try {
            users = tblUserService.pageSelectUsers(userReqParam);
        } catch (Exception e) {
            logger.error("查询用户列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询用户列表执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(users);
    }

    /**
     * 新增用户
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Result addUser(@RequestBody UserReqParam userReqParam, HttpServletRequest request) {
        logger.info("接收到的新增用户信息: {}", userReqParam);
        //设置初始密码
        userReqParam.setInitPass(passwordEncoder.encode(userReqParam.getUserPass()));
        // 对密码进行加密
        userReqParam.setUserPass(passwordEncoder.encode(userReqParam.getUserPass()));
        //配置用户口令
        BankReqParam bankReqParam = new BankReqParam();
        bankReqParam.setOrg(userReqParam.getOrg());
        List<TblBankManage> tblBankManages = tblBankService.selectByBankInfo(bankReqParam);
        userReqParam.setReservedField2(tblBankManages.get(0).getBankNameEn());
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        logger.info("获取当前系统用户姓名:userName-->{}", userName);
        //设置首次用户登录时显示绑定二维码
        userReqParam.setReservedField1("0");
        //查询bankmanage表设置用户的Issuer
        TblBankManage tblBankManage = tblBankService.selectBankInfoByOrg(userReqParam.getOrg());
        userReqParam.setReservedField3(tblBankManage.getBankNameEn());
        userReqParam.setReservedField2(GoogleAuthenticator.generateBase32Secret());
        //设置用户的Issuer

        userReqParam.setCreateBy(userName);
        //系统用户重名检查
        TblUser tblUser = tblUserService.selectByNameAndOrg(userReqParam);
        if (tblUser != null) {
            return Result.failure("该用户已存在", ResultCodeEnum.USER_HAS_EXISTED.code());
        }
        int result = 0;
        try {
            result = tblUserService.insertSelective(userReqParam);
        } catch (Exception e) {
            logger.error("新增用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("新增用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

    /**
     * 更新用户信息
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public Result updateUserInfo(@RequestBody TblUser userReqParam) {
        logger.info("接收到的更新用户信息: {}", userReqParam);
        //当接收到的密码字段不为空时启用加密
        if (userReqParam.getUserPass() != null && (!userReqParam.getUserPass().equals(""))) {
            userReqParam.setUserPass(passwordEncoder.encode(userReqParam.getUserPass()));
        }
        int result = 0;
        try {
            result = tblUserService.updateByPrimaryKeySelective(userReqParam);
        } catch (Exception e) {
            logger.error("更新用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

    /**
     * 删除
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Result delUser(@RequestBody UserReqParam userReqParam, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.info("请求的uri: {}", requestURI);
        //暂时存放进于预留域传到service
        userReqParam.setReservedField1(requestURI);
        int result = 0;
        try {
            result = tblUserService.deleteByPrimaryKey(userReqParam);
        } catch (Exception e) {
            logger.error("删除用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("删除用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

}
