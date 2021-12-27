package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblBankManageMapper;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/19 13:51
 * @description：
 */

public interface TblBankManageService {


    int deleteByPrimaryKey(BankManageReqParam bankManageReqParam);

    int insert(TblBankManage record);

    int insertSelective(TblBankManage record);

    TblBankManage selectByPrimaryKey(String bankId);

    int updateByPrimaryKeySelective(TblBankManage record);

    int updateByPrimaryKey(TblBankManage record);

    PageInfo<TblBankManage> pageSelectBanks(BankManageReqParam bankReqParam);

    List<TblBankManage> selectBankInfo(BankManageReqParam bankReqParam);
}
