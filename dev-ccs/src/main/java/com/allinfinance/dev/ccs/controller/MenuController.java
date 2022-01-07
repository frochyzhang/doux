package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单表 前端控制器
 *
 * @author liuqi
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/platform/menus")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    TblMenuService tblMenuService;

    @GetMapping
    @OperLog(operModul = "菜单管理-查询菜单", operType = AosContent.QUERY, operDesc = "分页查询菜单列表")
    public Result getMenusList(MenusReqParam menusReqParam) {
        logger.info("菜单查询开始，接受到的参数:currentPage-->{},pageSize-->{}", menusReqParam.getCurrent(), menusReqParam.getPageSize());
        PageInfo<TblMenu> menus;
        try {
            menus = tblMenuService.pageSelectOptMenus(menusReqParam);
        } catch (Exception e) {
            logger.error("查询菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询到的菜单列表: {}", menus);
        return Result.success(menus);
    }

    /**
     * 获取的所有的menu列表
     *
     * @return
     */
    @GetMapping("/all")
    @OperLog(operModul = "菜单管理-查询菜单", operType = AosContent.QUERY, operDesc = "查询所有菜单列表")
    public Result getAllMenusList() {
        logger.info("查询所有菜单开始!");
        List<TblMenu> menus;
        try {
            menus = tblMenuService.selectAllMenus();
        } catch (Exception e) {
            logger.error("查询所有菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询所有菜单结束!");
        return Result.success(menus);
    }


    @GetMapping(value = "/getCurrMenus")
    @OperLog(operModul = "菜单管理-当前菜单", operType = AosContent.QUERY, operDesc = "获取当前用户可访问菜单列表")
    public Result getCurrMenus(HttpServletRequest request) {
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        logger.info("获取菜单权限数据开始:userId-->{}", userId);
        List<CurrentMenusDto> currentMenusDtos;
        try {
            currentMenusDtos = tblMenuService.getCurrMenus(userId);
        } catch (Exception e) {
            logger.error("查询当前用户菜单权限异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询当前用户菜单权限结束，当前用户名：{}", username);
        return Result.success(currentMenusDtos.toArray());
    }


    @GetMapping(value = "/getCurrPowers")
    @OperLog(operModul = "菜单管理-当前权限", operType = AosContent.QUERY, operDesc = "获取当前用户拥有的按钮权限")
    public Result getCurrPowers(HttpServletRequest request) {
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        logger.info("获取当前用户所有权限数据开始:userId-->{}", userId);
        String[] currPowers;
        try {
            currPowers = tblMenuService.getCurrPowers(userId);
        } catch (Exception e) {
            logger.error("查询当前用户所有权限数据异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(currPowers);
    }


    @PostMapping
    @OperLog(operModul = "菜单管理-新增菜单", operType = AosContent.INSERT, operDesc = "新增菜单信息")
    public Result addMenu(@RequestBody TblMenu tblMenu, HttpServletRequest request) {
        logger.info("新增菜单请求开始:请求参数-->{}", tblMenu);
        return tblMenuService.addMenu(tblMenu, request);
    }


    @PutMapping
    @OperLog(operModul = "菜单管理-更新菜单", operType = AosContent.UPDATE, operDesc = "更新菜单信息")
    public Result updateMenu(@RequestBody TblMenu tblMenu, HttpServletRequest request) {
        return tblMenuService.updateMenuById(tblMenu, request);
    }


    @DeleteMapping
    @OperLog(operModul = "菜单管理-删除菜单", operType = AosContent.DELETE, operDesc = "删除菜单信息")
    public Result delMenu(@RequestBody MenusReqParam menusReqParam) {
        String[] menusId = menusReqParam.getMenusId();
        try {
            tblMenuService.delMenuByIds(menusId);
        } catch (RuntimeException e) {
            logger.error("删除菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
}

