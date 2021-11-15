package com.allinfinance.dev.ccs.dal.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.*;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@Service
public class TtblMenuServiceImpl implements TblMenuService {
    private static final Logger logger = LoggerFactory.getLogger(TtblMenuServiceImpl.class);

    @Autowired
    TblMenuMapper tblMenuMapper;
    @Autowired
    TblRoleAuthMapper roleAuthMapper;
    @Autowired
    TblMenuAuthMapper tblMenuAuthMapper;
    @Autowired
    TblUserMapper userMapper;

    private ConcurrentHashMap<String, List<TblMenu>> menusMap = new ConcurrentHashMap<>();
    private AtomicInteger intLevel = new AtomicInteger(0);

    @Override
    public PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam) {
        PageHelper.startPage(menusReqParam.getCurrent(), menusReqParam.getPageSize());
        List<MenusReqParam> menusReqParams = tblMenuMapper.pageSelectOptMenus(menusReqParam);
        return new PageInfo<MenusReqParam>(menusReqParams);
    }

    @Override
    public void delMenuByIds(String[] ids) {
        tblMenuMapper.delMenuBatch(ids);
    }

    @Override
    public void deleteByPrimaryKey(String menuId) {
        tblMenuMapper.deleteByPrimaryKey(menuId);
    }

    @Override
    public void addMenu(TblMenu tblMenu) {
        tblMenuMapper.addMenu(tblMenu);
    }

    @Override
    public void updateMenuById(TblMenu tblMenu) {
        tblMenuMapper.updateById(tblMenu);
    }


    @Override
    public List<CurrentMenusDto> getCurrMenus(String userId) {
        // 查询当前用户
        TblUser tblUser = userMapper.selectByPrimaryKey(userId);
        if (tblUser != null) {
            //通过当前用户的角色查询权限
            TblRoleAuthKey tblRoleAuthKey = new TblRoleAuthKey();
            tblRoleAuthKey.setRoleId(tblUser.getRoleId());
            // 获取的role和Auth的关联表
            List<TblRoleAuth> tblRoleAuths = roleAuthMapper.selectByRoleId(tblRoleAuthKey);
            if (tblRoleAuths.size() > 0) {
                // 获取的角色的权限列表
                ArrayList<String> authIds = new ArrayList<>(tblRoleAuths.size());
                tblRoleAuths.forEach(roleAuth -> {
                    authIds.add(roleAuth.getAuthId());
                });

                // 通过权限id查询菜单
                List<TblMenuAuth> tblMenuAuths = tblMenuAuthMapper.selectBatchIds(authIds);
                ArrayList<String> menuIds = new ArrayList<>();
                tblMenuAuths.forEach((authMenu) -> {
                    menuIds.add(authMenu.getMenuId());
                });
                menuIds.add("");
                List<TblMenu> tblMenus = tblMenuMapper.selectRootMenusPath(menuIds);
//                logger.info("获取数据库菜单：{}", tblMenus.toString());
                List<TblMenu> tblMenusData = menusData(null, tblMenus);
                List<CurrentMenusDto> getmenus = getmenus(tblMenusData, tblUser);
                List<CurrentMenusDto> currentMenusDtos = new ArrayList<>();
                getmenus.forEach((menusDto) -> {
                    if (menusDto.getChildren().size() > 0) {
                        currentMenusDtos.add(menusDto);
                    }
                });
//                logger.info("格式化后的层级菜单：{}", currentMenusDtos.toString());
                return currentMenusDtos;
            }
        }
        return new ArrayList<CurrentMenusDto>();
    }

    @Override
    public String[] getCurrPowers(String userId) {
        // 查询当前用户
        TblUser tblUser = userMapper.selectByPrimaryKey(userId);
        if (tblUser != null) {
            //通过当前用户的角色查询权限
            TblRoleAuthKey tblRoleAuthKey = new TblRoleAuthKey();
            tblRoleAuthKey.setRoleId(tblUser.getRoleId());
            // 获取的role和Auth的关联表
            List<TblRoleAuth> tblRoleAuths = roleAuthMapper.selectByRoleId(tblRoleAuthKey);
            if (tblRoleAuths.size() > 0) {
                // 获取的角色的权限列表信息
                ArrayList<String> authIds = new ArrayList<>(tblRoleAuths.size());
                tblRoleAuths.forEach(roleAuth -> {
                    authIds.add(roleAuth.getAuthId());
                });
                // 通过权限id查询菜单
                List<TblMenuAuth> tblMenuAuths = tblMenuAuthMapper.selectBatchIds(authIds);
                ArrayList<String> menuIds = new ArrayList<>();
                tblMenuAuths.forEach((authMenu) -> {
                    menuIds.add(authMenu.getMenuId());
                });
                menuIds.add("");
                // 先获取所有节点
                List<TblMenu> allMenus = tblMenuMapper.selectAllMenus();
                // 存储menuIds包含的节点
                List<TblMenu> menuList = new ArrayList<>();
                // 最后的结果
                List<TblMenu> powerList = new ArrayList<>();
                // 先找到所有的一级菜单
                for (TblMenu tblMenu : allMenus) {
                    for (String menuId : menuIds) {
                        if (tblMenu.getMenuId().equals(menuId)) {
                            menuList.add(tblMenu);
                        }
                    }
                }
                for (int i = 0; i < menuList.size(); i++) {
                    // 寻找当前节点的父节点
                    TblMenu menuParent = getMenuParentTmp(menuList.get(i), allMenus);
                    System.out.println("当前节点的父节点为：" + menuParent);
                    allMenus.stream().map(TblMenu::getMenuId).collect(Collectors.toList());
                }
                String[] powers = new String[10];
                return powers;
            }
        }
        return new String[0];
    }

    // 寻找父节点
    public TblMenu getMenuParent(TblMenu currMenu, List<TblMenu> allMenus) {
        TblMenu menu = new TblMenu();
        List<String> menuIdList = allMenus.stream().map(TblMenu::getMenuId).collect(Collectors.toList());
        //  保存子节点
        List<TblMenu> menus = new ArrayList<>();
        for (int i = 0; i < allMenus.size(); i++) {
            // 如果当前节点的父节点不是根节点则 继续寻找
            if (StringUtils.isNotBlank(currMenu.getParentMid()) && menuIdList.contains(currMenu.getParentMid())) {
                menu = allMenus.get(i);
                menus.add(currMenu);
                // 将当前节点添加到子节点中
                menu.setChildMenus(menus);
                // 当节点的父ID不为null 则递归继续寻找
                if (menu.getParentMid() != null) {
                    getMenuParent(menu, allMenus);
                } else {
                    break;
                }
            }
        }
        return menu;
    }

    public TblMenu getMenuParentTmp(TblMenu currMenu, List<TblMenu> allMenus) {
        TblMenu menu = new TblMenu();
        List<String> menuIdList = allMenus.stream().map(TblMenu::getMenuId).collect(Collectors.toList());
        //  保存子节点
        List<TblMenu> menus = new ArrayList<>();
        // 如果当前节点的父节点不是根节点则 继续寻找
        if (StringUtils.isNotBlank(currMenu.getParentMid()) && menuIdList.contains(currMenu.getParentMid())) {
            menu = allMenus.stream().filter(m -> m.getMenuId().equals(currMenu.getParentMid())).findAny().orElse(null);
            menus.add(currMenu);
            // 将当前节点添加到子节点中
            menu.setChildMenus(menus);
            // 当节点的父ID不为null 则递归继续寻找
            if (menu.getParentMid() != null) {
                menu = getMenuParentTmp(menu, allMenus);
            }
        }
        return menu;
    }


    @Override
    public String selectMaxMenuIdByRoot(TblMenu tblMenu) {
        return tblMenuMapper.selectMaxMenuIdByRoot(tblMenu);
    }

    @Override
    public String selectMaxMenuId(TblMenu tblMenu) {
        return tblMenuMapper.selectMaxMenuId(tblMenu);
    }


    public List<CurrentMenusDto> getmenus(List<TblMenu> tblMenusData, TblUser tblUser) {
        List<CurrentMenusDto> currentMenusDtos = new ArrayList<>();
        for (TblMenu menusData : tblMenusData) {
            CurrentMenusDto currentMenusDto = new CurrentMenusDto();
            CurrentMenusDto menusDto = currentMenusDto;
            menusDto.setName(menusData.getMenuName());
            menusDto.setIcon(menusData.getIcon());
            menusDto.setPath(menusData.getPath());
            menusDto.setAuthority(new String[]{tblUser.getRoleId()});
            List<TblMenu> childMenus = menusData.getChildMenus();
            menusDto.setChildren(getmenus(menusData.getChildMenus(), tblUser));
            currentMenusDtos.add(menusDto);
        }
        return currentMenusDtos;
    }


    public List<TblMenu> menusData(String pId, List<TblMenu> tblMenus) {
        ArrayList<TblMenu> menusDtos_0 = new ArrayList<>();
        for (TblMenu menu_0 : tblMenus) {
            if (StringUtils.equals(pId, menu_0.getParentMid())) {
                menu_0.setChildMenus(menusData(menu_0.getMenuId(), tblMenus));
                menusDtos_0.add(menu_0);
            }
        }
        return menusDtos_0;
    }

}
