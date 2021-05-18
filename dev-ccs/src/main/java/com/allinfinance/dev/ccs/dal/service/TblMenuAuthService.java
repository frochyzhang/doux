package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
public interface TblMenuAuthService  {

    PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam);
}
