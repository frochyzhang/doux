package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.github.pagehelper.PageInfo;

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
    long countByExample(TblMenuAuthExample example);

    int deleteByPrimaryKey(TblMenuAuthKey key);

    int insert(TblMenuAuth record);

    int insertSelective(TblMenuAuth record);

    List<TblMenuAuth> selectByExample(TblMenuAuthExample example);

    TblMenuAuth selectByPrimaryKey(TblMenuAuthKey key);

    int updateByExampleSelective(TblMenuAuth record, TblMenuAuthExample example);

    int updateByExample(TblMenuAuth record, TblMenuAuthExample example);

    int updateByPrimaryKeySelective(TblMenuAuth record);

    int updateByPrimaryKey(TblMenuAuth record);

    PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam);

    void deleteByAuthId(String authId);

    void createMenuAuthMapping(String authId, List<String> menuIdList);
}
