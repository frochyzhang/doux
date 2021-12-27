package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblRoleMapper {
    long countByExample(TblRoleExample example);

    int deleteByPrimaryKey(String roleId);

    int insert(TblRole record);

    int insertSelective(TblRole record);

    List<TblRole> selectByExample(TblRoleExample example);

    TblRole selectByPrimaryKey(String roleId);

    int updateByExampleSelective(@Param("record") TblRole record, @Param("example") TblRoleExample example);

    int updateByExample(@Param("record") TblRole record, @Param("example") TblRoleExample example);

    int updateByPrimaryKeySelective(TblRole record);

    int updateByPrimaryKey(TblRole record);
}