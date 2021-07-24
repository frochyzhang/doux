package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    TblUser selectByPrimaryKey(String userId);

    TblUser selectByUserNameAndBank(String userName, String org);

    TblUser selectByUserName(String userName);

    int updateByPrimaryKeySelective(@Param("record") TblUser record);

    int updateByPrimaryKey(TblUser record);

    List<TblUser> pageSelectUsers(@Param("userReqParam") UserReqParam userReqParam);

    TblUser selectByNameAndOrg(@Param("userReqParam") UserReqParam userReqParam);
}