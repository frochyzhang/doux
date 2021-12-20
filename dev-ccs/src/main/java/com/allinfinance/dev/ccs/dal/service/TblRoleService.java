package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblPermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRolePermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
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
 public interface TblRoleService {

     int deleteByPrimaryKey(String roleId);

     int insert(TblRole record);

     int insertSelective(TblRole record);

     TblRole selectByPrimaryKey(String roleId);

     TblRole selectByRoleId(String roleId);

     int updateByPrimaryKeySelective(TblRole record);

     int updateByPrimaryKey(TblRole record);


     PageInfo<TblRole> pageSelectRoles(RoleReqParam roleReqParam) ;

     List<TblRole> pageRoles(RoleReqParam roleReqParam);

     int invalidateRole(String roleId) ;

     List<TblPermissionInfo> selectPermissionInfos();

     int insertRolePermissionInfoSelective(TblRolePermissionInfo tblRolePermissionInfo);

     int deleteRolePermissionInfoByRoleId(String roleId);

}
