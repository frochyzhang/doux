package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.respdto.CurrentMenusDto;
import com.allinfinance.dev.core.util.result.Result;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
public interface TblMenuService {
    PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam);

    void delMenuByIds(String[] ids);

    void deleteByPrimaryKey(String menuId);

    Result addMenu(TblMenu tblMenu, HttpServletRequest request);

    Result updateMenuById(TblMenu tblMenu,HttpServletRequest request);

    List<CurrentMenusDto> getCurrMenus(String  userId);

    String[] getCurrPowers(String  userId);

    String selectMaxMenuIdByRoot(TblMenu tblMenu);

    String selectMaxMenuId(TblMenu tblMenu);
}
