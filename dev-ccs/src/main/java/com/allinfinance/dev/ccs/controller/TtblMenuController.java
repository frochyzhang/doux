package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
@RequestMapping("/platform/menus")
public class TtblMenuController {
    private static final Logger logger = LoggerFactory.getLogger(TtblMenuController.class);
    @Autowired
    TblMenuService tblMenuService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "菜单管理-查询菜单",operType = AosContent.QUERY,operDesc = "分页查询菜单列表")
    public Result getMenusList(MenusReqParam menusReqParam) {
        logger.info("菜单查询开始，接受到的参数:currentPage-->{},pageSize-->{}", menusReqParam.getCurrent(), menusReqParam.getPageSize());
        PageInfo<MenusReqParam> optLogs;
        try {
            optLogs = tblMenuService.pageSelectOptMenus(menusReqParam);
        } catch (Exception e) {
            logger.error("查询用户列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询到的菜单列表: {}", optLogs);
        return Result.success(optLogs);
    }

    @GetMapping(value = "/getCurrMenus")
    @ResponseBody
    @OperLog(operModul = "菜单管理-当前菜单",operType = AosContent.QUERY,operDesc = "获取当前用户可访问菜单列表")
    public Result getCurrMenus(HttpServletRequest request) {
        String token = request.getHeader( AosContent.AOS_TOKEN);
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
        logger.info("查询当前用户菜单权限结束，当前用户名：{}",username);
        return Result.success(currentMenusDtos.toArray());
    }

    @GetMapping(value = "/getCurrPowers")
    @ResponseBody
    @OperLog(operModul = "菜单管理-当前权限",operType = AosContent.QUERY,operDesc = "获取当前用户拥有的按钮权限")
    public Result getCurrPowers(HttpServletRequest request) {
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String userId = JwtUtil.getUserId(token);
        logger.info("获取当前用户所有权限数据开始:userId-->{}", userId);
        String[]  currPowers;
        try {
            currPowers = tblMenuService.getCurrPowers(userId);
        } catch (Exception e) {
            logger.error("查询当前用户所有权限数据异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success(currPowers);
    }

    @PostMapping
    @ResponseBody
    @OperLog(operModul = "菜单管理-新增菜单",operType = AosContent.INSERT,operDesc = "新增菜单信息")
    public Result addMenu(@RequestBody TblMenu tblMenu,HttpServletRequest request) {
        logger.info("菜单新增接口接收参数-->{}", tblMenu.toString());
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String username = JwtUtil.getUsername(token);
        tblMenu.setCreateTime(new Date());
        tblMenu.setCreateBy(username);
        try {
            if (tblMenu.getParentMid() == null ){
                String maxMenuId = tblMenuService.selectMaxMenuIdByRoot(tblMenu);
                if (maxMenuId == null){
                    tblMenu.setMenuId(AosContent.MENU_ID_ROOT);
                }else {
                    char[] temp = maxMenuId.toCharArray();//获取位数
                    int num = temp.length;
                    int menuId = Integer.valueOf(maxMenuId);
                    menuId++;
                    String nextMenuId = String.format("%0" + num + "d",menuId);
                    tblMenu.setMenuId(nextMenuId);
                }
            }else {
                String maxMenuId = tblMenuService.selectMaxMenuId(tblMenu);
                if (maxMenuId == null){
                    String nextMenuId = tblMenu.getParentMid() + "01";
                    tblMenu.setMenuId(nextMenuId);
                }else {
                    String order = maxMenuId.replaceAll(tblMenu.getParentMid(),"");
                    int orderNum = Integer.valueOf(order);
                    orderNum++;
                    char[] temp = order.toCharArray();//获取位数
                    int num = temp.length;
                    String nextOrderNum =  String.format("%0" + num + "d",orderNum);
                    logger.error("nextOrderNum:{}",nextOrderNum);
                    String nextMenuId = tblMenu.getParentMid() + nextOrderNum;
                    tblMenu.setMenuId(nextMenuId);
                }
            }
            //为了防止可能出现的重复问题，在每个模块配置的时候添加上父节点id
            if (tblMenu.getNodeType().equals(AosContent.MENU_BTN)){
                tblMenu.setPath(tblMenu.getParentMid()+":"+tblMenu.getPath());
            }
            tblMenuService.addMenu(tblMenu);
        } catch (RuntimeException e) {
            logger.error("新增菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }

    @PutMapping
    @ResponseBody
    @OperLog(operModul = "菜单管理-更新菜单",operType = AosContent.UPDATE,operDesc = "更新菜单信息")
    public Result updateMenu(@RequestBody TblMenu tblMenu,HttpServletRequest request) {
        logger.info("菜单更新接口开始，接收参数-->{}", tblMenu.toString());
        try {
            String token = request.getHeader( AosContent.AOS_TOKEN);
            String username = JwtUtil.getUsername(token);
            tblMenu.setUpdateTime(new Date());
            tblMenu.setUpdateBy(username);
            tblMenuService.updateMenuById(tblMenu);
        } catch (RuntimeException e) {
            logger.error("更新菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    @OperLog(operModul = "菜单管理-删除菜单",operType = AosContent.DELETE,operDesc = "删除菜单信息")
    public Result delMenu(@RequestBody MenusReqParam menusReqParam) {
        String[] menusId = menusReqParam.getMenusId();
        logger.info("菜单删除接口接收参数-->{}", (menusId));
        try {
            tblMenuService.delMenuByIds(menusId);
        } catch (RuntimeException e) {
            logger.error("删除菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }
}

