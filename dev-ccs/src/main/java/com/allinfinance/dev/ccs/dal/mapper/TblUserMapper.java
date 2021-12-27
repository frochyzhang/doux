package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblUserMapper {
    long countByExample(TblUserExample example);

    int deleteByPrimaryKey(String userId);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    List<TblUser> selectByExample(TblUserExample example);

    TblUser selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") TblUser record, @Param("example") TblUserExample example);

    int updateByExample(@Param("record") TblUser record, @Param("example") TblUserExample example);

    int updateByPrimaryKeySelective(TblUser record);

    int updateByPrimaryKey(TblUser record);
}