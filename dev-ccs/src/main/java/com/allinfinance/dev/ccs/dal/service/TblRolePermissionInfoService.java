package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-16
 */
public interface TblRolePermissionInfoService {
    public List<TblRolePermissionInfo> getRolePermissionInfByRoleId(String roleId);

    List<TblRolePermissionInfo> getRoleIdByPermissionCode(String permissionCode);
}
