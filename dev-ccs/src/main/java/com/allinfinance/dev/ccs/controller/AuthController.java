package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 10:52
 */
@RestController
@RequestMapping("/platform/auths")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TblAuthService tblAuthService;

    //分页查询权限
    @GetMapping
    public Result selectUsers(AuthReqParam authReqParam){
        logger.info("AuthReqParam: {}",authReqParam);
        PageInfo<TblAuth> auths;
        List<TblAuth> authList;
        try {
            if (authReqParam.getCurrent() == null || authReqParam.getPageSize() == null){
                authList = tblAuthService.selectAuths();
                logger.info("角色列表: {}",authList);
                return Result.success(authList);
            }else {
                auths = tblAuthService.pageSelectAuths(authReqParam);
                logger.info("分页的角色列表: {}",auths);
                return Result.success(auths);
            }
        }catch (Exception e){
            logger.error("查询权限列表异常",e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

    }

    //更新权限
    @PutMapping
    public Result modifyUser(@RequestBody TblAuth tblAuth,@PathVariable("authId") String authId){
        logger.debug("接收到的请求参数: {},authId:{}",tblAuth,authId);
        tblAuth.setAuthId(authId);
        int result;
        try {
            result = tblAuthService.updateByPrimaryKey(tblAuth);
        }catch (Exception e){
            logger.error("更新权限发生异常",e);
            return Result.failure();
        }
        logger.info("result: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

    //新增权限
    @PostMapping
    public Result createAuth(@RequestBody TblAuth tblAuth){
        logger.debug("将新增的权限: {}",tblAuth);
        int result;
        try {
            result = tblAuthService.insertSelective(tblAuth);
        }catch (Exception e){
            logger.error("新增权限发生异常",e);
            return Result.failure();
        }
        logger.info("新增结果: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

}
