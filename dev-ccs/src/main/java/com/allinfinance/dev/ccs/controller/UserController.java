package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author ：Lucas Li
 * @date ：2021/5/13 18:45
 * @description：TODO
 * @version: :1.0
 */
@RestController
@RequestMapping("/platform")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TblUserService tblUserService;

    //Id查询用户
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public Result selectUser(@PathVariable("userId") Integer userId) {
        TblUser tblUser;
        try {
            tblUser = tblUserService.selectByPrimaryKey(userId);
        } catch (Exception e) {
            logger.error("ID查询用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(tblUser);
    }

    /**
     * 分页带参数查新用户
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public Result selectUsers(UserReqParam userReqParam) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", userReqParam.getCurrent(), userReqParam.getPageSize());
        PageInfo<TblUser> users = null;
        try {
            users = tblUserService.pageSelectUsers(userReqParam);
        } catch (Exception e) {
            logger.error("查询用户列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(users);
    }

    /**
     * 新增用户
     *
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public Result addUser(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的新增用户信息: {}", userReqParam);
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
    @RequestMapping(path = "/users", method = RequestMethod.PUT)
    public Result updateUserInfo(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的更新用户信息: {}", userReqParam);
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
    @RequestMapping(path = "/users", method = RequestMethod.DELETE)
    public Result delUser(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的删除用户Id: {}", StringUtils.join(userReqParam.getUserIds(), ","));
        int result = 0;
        try {
            result = tblUserService.deleteByPrimaryKey(userReqParam.getUserIds());
        } catch (Exception e) {
            logger.error("删除用户异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("删除用户执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }
}
