package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/api/menus")
public class TtblMenuController {
    private static final Logger logger = LoggerFactory.getLogger(TtblMenuController.class);
    @Autowired
    TblMenuService tblMenuService;

    @GetMapping
    @ResponseBody
    public Result getMenusList(@RequestBody MenusReqParam menusReqParam) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", menusReqParam.getCurrent(), menusReqParam.getPageSize());
        PageInfo<MenusReqParam> optLogs;
        try {
            optLogs = tblMenuService.pageSelectOptMenus(menusReqParam);
        } catch (Exception e) {
            logger.error("查询用户列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询到的用户列表: {}", optLogs);
        return Result.success(optLogs);
    }

    @GetMapping(value = "/getCurrMenus")
    @ResponseBody
    public Result getCurrMenus(HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtUtil.getUserId(token);
        logger.info("获取菜单权限数据开始:userId-->{}", userId);
        List<CurrentMenusDto> currentMenusDtos;
        try {
            currentMenusDtos = tblMenuService.getCurrMenus(userId);
        } catch (Exception e) {
            logger.error("查询用户列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(currentMenusDtos.toArray());
    }

    @PostMapping
    @ResponseBody
    public Result addMenu(@RequestBody TblMenu tblMenu) {
        logger.info("菜单新增接口接收参数-->{}", tblMenu.toString());
        try {
            tblMenuService.addMenu(tblMenu);
        } catch (RuntimeException e) {
            logger.error("新增菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }

    @PutMapping
    @ResponseBody
    public Result updateMenu(@RequestBody TblMenu tblMenu) {
        logger.info("菜单更新接口接收参数-->{}", tblMenu.toString());
        try {
            tblMenuService.updateMenuById(tblMenu);
        } catch (RuntimeException e) {
            logger.error("更新菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }

    @DeleteMapping
    @ResponseBody
    public Result delMenu(String[] menusId) {
        logger.info("菜单删除接口接收参数-->{}", Arrays.toString(menusId));
        try {
            tblMenuService.delMenuByIds(menusId);
        } catch (RuntimeException e) {
            logger.error("删除菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
}

