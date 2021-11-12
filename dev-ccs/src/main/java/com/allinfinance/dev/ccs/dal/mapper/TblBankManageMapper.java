package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblBankManageMapper {
    int deleteByPrimaryKey(@Param("bankId") String bankId);

    int insertSelective(TblBankManage record);

    TblBankManage selectByPrimaryKey(Integer bankId);

    int updateByPrimaryKeySelective(TblBankManage record);

    int updateByPrimaryKey(TblBankManage record);

    List<TblBankManage> pageSelectBanks(@Param("bankReqParam") BankManageReqParam bankReqParam);

    List<TblBankManage> selectBank(@Param("bankReqParam") BankManageReqParam bankReqParam);

    TblBankManage selectBankByOrg(@Param("org") String org);
}