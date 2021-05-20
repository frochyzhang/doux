package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    TblUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(TblUser record);

    int updateByPrimaryKey(TblUser record);

    List<TblUser> pageSelectUsers(@Param("userReqParam") UserReqParam userReqParam);

    List<TblUser> selectByNameAndOrg();
}