package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
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
@RequestMapping("/platform")
public class RoleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(RoleAuthController.class);

    @Autowired
    private TblRoleAuthService tblRoleAuthService;

    //分页查询权限
    @RequestMapping(path = "/roleAuths",method = RequestMethod.GET)
    public PageInfo<TblRoleAuth> selectUsers( @RequestParam("roleId") int roleId, @RequestParam("authId") int authId,
                                          @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        logger.info("接受到的参数:roleId-{},authId-{},pageNo-{},pageSize-{}",roleId,authId,pageNo,pageSize);
        PageInfo<TblRoleAuth> roleAuths = tblRoleAuthService.pageSelectRoleAuths(pageNo,pageSize);
        logger.info("查询到的角色权限列表: {}",roleAuths);
        return roleAuths;
    }


    //修改角色
    @RequestMapping(path = "/roles",method = RequestMethod.PUT)
    public boolean modifyUser(@RequestBody TblRoleAuth tblRoleAuth){
        logger.info("接收到的请求参数:{}",tblRoleAuth);
        int result = tblRoleAuthService.updateByPrimaryKey(tblRoleAuth);
        logger.info("result: {}",result);
        return result == 1;
    }


    //新增角色权限
    @RequestMapping(path = "/roleAuths",method = RequestMethod.POST)
    public boolean createAuth(@RequestBody TblRoleAuth tblRoleAuth){
        logger.info("将新增的角色权限: {}",tblRoleAuth);
        int result = tblRoleAuthService.insertSelective(tblRoleAuth);
        logger.info("新增结果: {}",result);
        return result == 1;
    }



}
