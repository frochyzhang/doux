package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.result.Result;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @project: dev-parent
 * @description: 角色权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 13:56
 */
@RestController
@RequestMapping("/platform/roleAuths")
public class RoleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(RoleAuthController.class);

    @Autowired
    private TblRoleAuthService tblRoleAuthService;

    //分页查询权限
    @RequestMapping(method = RequestMethod.GET)
    public Result selectRoleAuths(@RequestParam("roleId") int roleId, @RequestParam("authId") int authId,
                              @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        logger.info("接受到的参数:roleId-{},authId-{},pageNo-{},pageSize-{}",roleId,authId,pageNo,pageSize);
        PageInfo<TblRoleAuth> roleAuths;
        try {
            roleAuths = tblRoleAuthService.pageSelectRoleAuths(pageNo,pageSize);
        }catch (Exception e){
            logger.error("分页查询角色权限发生异常",e);
            return Result.failure();
        }
        return Result.success(roleAuths);
    }


    //修改角色
    @RequestMapping(method = RequestMethod.PUT)
    public Result modifyRoleAuth(@RequestBody TblRoleAuth tblRoleAuth){
        logger.info("接收到的请求参数:{}",tblRoleAuth);
        int result;
        try {
            result = tblRoleAuthService.updateByPrimaryKey(tblRoleAuth);
        }catch (Exception e){
            return Result.failure();
        }
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }


    //新增角色权限
    @RequestMapping(method = RequestMethod.POST)
    public Result createRoleAuth(@RequestBody TblRoleAuth tblRoleAuth){
        logger.info("将新增的角色权限: {}",tblRoleAuth);

        int result;
        try {
            result = tblRoleAuthService.insertSelective(tblRoleAuth);
        }catch (Exception e){
            logger.error("新增角色权限发生异常",e);
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
