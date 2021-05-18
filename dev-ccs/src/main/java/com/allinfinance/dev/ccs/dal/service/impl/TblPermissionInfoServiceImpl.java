package com.allinfinance.dev.ccs.dal.service.impl;


import com.allinfinance.dev.ccs.dal.mapper.TblPermissionInfoMapper;
import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblPermissionInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TblPermissionInfoServiceImpl extends ServiceImpl<TblPermissionInfoMapper, TblPermissionInfo> implements TblPermissionInfoService {
    TblPermissionInfoMapper permissionInfoMapper;
    @Override
    public TblPermissionInfo getPromissionInfo(String requestUrl) {
        TblPermissionInfo tblPermissionInfo=permissionInfoMapper.selectOne(Wrappers.<TblPermissionInfo>query().lambda().eq(TblPermissionInfo::getUrl, requestUrl));
        return tblPermissionInfo;
    }
}
