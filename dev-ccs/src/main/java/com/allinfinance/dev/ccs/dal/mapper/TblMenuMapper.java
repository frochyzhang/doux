package com.allinfinance.dev.ccs.dal.mapper;


import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblMenuMapper {

    List<MenusReqParam> pageSelectOptMenus(@Param("menusReqParam")MenusReqParam menusReqParam );
    List<MenusReqParam> delMenuBatch(@Param("menuIds")String[] ids );
    List<TblMenu> selectRootMenusPath(@Param("menuIds") List menuIds);
    List<TblMenu> selectMenusPathByPMid(@Param("level")String level, @Param("menuIds") List<String> menuId, @Param("parentMid")String parentMid);


    void addMenu(TblMenu tblMenu);

    void updateById(TblMenu tblMenu);

    void deleteByPrimaryKey(@Param("menuId") String menuId);

    TblUser getCurrentUserInfo(@Param("userName")String userName, @Param("org")String bank);

    List<TblMenu> selectMenus();
}