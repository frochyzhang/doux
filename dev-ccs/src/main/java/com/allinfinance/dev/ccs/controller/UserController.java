package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Base64Utils;
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
    private  BCryptPasswordEncoder passwordEncoder;
    //Id查询用户
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
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
    public Result selectUsers(UserReqParam userReqParam) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", userReqParam.getCurrent(), userReqParam.getPageSize());
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
    public Result addUser(@RequestBody UserReqParam userReqParam, HttpServletRequest request) {
        logger.info("接收到的新增用户信息: {}", userReqParam);
        //设置初始密码
        String encodePass = passwordEncoder.encode(userReqParam.getUserPass());
        userReqParam.setInitPass(encodePass);
        //对密码进行加密,加密方法待定
        //userReqParam.setUserPass();
//        String token = request.getHeader("token");
//        String username = JwtUtil.getUsername(token);
//        logger.info("获取当前系统用户信息:userName-->{}", username);
//        TblUser sysCurrentUser = tblUserService.selectCurrentUser(username);
        //系统用户重名检查
        List<TblUser> tblUser = tblUserService.selectByNameAndOrg(userReqParam);
        if (tblUser.size() != 0) {
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
    public Result updateUserInfo(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的更新用户信息: {}", userReqParam);
        int result = 0;
        try {
            String encode = passwordEncoder.encode(userReqParam.getUserPass());
            userReqParam.setUserPass(encode);
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

    @RequestMapping(path = "updateNewPass",method = RequestMethod.POST)
    @ResponseBody
    public Result updateNewPass(@RequestBody UpdatePasswordParam passwordParam,HttpServletRequest request){
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        TblUser tblUser = tblUserService.selectByPrimaryKey(userId);
        tblUser.setUserPass(passwordEncoder.encode(passwordParam.getNewPassword()));
        tblUser.setPassStatus(AosContent.NOT_FIRST_PASS);
        tblUser.setLastPassUpdateTime(new Date());
        tblUser.setUpdateBy(username);
        try{tblUserService.updateByPrimaryKey(tblUser);}catch (RuntimeException e){
            logger.error("更新密码异常异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
      return Result.success();
    }

}
