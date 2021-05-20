package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色管理
 * @author: Lum Wang
 * @create: 2021-05-13 15:44
 */
@RestController
@RequestMapping("/platform/roles")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private TblRoleService tblRoleService;

    @Autowired
    private TblRoleAuthService tblRoleAuthService;

    //分页查询角色
    @RequestMapping(method = RequestMethod.GET)
    public Result selectRoles(RoleReqParam roleReqParam) {
        logger.info("roleReqParam:-{}", roleReqParam);
        if (roleReqParam.getCurrent() == null || roleReqParam.getPageSize() == null) {
            roleReqParam.setCurrent(1);
            roleReqParam.setPageSize(10);
        }
        PageInfo<TblRole> roles;
        try {
            roles = tblRoleService.pageSelectRoles(roleReqParam);
            logger.info("角色列表: {}",roles);

            //获取角色和权限映射
            List<TblRoleAuth> roleAuths = tblRoleAuthService.selectRoleAuths();
            logger.info("角色权限映射: {}",roleAuths);
            HashMap<String, ArrayList<String>> roleAuthMapping = new HashMap<>();
            for (TblRoleAuth tblRoleAuth : roleAuths){
                ArrayList<String> auths = roleAuthMapping.computeIfAbsent(tblRoleAuth.getRoleId(), k -> new ArrayList<>());
                auths.add(tblRoleAuth.getAuthId());
            }
            logger.info("roleAuthMapping: {}",roleAuthMapping);

            List<TblRole> roleList = roles.getList();
            for (TblRole tblRole: roleList){
                //tblRole.setAuth(roleAuthMapping.get(tblRole.getRoleId()));
            }
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的角色列表: {}", roles);
        return Result.success(roles);
    }

    //更新角色
    @RequestMapping(path = "/{roleId}", method = RequestMethod.PUT)
    public Result modifyRole(@RequestBody TblRole tblRole, @PathVariable("roleId") String roleId) {
        logger.info("接收到的请求参数: {},roleId:{}", tblRole, roleId);
        tblRole.setRoleId(roleId);
        int result;
        try {
            result = tblRoleService.updateByPrimaryKey(tblRole);
        } catch (Exception e) {
            logger.error("更新角色信息发生异常", e);
            return Result.failure();
        }
        logger.info("result: {}", result);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    //新增角色
    @RequestMapping(method = RequestMethod.POST)
    public Result createRole(@RequestBody TblRole tblRole) {
        logger.info("将新增的角色: {}", tblRole);
        int result;
        try {
            result = tblRoleService.insertSelective(tblRole);
        } catch (Exception e) {
            logger.error("新增角色发生异常", e);
            return Result.failure();
        }
        logger.info("新增结果: {}", result);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }


}
