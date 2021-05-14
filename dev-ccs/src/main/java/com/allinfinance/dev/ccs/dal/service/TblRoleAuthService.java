package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleMapper;
import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 11:50
 */
@Service
public class TblRoleAuthService {

    @Autowired
    TblRoleAuthMapper tblRoleAuthMapper;

    public int deleteByPrimaryKey(TblRoleAuthKey key){
        return tblRoleAuthMapper.deleteByPrimaryKey(key);
    }

    public int insert(TblRoleAuth record){
        return tblRoleAuthMapper.deleteByPrimaryKey(record);
    }

    public int insertSelective(TblRoleAuth record){
        return tblRoleAuthMapper.insertSelective(record);
    }

    public TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key){
        return tblRoleAuthMapper.selectByPrimaryKey(key);
    }

    public int updateByPrimaryKeySelective(TblRoleAuth record){
        return tblRoleAuthMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblRoleAuth record){
        return tblRoleAuthMapper.updateByPrimaryKey(record);
    }

    public PageInfo<TblRoleAuth> pageSelectRoleAuths(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<TblRoleAuth> auths = tblRoleAuthMapper.pageSelectRoleAuths(pageNo,pageSize);
        return new PageInfo<TblRoleAuth>(auths);
    }
}
