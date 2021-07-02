package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblBankManageMapper;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
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
@Service
public class TblBankManageService {
    @Autowired
    private TblBankManageMapper tblBankManageMapper;

    public int deleteByPrimaryKey(BankManageReqParam bankManageReqParam) {
        int i = 1;
        for (String bankId : bankManageReqParam.getBankIds()) {
            i = tblBankManageMapper.deleteByPrimaryKey(bankId);
            //断言 如果存在更新失败，则抛出异常？？
            assert i == 0;
        }
        return i;
    }

    public int insert(TblBankManage record) {
        return tblBankManageMapper.insert(record);
    }

    public int insertSelective(TblBankManage record) {
        record.setCreateTime(new Date());
        return tblBankManageMapper.insertSelective(record);
    }

    public TblBankManage selectByPrimaryKey(Integer bankId) {
        return tblBankManageMapper.selectByPrimaryKey(bankId);
    }

    public int updateByPrimaryKeySelective(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblBankManage record) {
        record.setUpdateTime(new Date());
        return tblBankManageMapper.updateByPrimaryKey(record);
    }

    public PageInfo<TblBankManage> pageSelectBanks(BankManageReqParam bankReqParam) {
        PageHelper.startPage(bankReqParam.getCurrent(), bankReqParam.getPageSize());
        List<TblBankManage> banks = tblBankManageMapper.pageSelectBanks(bankReqParam);
        return new PageInfo<TblBankManage>(banks);
    }

    public List<TblBankManage> selectByBankInfo(BankManageReqParam bankReqParam) {
        return tblBankManageMapper.selectBank(bankReqParam);
    }

    public TblBankManage selectBankInfoByOrg(String org) {
       return tblBankManageMapper.selectBankByOrg(org);
    }
}
