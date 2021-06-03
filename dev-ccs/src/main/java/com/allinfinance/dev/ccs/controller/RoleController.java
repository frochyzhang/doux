package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Result selectRoles(RoleReqParam roleReqParam, HttpServletRequest request) {
        logger.info("roleReqParam:-{}", roleReqParam);
        // 获取当前用户的id
        String token = request.getHeader("token");
        String userId = JwtUtil.getUserId(token);
        roleReqParam.setUserId(userId);
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
                tblRole.setAuth(roleAuthMapping.get(tblRole.getRoleId()));
            }
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的角色列表: {}", roles);
        return Result.success(roles);
    }

    //更新角色
    @RequestMapping(method = RequestMethod.POST)
    public Result modifyRole(@RequestBody TblRole tblRole) {
        logger.info("更新角色,接收到的请求参数: tblRole:{}", tblRole);
        int result;
        try {
            //删除角色和权限映射
            tblRoleAuthService.deleteByRoleId(tblRole.getRoleId());
            createRoleAuthMapping(tblRole);
            result = tblRoleService.updateByPrimaryKeySelective(tblRole);
            logger.info("result: {}", result);
            if (result == 1) {
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            logger.error("更新角色信息发生异常", e);
            return Result.failure();
        }

    }

    private void createRoleAuthMapping(TblRole tblRole) {
        ArrayList<String> auths = tblRole.getAuth();
        logger.info("Auths: {}",auths);
        if (auths != null && auths.size() > 0){
            for(String auth:auths){
                TblRoleAuth record = new TblRoleAuth();
                record.setRoleId(tblRole.getRoleId());
                record.setAuthId(auth);
                tblRoleAuthService.insertSelective(record);
            }
        }
    }

    //新增角色
    @RequestMapping(method = RequestMethod.PUT)
    public Result createRole(@RequestBody TblRole tblRole) {
        logger.info("将新增的角色: {}", tblRole);
        int result;
        try {
            //查询API_PERMISSION
            List<TblPermissionInfo> permissionInfos  = tblRoleService.selectPermissionInfos();
            logger.info("查询到的所有permission code: {}",permissionInfos);
            //插入AUTH_PERMISSION_CODE
            for (TblPermissionInfo tblPermissionInfo: permissionInfos){
                TblRolePermissionInfo rolePermissionInfo = new TblRolePermissionInfo();
                rolePermissionInfo.setRoleId(tblRole.getRoleId());
                rolePermissionInfo.setPermissioncode(tblPermissionInfo.getPermissioncode());
                int permissionCode = tblRoleService.insertRolePermissionInfoSelective(rolePermissionInfo);
                logger.info("插入权限代码结构: {}",permissionCode);
                logger.info("插入后: rolePermissionInfo--{}",rolePermissionInfo);
            }
            //插入权限表
            result = tblRoleService.insertSelective(tblRole);
            createRoleAuthMapping(tblRole);
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



    //删除一个或多个角色
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteRoles(@RequestBody RoleReqParam roleReqParam) {
        logger.info("**************************************");
        logger.info("roleReqParam: {}",roleReqParam);
        String[] roleIds = roleReqParam.getRoleIds();
        logger.info("待删除的角色-roleIds: {}", roleIds);
        int result = 0;
        try {
            if (roleIds != null && roleIds.length > 0){
                for (String roleId:roleIds){
//                    //删除角色和权限映射
//                    int i = tblRoleAuthService.deleteByRoleId(roleId);
                    //删除角色
//                    result = tblRoleService.deleteByPrimaryKey(roleId);
                    //使角色无效
                    result = tblRoleService.invalidateRole(roleId);
                    logger.info("result={}",result);
                }
            }
        } catch (Exception e) {
            logger.error("删除角色发生异常", e);
            return Result.failure();
        }
        
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }


}
