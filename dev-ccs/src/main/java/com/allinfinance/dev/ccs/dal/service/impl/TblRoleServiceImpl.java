package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblApiPermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRolePermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.utils.IdUtils;
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
public class TblRoleServiceImpl implements TblRoleService {

    @Autowired
    private TblRoleMapper tblRoleMapper;

    @Autowired
    private TblApiPermissionInfoMapper TblApiPermissionInfoMapper;

    @Autowired
    private TblRolePermissionInfoMapper tblRolePermissionInfoMapper;

    @Override
    public int deleteByPrimaryKey(String roleId){
        return tblRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(TblRole record){
        return tblRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(TblRole record){
        record.setRoleId(IdUtils.getId());
        return tblRoleMapper.insertSelective(record);
    }

    @Override
    public TblRole selectByPrimaryKey(String roleId){
        return tblRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public TblRole selectByRoleId(String roleId){
        return tblRoleMapper.selectByRoleId(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(TblRole record){
        return tblRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblRole record){
        return tblRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<TblRole> pageSelectRoles(RoleReqParam roleReqParam) {
        PageHelper.startPage(roleReqParam.getCurrent(),roleReqParam.getPageSize());
        List<TblRole> users = tblRoleMapper.pageSelectRoles(roleReqParam);
        return new PageInfo<>(users);
    }

    @Override
    public List<TblRole> pageRoles(RoleReqParam roleReqParam) {
        return tblRoleMapper.selectRoles(roleReqParam);
    }

    @Override
    public int invalidateRole(String roleId) {
        String isAvailable = AosContent.IS_AVAILABLE_FALSE;
        return tblRoleMapper.invalidateRole(roleId, isAvailable);
    }

    @Override
    public List<TblApiPermissionInfo> selectPermissionInfos() {
        return TblApiPermissionInfoMapper.selectPermissionInfos();
    }

    @Override
    public int insertRolePermissionInfoSelective(TblRolePermissionInfo tblRolePermissionInfo){
        tblRolePermissionInfo.setId(IdUtils.getId());
        return tblRolePermissionInfoMapper.insertSelective(tblRolePermissionInfo);
    }

    @Override
    public int deleteRolePermissionInfoByRoleId(String roleId) {
        return tblRolePermissionInfoMapper.deleteRolePermissionInfoByRoleId(roleId);
    }

}
