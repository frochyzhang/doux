package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblBankManageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblBankManageMapper {
    long countByExample(TblBankManageExample example);

    int deleteByPrimaryKey(String bankId);

    int insert(TblBankManage record);

    int insertSelective(TblBankManage record);

    List<TblBankManage> selectByExample(TblBankManageExample example);

    TblBankManage selectByPrimaryKey(String bankId);

    int updateByExampleSelective(@Param("record") TblBankManage record, @Param("example") TblBankManageExample example);

    int updateByExample(@Param("record") TblBankManage record, @Param("example") TblBankManageExample example);

    int updateByPrimaryKeySelective(TblBankManage record);

    int updateByPrimaryKey(TblBankManage record);
}