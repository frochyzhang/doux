package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblBankManageMapper {
    int deleteByPrimaryKey(Integer bankId);

    int insert(TblBankManage record);

    int insertSelective(TblBankManage record);

    TblBankManage selectByPrimaryKey(Integer bankId);

    int updateByPrimaryKeySelective(TblBankManage record);

    int updateByPrimaryKey(TblBankManage record);

    List<TblBankManage> pageSelectBanks(@Param("bankReqParam") BankReqParam bankReqParam);
}