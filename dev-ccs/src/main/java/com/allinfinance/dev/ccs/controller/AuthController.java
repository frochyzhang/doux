package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.respdto.AuthMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
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

    //分页查询权限
    @GetMapping
    @OperLog(operModul = "权限管理-权限列表", operType = AosContent.QUERY, operDesc = "分页查询权限列表")
    public Result selectPageAuths(AuthReqParam authReqParam, HttpServletRequest request) {
        logger.info("分页查询权限列表请求参数 AuthReqParam: {}", authReqParam);
        //获取用户的机构号
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String org = JwtUtil.getOrg(token);
        String weight = JwtUtil.getWeight(token);
        //先判断页面是否有传机构参数，如果没传参数就只
        if (StringUtils.isBlank(authReqParam.getOrg()))
            //判断当前操作员是否是超级管理员
            if (weight.equals(AosContent.SUPERADMIN)){}
                return null;
    }

    //更新权限
    @PostMapping
    @OperLog(operModul = "权限管理-更新权限", operType = AosContent.UPDATE, operDesc = "更新权限信息")
    public Result modifyAuth(@RequestBody TblAuth tblAuth) {
        logger.info("-----------------更新权限-------------------");
        logger.info("权限更新接收到的请求参数: tblAuth-{}", tblAuth);
        int result;
        try {
            //更新权限和菜单项映射
            tblAuthService.deleteMenuAuths(tblAuth.getAuthId());
            ArrayList<String> menus = tblAuth.getMenus();
            if (menus != null && menus.size() > 0) {
                for (String menu : menus) {
                    TblMenuAuth record = new TblMenuAuth();
                    record.setAuthId(tblAuth.getAuthId());
                    record.setMenuId(menu);
                    tblAuthService.insertMenuAuth(record);
                }
            }
            result = tblAuthService.updateByPrimaryKeySelective(tblAuth);
        } catch (Exception e) {
            logger.error("更新权限发生异常", e);
            return Result.failure();
        }
        logger.info("result: {}", result);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    //新增权限
    @PutMapping
    @OperLog(operModul = "权限管理-新增权限", operType = AosContent.INSERT, operDesc = "新增权限信息")
    public Result createAuth(@RequestBody TblAuth tblAuth) {
        logger.info("-------------------新增权限---------------------------");
        logger.info("将新增的权限: {}", tblAuth);
        int result;
        try {
            //插入权限
            result = tblAuthService.insertSelective(tblAuth);
            logger.info("插入权限result: {}", result);

            //插入权限和菜单映射
            ArrayList<String> menus = tblAuth.getMenus();
            logger.info("menus:  {}", menus);
            if (menus != null && menus.size() > 0) {
                for (String menu : menus) {
                    logger.info("开始插入权限和菜单映射");
                    TblMenuAuth record = new TblMenuAuth();
                    record.setAuthId(tblAuth.getAuthId());
                    record.setMenuId(menu);
                    int i = tblAuthService.insertMenuAuth(record);
                    logger.info("新增权限菜单结果: {}", i);
                }
            }

        } catch (Exception e) {
            logger.error("新增权限发生异常", e);
            return Result.failure();
        }
        logger.info("新增结果: {}", result);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @RequestMapping(path = "/menus", method = RequestMethod.GET)
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

    //删除一个或多个权限
    @RequestMapping(method = RequestMethod.DELETE)
    @OperLog(operModul = "权限管理-删除权限", operType = AosContent.DELETE, operDesc = "删除权限信息")
    public Result deleteAuths(@RequestBody AuthReqParam authReqParam) {
        logger.info("authReqParam: {}", authReqParam);
        String[] authIds = authReqParam.getAuthIds();
        // 为避免配置给用户的权限被删除 在删除之前先检查删除的权中是否有当前正在被使用的权限 有则当前的删除操作不执行并提示
        List<TblRoleAuth> tblRoleAuths = tblRoleAuthService.selectOnUseAuths(authReqParam);
        if (tblRoleAuths.size() != 0) {
            return Result.success(5008);
        }
        int result = 0;
        try {
            if (authIds != null && authIds.length > 0) {
                for (String authId : authIds) {
                    result = tblAuthService.invalidateAuth(authId);
                    logger.info("result={}", result);
                }
            }
        } catch (Exception e) {
            logger.error("删除权限发生异常", e);
            return Result.failure();
        }
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
