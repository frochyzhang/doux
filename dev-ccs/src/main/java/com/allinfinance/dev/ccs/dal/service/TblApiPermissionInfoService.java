package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-16
 */
public interface TblApiPermissionInfoService {
    TblApiPermissionInfo getPromissionInfo(String requestUrl);
}
