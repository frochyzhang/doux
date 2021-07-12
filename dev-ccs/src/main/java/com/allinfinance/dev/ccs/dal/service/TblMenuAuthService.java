package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

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
