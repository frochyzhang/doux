package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblApiPermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblApiPermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-16
 */
@Service
public class TblApiPermissionInfoServiceImpl implements TblApiPermissionInfoService {
    @Autowired
    TblApiPermissionInfoMapper permissionInfoMapper;

    @Override
    public TblApiPermissionInfo getPromissionInfo(String requestUrl) {
        TblApiPermissionInfo TblApiPermissionInfo = permissionInfoMapper.selectByRequestUrl(requestUrl);
        return TblApiPermissionInfo;
    }
}
