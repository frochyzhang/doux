package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblBankManageMapper;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/19 13:51
 * @description：
 */
@Service
public class TblBankService {
    @Autowired
    private TblBankManageMapper tblBankManageMapper;

    public int deleteByPrimaryKey(int authId) {
        return tblBankManageMapper.deleteByPrimaryKey(authId);
    }

    public int insert(TblBankManage record) {
        return tblBankManageMapper.insert(record);
    }

    public int insertSelective(TblBankManage record) {
        return tblBankManageMapper.insertSelective(record);
    }

    public TblBankManage selectByPrimaryKey(Integer bankId) {
        return tblBankManageMapper.selectByPrimaryKey(bankId);
    }

    public int updateByPrimaryKeySelective(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKey(record);
    }

    public PageInfo<TblBankManage> pageSelectBanks(BankReqParam bankReqParam) {
        PageHelper.startPage(bankReqParam.getCurrent(), bankReqParam.getPageSize());
        List<TblBankManage> banks = tblBankManageMapper.pageSelectBanks(bankReqParam);
        return new PageInfo<TblBankManage>(banks);
    }
}
