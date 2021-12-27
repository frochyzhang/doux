package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 11:50
 */
@Service
public class TblRoleAuthServiceImpl implements TblRoleAuthService {

    @Autowired
    TblRoleAuthMapper tblRoleAuthMapper;

    @Override
    public int deleteByPrimaryKey(TblRoleAuthKey key) {
        return tblRoleAuthMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int insert(TblRoleAuth record) {
        return tblRoleAuthMapper.deleteByPrimaryKey(record);
    }

    @Override
    public int insertSelective(TblRoleAuth record) {
        return tblRoleAuthMapper.insertSelective(record);
    }

    @Override
    public TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key) {
        return tblRoleAuthMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(TblRoleAuth record) {
        return tblRoleAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblRoleAuth record) {
        return tblRoleAuthMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<TblRoleAuth> selectRoleAuths() {
        return tblRoleAuthMapper.selectByExample(new TblRoleAuthExample());
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return tblRoleAuthMapper.deleteByRoleId(roleId);
    }

    @Override
    public List<TblRoleAuth> selectOnUseAuths(AuthReqParam authReqParam) {
        return tblRoleAuthMapper.selectOnUseAuth(new ArrayList<>(Arrays.asList(authReqParam.getAuthIds())));
    }

}
