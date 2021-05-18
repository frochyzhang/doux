package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TblMenuMapper extends BaseMapper<TblMenu> {

    List<MenusReqParam> pageSelectOptMenus(@Param("menusReqParam")MenusReqParam menusReqParam );
    List<MenusReqParam> delMenuById(@Param("ids")String[] ids );
}