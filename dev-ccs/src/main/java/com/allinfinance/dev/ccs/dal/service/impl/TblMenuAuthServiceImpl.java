package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.service.TblMenuAuthService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@Service
public class TblMenuAuthServiceImpl implements TblMenuAuthService {


    @Override
    public PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam) {
        return null;
    }
}
