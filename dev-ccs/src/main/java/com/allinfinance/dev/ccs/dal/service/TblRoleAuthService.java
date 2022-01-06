package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 11:50
 */
 public interface TblRoleAuthService {
    long countByExample(TblRoleAuthExample example);

    int deleteByExample(TblRoleAuthExample example);

    int deleteByPrimaryKey(TblRoleAuthKey key);

    int insert(TblRoleAuth record);

    int insertSelective(TblRoleAuth record);

    List<TblRoleAuth> selectByExample(TblRoleAuthExample example);

    TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key);

    int updateByExampleSelective(TblRoleAuth record, TblRoleAuthExample example);

    int updateByExample(TblRoleAuth record, TblRoleAuthExample example);

    int updateByPrimaryKeySelective(TblRoleAuth record);

    int updateByPrimaryKey(TblRoleAuth record);

     void deleteByRoleId(String roleId);

    void createRoleAuthMapping(String roleId, List<String> authIdList);

    List<TblRoleAuth> selectOnUseAuths(AuthReqParam authReqParam);
}
