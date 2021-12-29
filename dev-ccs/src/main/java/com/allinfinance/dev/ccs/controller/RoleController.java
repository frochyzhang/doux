package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.converter.PoMapper;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.dto.PageableRolesQueryResponseDTO;
import com.allinfinance.dev.ccs.dto.RoleCreateRequestDTO;
import com.allinfinance.dev.ccs.dto.RoleInfoUpdateRequestDTO;
import com.allinfinance.dev.ccs.dto.RolesQueryResponseDTO;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    /**
     * 根据用户权限分页查询角色列表
     * @param roleReqParam 查询参数
     * @param request HttpServletRequest
     * @return 分页角色列表
     */
    @GetMapping
    @OperLog(operModul = "角色管理-角色列表", operType = AosContent.QUERY, operDesc = "分页查询角色列表")
    public Result selectPageRoles(RoleReqParam roleReqParam, HttpServletRequest request) {
        logger.info("开始查询分页查询角色列表, roleReqParam: {}", roleReqParam);
        // 获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);

        //机构隔离
        if (!AosContent.ROLE_WEIGHT_SUPER_ADMIN.equals(JwtUtil.getWeight(token))) {
            roleReqParam.setOrg(JwtUtil.getOrg(token));
        }

        TblUser tblUser = tblUserService.selectByPrimaryKey(userId);
        logger.info("用户信息: {}", tblUser);
        if (tblUser == null || !AosContent.IS_AVAILABLE_TRUE.equals(tblUser.getIsAvailable())) {
            logger.error("用户不存在或者不可用, userId: {}", userId);
            return Result.failure(ResultCodeEnum.USER_NOT_EXIST);
        }

        TblRole tblRole = tblRoleService.selectByPrimaryKey(tblUser.getRoleId());
        logger.info("用户对应角色信息: {}", tblRole);
        if (tblRole == null) {
            logger.error("未查询到用户对应角色信息, roleId: {}", tblUser.getRoleId());
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        roleReqParam.setWeight(tblRole.getWeight());

        PageInfo<PageableRolesQueryResponseDTO> roles;
        try {
            roles = tblRoleService.pageSelectRoles(roleReqParam);
            logger.debug("角色列表: {}", roles);
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(roles);
    }

    /**
     * 无分页查询角色
     *
     * @param roleReqParam 查询参数
     * @param request HttpServletRequest
     * @return 角色列表
     */
    @GetMapping("/currLists")
    @OperLog(operModul = "角色管理-角色列表", operType = AosContent.QUERY, operDesc = "无分页角色列表")
    public Result selectRoles(RoleReqParam roleReqParam, HttpServletRequest request) {
        logger.info("开始查询所有角色列表, roleReqParam: {}", roleReqParam);
        // 获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);

        //机构隔离
        if (!AosContent.ROLE_WEIGHT_SUPER_ADMIN.equals(JwtUtil.getWeight(token))) {
            roleReqParam.setOrg(JwtUtil.getOrg(token));
        }

        TblUser tblUser = tblUserService.selectByPrimaryKey(userId);
        logger.info("用户信息: {}", tblUser);
        if (tblUser == null || !AosContent.IS_AVAILABLE_TRUE.equals(tblUser.getIsAvailable())) {
            logger.error("用户不存在或者不可用, userId: {}", userId);
            return Result.failure(ResultCodeEnum.USER_NOT_EXIST);
        }

        TblRole tblRole = tblRoleService.selectByPrimaryKey(tblUser.getRoleId());
        logger.info("用户对应角色信息: {}", tblRole);
        if (tblRole == null) {
            logger.error("未查询到用户对应角色信息, roleId: {}", tblUser.getRoleId());
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        roleReqParam.setWeight(tblRole.getWeight());
        // 排除不可用的角色
        roleReqParam.setIsAvailable(AosContent.IS_AVAILABLE_TRUE);

        List<RolesQueryResponseDTO> roles;
        try {
            roles = tblRoleService.queryRoles(roleReqParam);
            logger.debug("角色列表: {}", roles);
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(roles);
    }

    /**
     * 更新角色信息
     * @param roleInfoUpdateRequestDTO 角色信息
     * @param request HttpServletRequest
     * @return 是否更新成功
     */
    @PostMapping
    @OperLog(operModul = "角色管理-更新角色", operType = AosContent.UPDATE, operDesc = "更新角色信息")
    public Result modifyRole(@RequestBody RoleInfoUpdateRequestDTO roleInfoUpdateRequestDTO, HttpServletRequest request) {
        logger.info("更新角色, 接收到的请求参数: {}", roleInfoUpdateRequestDTO);
        String username = JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN));
        logger.info("用户名: {}", username);

        //删除角色和权限映射
        tblRoleAuthService.deleteByRoleId(roleInfoUpdateRequestDTO.getRoleId());
        //创建角色和权限的映射
        tblRoleAuthService.createRoleAuthMapping(roleInfoUpdateRequestDTO.getRoleId(), roleInfoUpdateRequestDTO.getAuth());
        //更新角色信息
        TblRole tblRole = PoMapper.INSTANCE.convertToTblRole(roleInfoUpdateRequestDTO, new Date(), username);
        tblRoleService.updateByPrimaryKeySelective(tblRole);

        logger.info("角色信息更新完成");
        return Result.success();
    }

    /**
     * 新增角色
     * @param roleCreateRequestDTO 新增角色信息
     * @param request HttpServletRequest
     * @return 是否新增成功
     */
    @PutMapping
    @OperLog(operModul = "角色管理-新增角色", operType = AosContent.INSERT, operDesc = "新增角色信息")
    public Result createRole(@RequestBody RoleCreateRequestDTO roleCreateRequestDTO, HttpServletRequest request) {
        logger.info("新增角色信息: {}", roleCreateRequestDTO);
        String username = JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN));
        logger.info("用户名: {}", username);

        //判断角色信息是否重复
        TblRole tblRole = tblRoleService.selectByRoleName(roleCreateRequestDTO.getRoleName());
        if (tblRole != null) {
            logger.error("角色名称已存在, roleName: {}", roleCreateRequestDTO.getRoleName());
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        tblRole = PoMapper.INSTANCE.convertToTblRole(roleCreateRequestDTO, new Date(), username);
        tblRole.setRoleId(IdUtils.getId());
        //插入角色信息
        tblRoleService.insertSelective(tblRole);

        //创建角色和权限的映射
        tblRoleAuthService.createRoleAuthMapping(tblRole.getRoleId(), roleCreateRequestDTO.getAuth());
        logger.info("新增角色信息完成");
        return Result.success();
    }
}
