package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.GoogleAuthenticator;
import com.allinfinance.dev.ccs.utils.RSAUtils;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;


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
    private TblBankManageService tblBankService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //Id查询用户
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "用户管理-查询用户", operType = AosContent.QUERY, operDesc = "根据id查询用户")
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
    @OperLog(operModul = "用户管理-查询用户", operType = AosContent.QUERY, operDesc = "查询用户列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result selectUsers(UserReqParam userReqParam, HttpServletRequest request) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", userReqParam.getCurrent(), userReqParam.getPageSize());
        String token = request.getHeader(AosContent.AOS_TOKEN);
        if (!AosContent.SUPERADMIN.equals(JwtUtil.getWeight(token))) {
            userReqParam.setOrg(JwtUtil.getOrg(token));
        }
        PageInfo<TblUser> users;
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
    @OperLog(operModul = "用户管理-新增用户", operType = AosContent.INSERT, operDesc = "新增用户信息")
    public Result addUser(@RequestBody UserReqParam userReqParam, HttpServletRequest request) {
        logger.info("接收到的新增用户信息: {}", userReqParam);
        TblUser tblUser = new TblUser();
        tblUser.setUserName(userReqParam.getUserName());
        tblUser.setRoleId(userReqParam.getRoleId());
        tblUser.setOrg(userReqParam.getOrg());
        tblUser.setUserPass(userReqParam.getUserPass());
        tblUser.setMobileNo(userReqParam.getMobileNo());
        tblUser.setIsAvailable(userReqParam.getIsAvailable());
        //设置初始密码
        tblUser.setInitPass(passwordEncoder.encode(userReqParam.getUserPass()));
        // 对密码进行加密
        tblUser.setUserPass(passwordEncoder.encode(userReqParam.getUserPass()));
        //配置用户口令
        //设置首次用户登录时显示绑定二维码，设置为未绑定状态
        tblUser.setReservedField1(AosContent.NOT_BIND);
        //设置用户的Issuer
        tblUser.setReservedField2(GoogleAuthenticator.generateBase32Secret());
        BankManageReqParam bankReqParam = new BankManageReqParam();
        bankReqParam.setOrg(userReqParam.getOrg());
        // 查询用户所属机构的英文简称
        tblBankService.selectBankInfo(bankReqParam).stream().findFirst().ifPresent(tblBankManage -> tblUser.setReservedField3(tblBankManage.getBankNameEn()));
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        tblUser.setCreateBy(userName);
        tblUser.setCreateTime(new Date());
        //系统用户重名检查
        TblUser isExitUser = tblUserService.selectByUserName(userReqParam);
        if (isExitUser != null) {
            return Result.failure("该用户已存在", ResultCodeEnum.USER_HAS_EXISTED.code());
        }
        int result;
        try {
            result = tblUserService.insertSelective(tblUser);
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
    @OperLog(operModul = "用户管理-更新用户", operType = AosContent.UPDATE, operDesc = "更新用户信息")
    public Result updateUserInfo(@RequestBody TblUser userReqParam) {
        logger.info("接收到的更新用户信息: {}", userReqParam);
        //当接收到的密码字段不为空时先解密再加密
        if (userReqParam.getUserPass() != null && (!"".equals(userReqParam.getUserPass()))) {
            String decryptPass = null;
            try {
                decryptPass = RSAUtils.decrypt(userReqParam.getUserPass());
            } catch (Exception e) {
                logger.error("密文解密错误！", e);
                return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
            }
            userReqParam.setUserPass(passwordEncoder.encode(decryptPass));
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
    @OperLog(operModul = "用户管理-删除用户", operType = AosContent.DELETE, operDesc = "删除用户")
    public Result delUser(@RequestBody UserReqParam userReqParam, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        logger.info("请求的参数userReqParam: {}", userReqParam);
        //暂时存放进于预留域传到service
//        userReqParam.setReservedField1(requestUri);
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
