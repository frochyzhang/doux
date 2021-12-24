package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public Result addMenu(TblMenu tblMenu, HttpServletRequest request) {
        logger.info("**********************************菜单新增服务开始，接收参数-->{}*****************************", tblMenu.toString());
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String username = JwtUtil.getUsername(token);
        tblMenu.setCreateTime(new Date());
        tblMenu.setCreateBy(username);
        try {
            if (tblMenu.getParentMid() == null) {
                String maxMenuId = tblMenuMapper.selectMaxMenuIdByRoot(tblMenu);
                if (maxMenuId == null) {
                    tblMenu.setMenuId(AosContent.MENU_ID_ROOT);
                } else {
                    char[] temp = maxMenuId.toCharArray();//获取位数
                    int num = temp.length;
                    int menuId = Integer.valueOf(maxMenuId);
                    menuId++;
                    String nextMenuId = String.format("%0" + num + "d", menuId);
                    tblMenu.setMenuId(nextMenuId);
                }
            } else {
                String maxMenuId = tblMenuMapper.selectMaxMenuId(tblMenu);
                if (maxMenuId == null) {
                    String nextMenuId = tblMenu.getParentMid() + "01";
                    tblMenu.setMenuId(nextMenuId);
                } else {
                    String order = maxMenuId.replaceAll(tblMenu.getParentMid(), "");
                    int orderNum = Integer.valueOf(order);
                    orderNum++;
                    char[] temp = order.toCharArray();//获取位数
                    int num = temp.length;
                    String nextOrderNum = String.format("%0" + num + "d", orderNum);
                    logger.error("nextOrderNum:{}", nextOrderNum);
                    String nextMenuId = tblMenu.getParentMid() + nextOrderNum;
                    tblMenu.setMenuId(nextMenuId);
                }
            }
            //为了防止可能出现的重复问题，在每个模块配置的时候添加上父节点id
            if (tblMenu.getNodeType().equals(AosContent.MENU_BTN)) {
                tblMenu.setPath(tblMenu.getParentMid() + ":" + tblMenu.getPath());
            }
            tblMenuMapper.addMenu(tblMenu);
        } catch (RuntimeException e) {
            logger.error("新增菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("*****************菜单新增服务结束***********");
        return Result.success();

    }

    @Override
    public Result updateMenuById(TblMenu tblMenu,HttpServletRequest request) {
        logger.info("******************菜单更新服务开始，接收参数-->{}*******************", tblMenu.toString());
        try {
            String token = request.getHeader(AosContent.AOS_TOKEN);
            String username = JwtUtil.getUsername(token);
            tblMenu.setUpdateTime(new Date());
            tblMenu.setUpdateBy(username);
            tblMenuMapper.updateById(tblMenu);
        } catch (RuntimeException e) {
            logger.error("更新菜单异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("*************************菜单更新服务结束*******************");
        return Result.success();
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
                // 先获取所有节点
                List<TblMenu> allMenus = tblMenuMapper.selectAllMenus();
                // 查询出所有的根节点
                List<TblMenu> secondMenus = new ArrayList<>();
                // 存储menuIds包含的节点
                List<TblMenu> menuList = new ArrayList<>();
                // 最后结果
                List<TblMenu> resultMenu = new ArrayList<>();
                // 先找到所有的一级菜单
                for (TblMenu tblMenu : allMenus) {
                    for (String menuId : menuIds) {
                        if (tblMenu.getMenuId().equals(menuId)) {
                            menuList.add(tblMenu);
                        }
                    }
                }
                // 查找所有的二级节点
                for (TblMenu tblMenu : menuList) {
                    if (tblMenu.getNodeType().equals(AosContent.MENU_BTN)) {
                        // 如果当前节点是按钮找上级节点
                        secondMenus.addAll((allMenus.stream().filter(m -> m.getMenuId().equals(tblMenu.getParentMid())).collect(Collectors.toList())));
                    } else if (tblMenu.getNodeType().equals(AosContent.LEVEL_2)) {
                        // 如果是二级菜单直接加进去
                        secondMenus.add(tblMenu);
                    } else {
                        // 如果是根菜单把子菜单加进去
                        secondMenus.addAll((allMenus.stream().filter(m -> StringUtils.isNotBlank(m.getParentMid()) && m.getParentMid().equals(tblMenu.getMenuId())).collect(Collectors.toList())));
                    }
                }
                // 为所有的二级菜单寻找根菜单
                for (TblMenu tMenu : secondMenus) {
                    resultMenu.addAll(allMenus.stream().filter(m -> StringUtils.isNotBlank(m.getMenuId()) && m.getMenuId().equals(tMenu.getParentMid())).collect(Collectors.toList()));
                }
                resultMenu.addAll(secondMenus);
                // 去除可能存在的重复
                List<TblMenu> collectMenus = resultMenu.stream().distinct().collect(Collectors.toList());
                List<TblMenu> tblMenusData = menusData(null, collectMenus);
                List<CurrentMenusDto> getmenus = getmenus(tblMenusData, tblUser);
                List<CurrentMenusDto> currentMenusDtos = new ArrayList<>();
                getmenus.forEach((menusDto) -> {
                    if (menusDto.getChildren().size() > 0) {
                        currentMenusDtos.add(menusDto);
                    }
                });
                return currentMenusDtos;
            }
        }
        return new ArrayList<>();
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
                // 先获取所有节点
                List<TblMenu> allMenus = tblMenuMapper.selectAllMenus();
                // 查询出所有的根节点
                List<TblMenu> secondMenus = new ArrayList<>();
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
                // 查找所有的二级节点
                for (TblMenu tblMenu : menuList) {
                    if (tblMenu.getNodeType().equals(AosContent.MENU_BTN)) {
                        // 如果当前节点是按钮直接加入
                        powerList.add(tblMenu);
                    } else if (tblMenu.getNodeType().equals(AosContent.LEVEL_2)) {
                        // 如果是二级菜单  直接加入
                        secondMenus.add(tblMenu);
                    } else {
                        // 如果是根菜单需要寻找二级菜单
                        secondMenus.addAll((allMenus.stream().filter(m -> StringUtils.isNotBlank(m.getParentMid()) && m.getParentMid().equals(tblMenu.getMenuId())).collect(Collectors.toList())));
                    }
                }
                // 循环将二级菜单对应的权限加入
                for (TblMenu tMenu : secondMenus) {
                    powerList.addAll(allMenus.stream().filter(m -> StringUtils.isNotBlank(m.getParentMid()) && m.getParentMid().equals(tMenu.getMenuId())).collect(Collectors.toList()));
                }
                // 去除可能的重复
                List<TblMenu> tblMenuList = powerList.stream().distinct().collect(Collectors.toList());
                List<String> pathList = tblMenuList.stream().filter(m -> m.getNodeType().equals(AosContent.MENU_BTN)).map(TblMenu::getPath).collect(Collectors.toList());
                return pathList.toArray(new String[]{});
            }
        }
        return new String[0];
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

