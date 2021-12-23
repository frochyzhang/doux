package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblMenuMapper {
    long countByExample(TblMenuExample example);

    int deleteByPrimaryKey(String menuId);

    int insert(TblMenu record);

    int insertSelective(TblMenu record);

    List<TblMenu> selectByExample(TblMenuExample example);

    TblMenu selectByPrimaryKey(String menuId);

    int updateByExampleSelective(@Param("record") TblMenu record, @Param("example") TblMenuExample example);

    int updateByExample(@Param("record") TblMenu record, @Param("example") TblMenuExample example);

    int updateByPrimaryKeySelective(TblMenu record);

    int updateByPrimaryKey(TblMenu record);
}