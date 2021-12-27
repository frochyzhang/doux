package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.converter.PoMapper;
import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.respdto.AuthMenusDto;
import com.allinfinance.dev.ccs.dal.service.*;
import com.allinfinance.dev.ccs.dto.AuthCreateRequestDTO;
import com.allinfinance.dev.ccs.dto.AuthInfoUpdateRequestDTO;
import com.allinfinance.dev.ccs.dto.AuthsQueryResponseDTO;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private TblRoleAuthService tblRoleAuthService;

    @Autowired
    private TblUserService tblUserService;

    @Autowired
    private TblRoleService tblRoleService;

    @Autowired
    private TblMenuAuthService tblMenuAuthService;

    //分页查询权限
    @GetMapping
    @OperLog(operModul = "权限管理-权限列表", operType = AosContent.QUERY, operDesc = "分页查询权限列表")
    public Result selectPageAuths(AuthReqParam authReqParam, HttpServletRequest request) {
        logger.info("分页查询权限列表请求参数, AuthReqParam: {}", authReqParam);
        // 获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);

        //机构隔离
        if (!AosContent.ROLE_WEIGHT_3.equals(JwtUtil.getWeight(token))) {
            authReqParam.setOrg(JwtUtil.getOrg(token));
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
        authReqParam.setWeight(tblRole.getWeight());

        PageInfo<AuthsQueryResponseDTO> auths;
        try {
            auths = tblAuthService.pageSelectAuths(authReqParam);
            logger.debug("权限列表: {}", auths);
        } catch (Exception e) {
            logger.error("查询权限列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(auths);
    }

    @GetMapping("/all")
    @OperLog(operModul = "权限管理-权限列表", operType = AosContent.QUERY, operDesc = "无分页查询权限列表")
    public Result selectAuths(AuthReqParam authReqParam, HttpServletRequest request) {
        logger.info("开始查询所有权限列表, authReqParam: {}", authReqParam);
        // 获取当前用户的id
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        //机构隔离
        if (!AosContent.ROLE_WEIGHT_3.equals(JwtUtil.getWeight(token))) {
            authReqParam.setOrg(JwtUtil.getOrg(token));
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
        //排除不可用的权限
        authReqParam.setIsAvailable(AosContent.IS_AVAILABLE_TRUE);

        List<AuthsQueryResponseDTO> auths;
        try {
            auths = tblAuthService.selectAuths(authReqParam);
            logger.debug("权限列表: {}", auths);
        } catch (Exception e) {
            logger.error("查询权限列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(auths);
    }

    //更新权限
    @PostMapping
    @OperLog(operModul = "权限管理-更新权限", operType = AosContent.UPDATE, operDesc = "更新权限信息")
    public Result modifyAuth(@RequestBody AuthInfoUpdateRequestDTO authInfoUpdateRequestDTO, HttpServletRequest request) {
        logger.info("-----------------更新权限-------------------");
        logger.info("权限更新接收到的请求参数: authInfoUpdateRequestDTO-{}", authInfoUpdateRequestDTO);
        String username = JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN));
        logger.info("用户名: {}", username);

        //删除权限和菜单映射
        tblMenuAuthService.deleteByAuthId(authInfoUpdateRequestDTO.getAuthId());
        //创建权限和菜单映射
        tblMenuAuthService.createMenuAuthMapping(authInfoUpdateRequestDTO.getAuthId(), authInfoUpdateRequestDTO.getMenus());
        //更新权限信息
        TblAuth tblAuth = PoMapper.INSTANCE.convertToTblAuth(authInfoUpdateRequestDTO, new Date(), username);
        tblAuthService.updateByPrimaryKeySelective(tblAuth);

        logger.info("权限信息更新完成");
        return Result.success();
    }

    //新增权限
    @PutMapping
    @OperLog(operModul = "权限管理-新增权限", operType = AosContent.INSERT, operDesc = "新增权限信息")
    public Result createAuth(@RequestBody AuthCreateRequestDTO authCreateRequestDTO, HttpServletRequest request) {
        logger.info("-------------------新增权限---------------------------");
        logger.info("将新增的权限, authCreateRequestDTO: {}", authCreateRequestDTO);
        String username = JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN));
        logger.info("用户名: {}", username);

        //判断权限信息是否重复
        TblAuth tblAuth = tblAuthService.selectByAuthName(authCreateRequestDTO.getAuthName());
        if (tblAuth != null) {
            logger.error("权限名称已存在, authName: {}", authCreateRequestDTO.getAuthName());
            return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
        }
        tblAuth = PoMapper.INSTANCE.convertToTblAuth(authCreateRequestDTO, new Date(), username);
        tblAuth.setAuthId(IdUtils.getId());
        //插入权限信息
        tblAuthService.insertSelective(tblAuth);

        //创建权限和菜单的映射
        tblMenuAuthService.createMenuAuthMapping(tblAuth.getAuthId(), authCreateRequestDTO.getMenus());
        logger.info("新增权限信息完成");
        return Result.success();
    }

    /**
     * 更新权限时查询权限对应的menu列表
     * @param authId 权限ID
     * @return menu列表
     */
    @GetMapping("/menus")
    @OperLog(operModul = "权限管理-权限列表", operType = AosContent.QUERY, operDesc = "获取权限对应下的列表信息")
    public Result getAuthMenus(@Param("authId") String authId) {
        ArrayList<AuthMenusDto> authMenus;
        try {
            //获取所有的菜单项
            List<TblMenu> tblMenus = tblAuthService.selectMenus(authId);
            logger.info("tblMenus: {}", tblMenus);
            //获取权限菜单树
            authMenus = generateAuthMenuTree(tblMenus);


        } catch (Exception e) {
            logger.error("获取权限列表树异常", e);
            return Result.failure();
        }
        return Result.success(authMenus);
    }

    private ArrayList<AuthMenusDto> generateAuthMenuTree(List<TblMenu> tblMenus) {
        ArrayList<AuthMenusDto> authMenuTree = new ArrayList<>();
        for (TblMenu tblMenu : tblMenus) {
            if (tblMenu.getParentMid() == null) {
                AuthMenusDto authMenusDto = generateSubAuthMenuTree(tblMenu, tblMenus);
                authMenusDto.setParentId("");
                authMenuTree.add(authMenusDto);
            }
        }
        return authMenuTree;
    }

    private AuthMenusDto generateSubAuthMenuTree(TblMenu tblMenu, List<TblMenu> tblMenus) {
        AuthMenusDto authMenusDto = new AuthMenusDto();
        authMenusDto.setKey(tblMenu.getMenuId());
        authMenusDto.setValue(tblMenu.getMenuId());
        authMenusDto.setIsUse(tblMenu.getReservedField1());
        authMenusDto.setTitle(tblMenu.getMenuName());
        authMenusDto.setParentId(tblMenu.getParentMid());
        ArrayList<AuthMenusDto> subAuthMenuTree = new ArrayList<>();
        authMenusDto.setChildren(subAuthMenuTree);
        for (TblMenu menu : tblMenus) {
            if (menu.getParentMid() != null && menu.getParentMid().equals(tblMenu.getMenuId())) {
                subAuthMenuTree.add(generateSubAuthMenuTree(menu, tblMenus));
            }
        }
        return authMenusDto;
    }
}
