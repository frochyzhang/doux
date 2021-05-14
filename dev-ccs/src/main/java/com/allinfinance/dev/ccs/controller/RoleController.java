package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @project: dev-parent
 * @description: 角色管理
 * @author: Lum Wang
 * @create: 2021-05-13 15:44
 */
@RestController
@RequestMapping("/platform")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private TblRoleService tblRoleService;

    //分页查询角色
    @RequestMapping(path = "/roles",method = RequestMethod.GET)
    public PageInfo<TblRole> selectUsers(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        logger.info("接受到的参数:pageNo-{},pageSize-{}",pageNo,pageSize);
        PageInfo<TblRole> users = tblRoleService.pageSelectRoles(pageNo,pageSize);
        logger.info("查询到的角色列表: {}",users);
        return users;
    }

    //更新角色
    @RequestMapping(path = "/roles/{roleId}",method = RequestMethod.PUT)
    public boolean modifyUser(@RequestBody TblRole tblRole,@PathVariable("roleId") int roleId){
        logger.info("接收到的请求参数: {},authId:{}",tblRole,roleId);
        tblRole.setRoleId(roleId);

        int result = tblRoleService.updateByPrimaryKey(tblRole);


        logger.info("result: {}",result);
        return result == 1;
    }

    //新增角色
    @RequestMapping(path = "/roles",method = RequestMethod.POST)
    public boolean createAuth(@RequestBody TblRole tblRole){
        logger.info("将新增的角色: {}",tblRole);
        int result = tblRoleService.insertSelective(tblRole);
        logger.info("新增结果: {}",result);
        return result == 1;
    }



}
