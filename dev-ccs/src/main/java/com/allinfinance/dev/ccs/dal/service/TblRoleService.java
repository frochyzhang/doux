package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblRoleMapper;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色持久层服务
 * @author: Lum Wang
 * @create: 2021-05-13 16:16
 */
@Service
public class TblRoleService {

    @Autowired
    private TblRoleMapper tblRoleMapper;

    public int deleteByPrimaryKey(Integer roleId){
        return tblRoleMapper.deleteByPrimaryKey(roleId);
    }

    public int insert(TblRole record){
        return tblRoleMapper.insert(record);
    }

    public int insertSelective(TblRole record){
        return tblRoleMapper.insertSelective(record);
    }

    public TblRole selectByPrimaryKey(String roleId){
        return tblRoleMapper.selectByPrimaryKey(roleId);
    }

    public int updateByPrimaryKeySelective(TblRole record){
        return tblRoleMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblRole record){
        return tblRoleMapper.updateByPrimaryKey(record);
    }


    public PageInfo<TblRole> pageSelectRoles(RoleReqParam roleReqParam) {
        PageHelper.startPage(roleReqParam.getCurrent(),roleReqParam.getPageSize());
        List<TblRole> users = tblRoleMapper.pageSelectRoles(roleReqParam);
        return new PageInfo<TblRole>(users);
    }
}
