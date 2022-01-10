package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.converter.PoMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private TblRoleAuthMapper tblRoleAuthMapper;

    @Override
    public long countByExample(TblRoleAuthExample example) {
        return tblRoleAuthMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(TblRoleAuthExample example) {
        return tblRoleAuthMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(TblRoleAuthKey key) {
        return tblRoleAuthMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int insert(TblRoleAuth record) {
        return tblRoleAuthMapper.insert(record);
    }

    @Override
    public int insertSelective(TblRoleAuth record) {
        return tblRoleAuthMapper.insertSelective(record);
    }

    @Override
    public List<TblRoleAuth> selectByExample(TblRoleAuthExample example) {
        return tblRoleAuthMapper.selectByExample(example);
    }

    @Override
    public TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key) {
        return tblRoleAuthMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByExampleSelective(TblRoleAuth record, TblRoleAuthExample example) {
        return tblRoleAuthMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(TblRoleAuth record, TblRoleAuthExample example) {
        return tblRoleAuthMapper.updateByExample(record, example);
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
    public void deleteByRoleId(String roleId) {
        TblRoleAuthExample tblRoleAuthExample = new TblRoleAuthExample();
        tblRoleAuthExample.createCriteria()
                .andRoleIdEqualTo(roleId);
        tblRoleAuthMapper.deleteByExample(tblRoleAuthExample);
    }

    @Override
    public void createRoleAuthMapping(String roleId, String authId) {
        TblRoleAuth tblRoleAuth = PoMapper.INSTANCE.convertToTblRoleAuth(roleId, authId);
        tblRoleAuthMapper.insertSelective(tblRoleAuth);
    }

    @Override
    public List<TblRoleAuth> selectOnUseAuths(AuthReqParam authReqParam) {
        TblRoleAuthExample example = new TblRoleAuthExample();
        TblRoleAuthExample.Criteria criteria = example.createCriteria();
        criteria.andAuthIdIn(Arrays.asList(authReqParam.getAuthIds()));
        return tblRoleAuthMapper.selectByExample(example);
    }

    @Override
    public TblRoleAuth selectByRoleId(String roleId) {
        TblRoleAuthExample tblRoleAuthExample = new TblRoleAuthExample();
        tblRoleAuthExample.createCriteria()
                .andRoleIdEqualTo(roleId);
        return tblRoleAuthMapper.selectByExample(tblRoleAuthExample)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
