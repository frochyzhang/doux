package com.allinfinance.dev.ccs.controller;

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
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private TblRoleService tblRoleService;

    //分页查询角色
    @RequestMapping(path = "/users",method = RequestMethod.GET)
    public PageInfo<TblRole> selectUsers(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        logger.info("接受到的参数:pageNo-{},pageSize-{}",pageNo,pageSize);
        PageInfo<TblRole> users = tblRoleService.pageSelectRoles(pageNo,pageSize);
        logger.info("查询到的用户列表: {}",users);
        return users;
    }

    //查询角色
    @RequestMapping(path = "/role/{roleId}", method = RequestMethod.GET)
    public TblRole selectRole(@PathVariable("roleId") String roleId){
        return tblRoleService.selectByPrimaryKey(roleId);
    }

}
