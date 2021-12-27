package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private TblUserService tblUserService;

    @Autowired
    private TblRoleAuthService tblRoleAuthService;

    //分页查询角色
    @RequestMapping(method = RequestMethod.GET)
    @OperLog(operModul = "角色管理-角色列表", operType = AosContent.QUERY, operDesc = "分页查询角色列表")
    public Result selectPageRoles(RoleReqParam roleReqParam, HttpServletRequest request) {
        logger.info("roleReqParam:-{}", roleReqParam);
        // 获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        //  获取当前用户的机构号
        String org = JwtUtil.getOrg(token);
        if (!AosContent.ALLINFINANCE_ORG.equals(org)) {
            roleReqParam.setOrg(org);
        }
        roleReqParam.setUserId(userId);
//        if (roleReqParam.getCurrent() == null || roleReqParam.getPageSize() == null) {
//            roleReqParam.setCurrent(1);
//            roleReqParam.setPageSize(20);
//        }
        PageInfo<TblRole> roles;
        try {
            roles = tblRoleService.pageSelectRoles(roleReqParam);
            logger.info("角色列表: {}", roles);
            //获取角色和权限映射
            List<TblRoleAuth> roleAuths = tblRoleAuthService.selectRoleAuths();
            logger.info("角色权限映射: {}", roleAuths);
            HashMap<String, ArrayList<String>> roleAuthMapping = new HashMap<>();
            for (TblRoleAuth tblRoleAuth : roleAuths) {
                ArrayList<String> auths = roleAuthMapping.computeIfAbsent(tblRoleAuth.getRoleId(), k -> new ArrayList<>());
                auths.add(tblRoleAuth.getAuthId());
            }
            logger.info("roleAuthMapping: {}", roleAuthMapping);

            List<TblRole> roleList = roles.getList();
            for (TblRole tblRole : roleList) {
                tblRole.setAuth(roleAuthMapping.get(tblRole.getRoleId()));
            }
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的角色列表: {}", roles);
        return Result.success(roles);
    }

    /**
     * 无分页查询角色
     *
     * @param roleReqParam
     * @param request
     * @return
     */
    @RequestMapping(value = "/currLists", method = RequestMethod.GET)
    @OperLog(operModul = "角色管理-角色列表", operType = AosContent.QUERY, operDesc = "无分页角色列表")
    public Result selectRoles(RoleReqParam roleReqParam, HttpServletRequest request) {
        // 从token中获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        //  获取当前用户的机构号
        String org = JwtUtil.getOrg(token);
        if (!AosContent.ALLINFINANCE_ORG.equals(org)) {
            roleReqParam.setOrg(org);
        }
        // 排除不可用的角色
        roleReqParam.setIsAvailable(AosContent.IS_AVAILABLE_TRUE);
        roleReqParam.setUserId(userId);
        List<TblRole> roles;
        try {
            roles = tblRoleService.pageRoles(roleReqParam);
            logger.info("角色列表: {}", roles);
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的角色列表: {}", roles);
        return Result.success(roles);
    }

    //更新角色
    @RequestMapping(method = RequestMethod.POST)
    @OperLog(operModul = "角色管理-更新角色", operType = AosContent.UPDATE, operDesc = "更新角色信息")
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
        logger.info("Auths: {}", auths);
        if (auths != null && auths.size() > 0) {
            for (String auth : auths) {
                TblRoleAuth record = new TblRoleAuth();
                record.setRoleId(tblRole.getRoleId());
                record.setAuthId(auth);
                tblRoleAuthService.insertSelective(record);
            }
        }
    }

    //新增角色
    @RequestMapping(method = RequestMethod.PUT)
    @OperLog(operModul = "角色管理-新增角色", operType = AosContent.INSERT, operDesc = "新增角色信息")
    public Result createRole(@RequestBody TblRole tblRole) {
        logger.info("将新增的角色: {}", tblRole);
        int result;
        try {
            //插入权限表
            result = tblRoleService.insertSelective(tblRole);
            logger.info("插入权限后: tblRole--{}", tblRole);
            //插入角色权限映射
            createRoleAuthMapping(tblRole);
            //查询API_PERMISSION
            List<TblApiPermissionInfo> permissionInfos = tblRoleService.selectPermissionInfos();
            logger.info("查询到的所有permission code: {}", permissionInfos);
            //插入AUTH_PERMISSION_CODE
            for (TblApiPermissionInfo TblApiPermissionInfo : permissionInfos) {
                TblRolePermissionInfo rolePermissionInfo = new TblRolePermissionInfo();
                rolePermissionInfo.setRoleId(tblRole.getRoleId());
                rolePermissionInfo.setPermissioncode(TblApiPermissionInfo.getPermissioncode());
                logger.info("插入权限代码前 rolePermissionInfo: {}", rolePermissionInfo);
                int permissionCode = tblRoleService.insertRolePermissionInfoSelective(rolePermissionInfo);
                logger.info("插入权限代码结果: {}", permissionCode);
                logger.info("插入后: rolePermissionInfo--{}", rolePermissionInfo);
            }

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
    @OperLog(operModul = "角色管理-删除角色", operType = AosContent.DELETE, operDesc = "删除菜单信息")
    public Result deleteRoles(@RequestBody RoleReqParam roleReqParam) {
        logger.info("roleReqParam: {}", roleReqParam);
        String[] roleIds = roleReqParam.getRoleIds();
        logger.info("待删除的角色-roleIds: {}", roleIds);
        // 执行删除角色操作之前 先检查是否有正在使用的权限，如果有则不允许删除，要先删除用户才行
        List<TblUser> users = tblUserService.selectOnUseRoles(roleReqParam);
        if (users.size() != 0) {
            return Result.success(5008);
        }
        int result = 0;
        try {
            if (roleIds != null && roleIds.length > 0) {
                for (String roleId : roleIds) {
//                    //删除角色和权限映射
//                    int i = tblRoleAuthService.deleteByRoleId(roleId);
                    //删除角色
//                    result = tblRoleService.deleteByPrimaryKey(roleId);
//                    删除PERMISSION CODE
                    int delResult = tblRoleService.deleteRolePermissionInfoByRoleId(roleId);
                    logger.info("删除权限代码结果: {}", delResult);
                    //使角色无效
                    result = tblRoleService.invalidateRole(roleId);
                    logger.info("result={}", result);
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
