package com.allinfinance.dev.ccs.dal.mapper;


import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;

import java.util.List;

public interface TblRoleAuthMapper  {
    int deleteByPrimaryKey(TblRoleAuthKey key);

    int insert(TblRoleAuth record);

    int insertSelective(TblRoleAuth record);

    TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key);

    int updateByPrimaryKeySelective(TblRoleAuth record);

    int updateByPrimaryKey(TblRoleAuth record);

    List<TblRoleAuth> pageSelectRoleAuths(int pageNo, int pageSize);

    List<TblRoleAuth> selectByRoleId(TblRoleAuthKey tblRoleAuthKey);
}