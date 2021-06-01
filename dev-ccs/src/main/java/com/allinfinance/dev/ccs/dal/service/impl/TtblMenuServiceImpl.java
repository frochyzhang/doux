package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.*;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    TblMenuMapper tblMenuMapper;
    @Autowired
    TblRoleAuthMapper roleAuthMapper;
    @Autowired
    TblMenuAuthMapper tblMenuAuthMapper;
    @Autowired
    TblUserMapper userMapper;

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
               return  getmenus(tblMenus,menuIds,tblUser);
            }
        }
        return new ArrayList<CurrentMenusDto>();
    }





    public List<CurrentMenusDto> getmenus(List<TblMenu> tblMenus, ArrayList<String> menuIds, TblUser tblUser ){
        ArrayList<CurrentMenusDto> currentMenusDtos = new ArrayList<>();
        for (TblMenu menu : tblMenus) {
           CurrentMenusDto menusDto = new CurrentMenusDto();
           menusDto.setName(menu.getMenuName());
           menusDto.setIcon(menu.getIcon());
           menusDto.setPath(menu.getPath());
           menusDto.setAuthority(new String[]{tblUser.getRoleId()});
            List<TblMenu> tblMenuList = tblMenuMapper.selectMenusPathByPMid("1", menuIds,String.valueOf(menu.getMenuId()));
            ArrayList<CurrentMenusDto> currentMenusDtos1 = new ArrayList<>();
            for (TblMenu menu1 : tblMenuList) {
                CurrentMenusDto menusDto1 = new CurrentMenusDto();
                menusDto1.setName(menu1.getMenuName());
                menusDto1.setIcon(menu1.getIcon());
                menusDto1.setPath(menu1.getPath());
                menusDto1.setComponent(menu1.getPageUrl());
                menusDto1.setAuthority(new String[]{tblUser.getRoleId()});
                currentMenusDtos1.add(menusDto1);
                List<TblMenu> tblMenuList2 = tblMenuMapper.selectMenusPathByPMid("2",menuIds,String.valueOf(menu.getMenuId()));
                ArrayList<CurrentMenusDto> currentMenusDtos2 = new ArrayList<>();
                if(tblMenuList2.size()>0){
                    for (TblMenu menu2 : tblMenuList2) {
                        CurrentMenusDto menusDto2 = new CurrentMenusDto();
                        menusDto2.setName(menu2.getMenuName());
                        menusDto2.setIcon(menu2.getIcon());
                        menusDto2.setPath(menu2.getPath());
                        menusDto2.setComponent(menu2.getPageUrl());
                        menusDto2.setAuthority(new String[]{tblUser.getRoleId()});
                        currentMenusDtos2.add(menusDto2);
                    }
                }
                menusDto1.setChildren(currentMenusDtos2);
            }
            menusDto.setChildren(currentMenusDtos1);
            currentMenusDtos.add(menusDto);
        }
       return  currentMenusDtos;
    }

}
