package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblRolePermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblRolePermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-16
 */
@Service
public class TblRolePermissionInfoServiceImpl implements TblRolePermissionInfoService {
    @Autowired
    TblRolePermissionInfoMapper rolePermissionInfoMapper;

    @Override
    public List<TblRolePermissionInfo> getRolePermissionInfByRoleId(String roleId) {
        List<TblRolePermissionInfo> permissionInfos = rolePermissionInfoMapper.getRolePermissionInfByRoleId(roleId);
        return permissionInfos;
    }

    @Override
    public List<TblRolePermissionInfo> getRoleIdByPermissionCode(String permissionCode) {
        return rolePermissionInfoMapper.getRoleIdByPermissionCode(permissionCode);
    }
}
