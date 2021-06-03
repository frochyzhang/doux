package com.allinfinance.dev.ccs.dal.service.impl;


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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    private  ConcurrentHashMap<String, List<TblMenu>> menusMap= new ConcurrentHashMap<>();
    private AtomicInteger intLevel=new AtomicInteger(0);
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
            List<TblRoleAuth> tblRoleAuths = roleAuthMapper.selectByRoleId(tblRoleAuthKey);
            if (tblRoleAuths.size() > 0) {
                ArrayList<String> authIds = new ArrayList<>(tblRoleAuths.size());
                tblRoleAuths.forEach(roleAuth -> {
                    authIds.add(roleAuth.getAuthId());
                });

                // 通过权限id查询菜单
                List<TblMenuAuth> tblMenuAuths = tblMenuAuthMapper.selectBatchIds(authIds);
                ArrayList<String> menuIds = new ArrayList<>(tblRoleAuths.size());
                tblMenuAuths.forEach((authMenu)->{
                    menuIds.add(authMenu.getMenuId());
                });
                List<TblMenu> tblMenus = tblMenuMapper.selectRootMenusPath(menuIds);
                logger.info("获取数据库菜单：{}",tblMenus.toString());
                List<TblMenu> tblMenusData = menusData(null, tblMenus);
                logger.info("格式化后的层级菜单：{}",tblMenusData.toString());
                List<CurrentMenusDto> getmenus = getmenus(tblMenus, tblUser);
                List<CurrentMenusDto> currentMenusDtos=new ArrayList<>();
                getmenus.forEach((menusDto)->{
                    if(menusDto.getChildren().size()>0){
                        currentMenusDtos.add(menusDto);
                    }
                });
                return  currentMenusDtos;
            }
        }
        return new ArrayList<CurrentMenusDto>();
    }





    public List<CurrentMenusDto> getmenus(List<TblMenu> tblMenusData, TblUser tblUser ){
        List<CurrentMenusDto> currentMenusDtos=new ArrayList<>();
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
        //组合前端需要的数据结构

//        ArrayList<CurrentMenusDto> currentMenusDtos = new ArrayList<>();
//        for (TblMenu menu : tblMenus) {
//           CurrentMenusDto menusDto = currentMenusDto;
//           menusDto.setName(menu.getMenuName());
//           menusDto.setIcon(menu.getIcon());
//           menusDto.setPath(menu.getPath());
//           menusDto.setAuthority(new String[]{tblUser.getRoleId()});
//            List<TblMenu> tblMenuList = tblMenuMapper.selectMenusPathByPMid("1", menuIds,String.valueOf(menu.getMenuId()));
//            ArrayList<CurrentMenusDto> currentMenusDtos1 = new ArrayList<>();
//            for (TblMenu menu1 : tblMenuList) {
//                CurrentMenusDto menusDto1 = currentMenusDto;
//                menusDto1.setName(menu1.getMenuName());
//                menusDto1.setIcon(menu1.getIcon());
//                menusDto1.setPath(menu1.getPath());
//                menusDto1.setComponent(menu1.getPageUrl());
//                menusDto1.setAuthority(new String[]{tblUser.getRoleId()});
//                currentMenusDtos1.add(menusDto1);
//                List<TblMenu> tblMenuList2 = tblMenuMapper.selectMenusPathByPMid("2",menuIds,String.valueOf(menu.getMenuId()));
//                ArrayList<CurrentMenusDto> currentMenusDtos2 = new ArrayList<>();
//                if(tblMenuList2.size()>0){
//                    for (TblMenu menu2 : tblMenuList2) {
//                        CurrentMenusDto menusDto2 = currentMenusDto;
//                        menusDto2.setName(menu2.getMenuName());
//                        menusDto2.setIcon(menu2.getIcon());
//                        menusDto2.setPath(menu2.getPath());
//                        menusDto2.setComponent(menu2.getPageUrl());
//                        menusDto2.setAuthority(new String[]{tblUser.getRoleId()});
//                        currentMenusDtos2.add(menusDto2);
//                    }
//                }
//                menusDto1.setChildren(currentMenusDtos2);
//            }
//            menusDto.setChildren(currentMenusDtos1);
//            currentMenusDtos.add(menusDto);
//        }
//       return  currentMenusDtos;
    }


    public  List<TblMenu> menusData( String pId, List<TblMenu> tblMenus){
        ArrayList<TblMenu> menusDtos_0 = new ArrayList<>();
        for (TblMenu menu_0 : tblMenus) {
            if(StringUtils.equals(pId,menu_0.getParentMid())){
                menu_0.setChildMenus(menusData(menu_0.getMenuId(),tblMenus));
                menusDtos_0.add(menu_0);
            }
        }
     return menusDtos_0;
    }

}
