package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleExample;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dto.PageableRolesQueryResponseDTO;
import com.allinfinance.dev.ccs.dto.RolesQueryResponseDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色持久层服务
 * @author: Lum Wang
 * @create: 2021-05-13 16:16
 */
 public interface TblRoleService {
    long countByExample(TblRoleExample example);

    int deleteByPrimaryKey(String roleId);

    int insert(TblRole record);

    int insertSelective(TblRole record);

    List<TblRole> selectByExample(TblRoleExample example);

    TblRole selectByPrimaryKey(String roleId);

    int updateByExampleSelective(TblRole record, TblRoleExample example);

    int updateByExample(TblRole record, TblRoleExample example);

    int updateByPrimaryKeySelective(TblRole record);

    int updateByPrimaryKey(TblRole record);

     PageInfo<PageableRolesQueryResponseDTO> pageSelectRoles(RoleReqParam roleReqParam) ;

     List<RolesQueryResponseDTO> queryRoles(RoleReqParam roleReqParam);

    TblRole selectByRoleName(String roleName);
}
