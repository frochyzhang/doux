package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblPermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblPermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2021-05-16
 */
@Service
public class TblPermissionInfoServiceImpl implements TblPermissionInfoService {
    @Autowired
    TblPermissionInfoMapper permissionInfoMapper;
    @Override
    public TblPermissionInfo getPromissionInfo(String requestUrl) {
        TblPermissionInfo tblPermissionInfo=permissionInfoMapper.selectByRequestUrl( requestUrl);
        return tblPermissionInfo;
    }
}
