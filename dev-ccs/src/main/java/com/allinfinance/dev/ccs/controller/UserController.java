package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
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
    public TblUser selectUser(@PathVariable("userId") Integer userId) {
        return tblUserService.selectByPrimaryKey(userId);
    }

    /**
     * 分页带参数查新用户
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public PageInfo<TblUser> selectUsers(UserReqParam userReqParam) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", userReqParam.getCurrent(), userReqParam.getPageSize());
        PageInfo<TblUser> users = tblUserService.pageSelectUsers(userReqParam);
        logger.info("查询到的用户列表: {}", users);
        return users;
    }

    /**
     * 新增用户
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public Boolean addUser(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的新增用户信息: {}", userReqParam);
        int result = tblUserService.insertSelective(userReqParam);
        logger.info("新增用户执行结果: {}", result == 1 ? "SUCCESS" : "FAIL");
        return result == 1 ? true : false;
    }

    /**
     * 更新用户信息
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/user", method = RequestMethod.PUT)
    public Boolean updateUserInfo(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的更新用户信息: {}", userReqParam);
        int result = tblUserService.updateByPrimaryKeySelective(userReqParam);
        logger.info("更新用户执行结果: {}", result == 1 ? "SUCCESS" : "FAIL");
        return result == 1 ? true : false;
    }

    /**
     * 删除
     * @param userReqParam
     * @return
     */
    @RequestMapping(path = "/user", method = RequestMethod.DELETE)
    public Boolean delUser(@RequestBody UserReqParam userReqParam) {
        logger.info("接收到的删除用户Id: {}", StringUtils.join(userReqParam.getUserIds(),",") );
        int result = tblUserService.deleteByPrimaryKey(userReqParam.getUserIds());
        logger.info("删除用户执行结果: {}", result == 1 ? "SUCCESS" : "FAIL");
        return result == 1 ? true : false;
    }
}
