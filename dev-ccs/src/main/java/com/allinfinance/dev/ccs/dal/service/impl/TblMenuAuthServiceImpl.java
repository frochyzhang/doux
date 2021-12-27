package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.converter.PoMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.service.TblMenuAuthService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@Service
public class TblMenuAuthServiceImpl  implements TblMenuAuthService {

    @Autowired
    private TblMenuAuthMapper tblMenuAuthMapper;

    @Override
    public long countByExample(TblMenuAuthExample example) {
        return tblMenuAuthMapper.countByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(TblMenuAuthKey key) {
        return tblMenuAuthMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int insert(TblMenuAuth record) {
        return tblMenuAuthMapper.insert(record);
    }

    @Override
    public int insertSelective(TblMenuAuth record) {
        return tblMenuAuthMapper.insertSelective(record);
    }

    @Override
    public List<TblMenuAuth> selectByExample(TblMenuAuthExample example) {
        return tblMenuAuthMapper.selectByExample(example);
    }

    @Override
    public TblMenuAuth selectByPrimaryKey(TblMenuAuthKey key) {
        return tblMenuAuthMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByExampleSelective(TblMenuAuth record, TblMenuAuthExample example) {
        return tblMenuAuthMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(TblMenuAuth record, TblMenuAuthExample example) {
        return tblMenuAuthMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(TblMenuAuth record) {
        return tblMenuAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblMenuAuth record) {
        return tblMenuAuthMapper.updateByPrimaryKey(record);
    }

    @Override
    public void deleteByAuthId(String authId) {
        TblMenuAuthExample tblMenuAuthExample = new TblMenuAuthExample();
        tblMenuAuthExample.createCriteria()
                .andAuthIdEqualTo(authId);
        tblMenuAuthMapper.deleteByExample(tblMenuAuthExample);
    }

    @Override
    public PageInfo<MenusReqParam> pageSelectOptMenus(MenusReqParam menusReqParam) {
        return null;
    }

    @Override
    public void createMenuAuthMapping(String authId, List<String> menuIdList) {
        menuIdList.forEach(menuId -> {
            TblMenuAuth tblMenuAuth = PoMapper.INSTANCE.convertToTblMenuAuth(authId, menuId);
            tblMenuAuthMapper.insertSelective(tblMenuAuth);
        });
    }
}
