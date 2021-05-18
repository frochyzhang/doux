package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblRolePermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblRolePermissionInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TblRolePermissionInfoServiceImpl extends ServiceImpl<TblRolePermissionInfoMapper, TblRolePermissionInfo> implements TblRolePermissionInfoService {

    TblRolePermissionInfoMapper rolePermissionInfoMapper;
    @Override
    public List<TblRolePermissionInfo> getRolePermissionInfByRoleId(String roleId) {
        List<TblRolePermissionInfo> permissionInfos=rolePermissionInfoMapper.selectList(Wrappers.<TblRolePermissionInfo>lambdaQuery().eq(TblRolePermissionInfo::getRoleId, roleId));
        return permissionInfos;
    }
}
