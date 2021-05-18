package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class TtblMenuServiceImpl  implements TblMenuService {
    TblMenuMapper tblMenuMapper;
    @Override
    public PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam) {
        PageHelper.startPage(menusReqParam.getCurrent(), menusReqParam.getPageSize());
        List<MenusReqParam> menusReqParams = tblMenuMapper.pageSelectOptMenus(menusReqParam);
        return new PageInfo<MenusReqParam>(menusReqParams);
    }
    @Override
    public void delMenuByIds(String [] ids){
        tblMenuMapper.deleteBatchIds(Arrays.asList(ids));
    }
    @Override
    public void addMenu(TblMenu tblMenu){
        tblMenuMapper.insert(tblMenu);
    }
    @Override
    public void updateMenuById(TblMenu tblMenu){
        tblMenuMapper.updateById(tblMenu);
    }
}
