package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.respdto.AuthMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
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

    //分页查询权限
    @GetMapping
    public Result selectAuths(AuthReqParam authReqParam){
        logger.info("AuthReqParam: {}",authReqParam);
        PageInfo<TblAuth> auths;
        List<TblAuth> authList;
        try {
            if (authReqParam.getCurrent() == null || authReqParam.getPageSize() == null){
                authList = tblAuthService.selectAuths();
                logger.info("权限列表: {}",authList);
                return Result.success(authList);
            }else {
                auths = tblAuthService.pageSelectAuths(authReqParam);
                logger.info("分页的权限列表: {}",auths);
                List<TblAuth> tblAuths = auths.getList();
                logger.info("tblAuths: {}",tblAuths);
                //获取所有的权限,菜单项映射
                List<TblMenuAuth> tblMenuAuths = tblAuthService.selectMenuAuths();
                logger.info("tblMenuAuths: {}",tblMenuAuths);
                HashMap<String, ArrayList<String>> authMenuMapping = new HashMap<>();
                for (TblMenuAuth tblMenuAuth : tblMenuAuths){
                    ArrayList<String> menus = authMenuMapping.computeIfAbsent(tblMenuAuth.getAuthId(), k -> new ArrayList<>());
                    menus.add(tblMenuAuth.getMenuId());
                }
                logger.info("roleMenuMapping: {}",authMenuMapping);
                for (TblAuth tblAuth : tblAuths){
                    tblAuth.setMenus(authMenuMapping.get(tblAuth.getAuthId()));
                }

                return Result.success(auths);
            }
        }catch (Exception e){
            logger.error("查询权限列表异常",e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

    }


    //更新权限
    @PostMapping
    public Result modifyAuth(@RequestBody TblAuth tblAuth){
        logger.debug("权限更新接收到的请求参数: tblAuth-{}",tblAuth);
        int result;
        try {
            //更新权限和菜单项映射
            tblAuthService.deleteMenuAuths(tblAuth.getAuthId());
            ArrayList<String> menus = tblAuth.getMenus();
            if (menus != null && menus.size() > 0){
                for(String menu:menus){
                    TblMenuAuth record = new TblMenuAuth();
                    record.setAuthId(tblAuth.getAuthId());
                    record.setMenuId(menu);
                    tblAuthService.insertMenuAuth(record);
                }
            }
            result = tblAuthService.updateByPrimaryKeySelective(tblAuth);
        }catch (Exception e){
            logger.error("更新权限发生异常",e);
            return Result.failure();
        }
        logger.info("result: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

    //新增权限
    @PutMapping
    public Result createAuth(@RequestBody TblAuth tblAuth){
        logger.info("-------------------新增权限---------------------------");
        logger.info("将新增的权限: {}",tblAuth);
        int result;
        try {
            //插入权限
            result = tblAuthService.insertSelective(tblAuth);
            logger.info("result: {}",result);

            //插入权限和菜单映射
            ArrayList<String> menus = tblAuth.getMenus();
            logger.info("menus:  {}",menus);
            if (menus != null && menus.size() > 0){
                for(String menu:menus){
                    logger.info("开始插入权限和菜单映射");
                    TblMenuAuth record = new TblMenuAuth();
                    record.setAuthId(tblAuth.getAuthId());
                    record.setMenuId(menu);
                    int i = tblAuthService.insertMenuAuth(record);
                    logger.info("新增权限菜单结果: {}",i);
                }
            }

        }catch (Exception e){
            logger.error("新增权限发生异常",e);
            return Result.failure();
        }
        logger.info("新增结果: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

    @RequestMapping(path = "/menus",method = RequestMethod.GET)
    public Result getAuthMenus(){
        ArrayList<AuthMenusDto> authMenus;
        try {
            //获取所有的菜单项
            List<TblMenu> tblMenus = tblAuthService.selectMenus();
            logger.info("tblMenus: {}",tblMenus);
            //获取权限菜单树
            authMenus = generateAuthMenuTree(tblMenus);


        }catch (Exception e){
            logger.error("获取权限列表树异常",e);
            return Result.failure();
        }
        return Result.success(authMenus);
    }

    private ArrayList<AuthMenusDto> generateAuthMenuTree(List<TblMenu> tblMenus) {
        ArrayList<AuthMenusDto> authMenuTree = new ArrayList<>();
        for (TblMenu tblMenu : tblMenus){
            if (tblMenu.getParentMid() == null){
                AuthMenusDto authMenusDto = generateSubAuthMenuTree(tblMenu,tblMenus);
                authMenusDto.setParentId("");
                authMenuTree.add(authMenusDto);
            }
        }
        return authMenuTree;
    }

    private AuthMenusDto generateSubAuthMenuTree(TblMenu tblMenu,List<TblMenu> tblMenus) {
        AuthMenusDto authMenusDto = new AuthMenusDto();
        authMenusDto.setMenuId(tblMenu.getMenuId());
        authMenusDto.setMenuName(tblMenu.getMenuName());
        authMenusDto.setParentId(tblMenu.getParentMid());
        ArrayList<AuthMenusDto> subAuthMenuTree = new ArrayList<>();
        authMenusDto.setChildren(subAuthMenuTree);
        for (TblMenu menu:tblMenus){
            if (menu.getParentMid() != null && menu.getParentMid().equals(tblMenu.getMenuId())){
                subAuthMenuTree.add(generateSubAuthMenuTree(menu,tblMenus));
            }
        }
        return authMenusDto;
    }

    //删除一个或多个权限
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteAuths(@RequestBody AuthReqParam authReqParam) {
        logger.info("authReqParam: {}",authReqParam);
        String[] authIds = authReqParam.getAuthIds();
        logger.info("待删除的权限-authIds: {}", authIds);
        int result = 0;
        try {
            if (authIds != null && authIds.length > 0){
                for (String authId:authIds){
                    //删除权限和菜单项映射
//                    tblAuthService.deleteMenuAuths(authId);
                    //删除权限
//                    result = tblAuthService.deleteByPrimaryKey(authId);
                    //使权限无效
                    result = tblAuthService.invalidateAuth(authId);
                    logger.info("result={}",result);
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
