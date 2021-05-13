package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    //查询角色
    @RequestMapping(path = "/role/{roleId}", method = RequestMethod.GET)
    public TblRole selectRole(@PathVariable("roleId") String roleId){
        return tblRoleService.selectByPrimaryKey(roleId);
    }

}
