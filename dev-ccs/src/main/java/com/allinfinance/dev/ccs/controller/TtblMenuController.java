package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.service.TblMenuAuthService;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/")
public class TtblMenuController {
    private static final Logger logger = LoggerFactory.getLogger(TtblMenuController.class);
    @Autowired
    TblMenuService tblMenuService;

    @RequestMapping(path = "queryMenus" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getMenusList(@RequestBody MenusReqParam menusReqParam){
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

    @RequestMapping(path = "addMenus" ,method = RequestMethod.PUT)
    @ResponseBody
    public Result addMenu(@RequestBody TblMenu tblMenu){
        logger.info("菜单新增接口接收参数-->{}",tblMenu.toString());
        try {
            tblMenuService.addMenu(tblMenu);
        }catch (RuntimeException e){
            logger.error("新增菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
    @RequestMapping(path = "updateMenus" ,method = RequestMethod.POST)
    @ResponseBody
    public Result updateMenu(@RequestBody TblMenu tblMenu){
        logger.info("菜单更新接口接收参数-->{}",tblMenu.toString());
        try {
            tblMenuService.updateMenuById(tblMenu);
        }catch (RuntimeException e){
            logger.error("更新菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }

    @RequestMapping(path = "delMenus/{menuId}" ,method = RequestMethod.DELETE)
    @ResponseBody
    public Result delMenu(String[] menusId){
        logger.info("菜单删除接口接收参数-->{}",menusId.toString());
        try {
            tblMenuService.delMenuByIds(menusId);
        }catch (RuntimeException e){
            logger.error("删除菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
    }

