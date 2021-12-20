package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblBankManageMapper;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
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
@Service
public class TblBankManageServiceImpl implements TblBankManageService {
    @Autowired
    private TblBankManageMapper tblBankManageMapper;

    @Override
    public int deleteByPrimaryKey(BankManageReqParam bankManageReqParam) {
        int i = 1;
        for (String bankId : bankManageReqParam.getBankIds()) {
            i = tblBankManageMapper.deleteByPrimaryKey(bankId);
            //断言 如果存在更新失败，则抛出异常？？
            assert i == 0;
        }
        return i;
    }

    @Override
    public int insert(TblBankManage record) {
        return insertSelective(record);
    }

    @Override
    public int insertSelective(TblBankManage record) {
        record.setCreateTime(new Date());
        record.setBankId(IdUtils.getId());
        return tblBankManageMapper.insertSelective(record);
    }

    @Override
    public TblBankManage selectByPrimaryKey(Integer bankId) {
        return tblBankManageMapper.selectByPrimaryKey(bankId);
    }

    @Override
    public int updateByPrimaryKeySelective(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblBankManage record) {
        record.setUpdateTime(new Date());
        return tblBankManageMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<TblBankManage> pageSelectBanks(BankManageReqParam bankReqParam) {
        PageHelper.startPage(bankReqParam.getCurrent(), bankReqParam.getPageSize());
        List<TblBankManage> banks = tblBankManageMapper.pageSelectBanks(bankReqParam);
        return new PageInfo<TblBankManage>(banks);
    }

    @Override
    public List<TblBankManage> selectByBankInfo(BankManageReqParam bankReqParam) {
        return tblBankManageMapper.selectBank(bankReqParam);
    }

    @Override
    public TblBankManage selectBankInfoByOrg(String org) {
        return tblBankManageMapper.selectBankByOrg(org);
    }
}
