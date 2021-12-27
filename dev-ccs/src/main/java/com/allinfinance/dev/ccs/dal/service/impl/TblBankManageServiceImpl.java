package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblBankManageMapper;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.model.TblBankManageExample;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author ：Li
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
        TblBankManageExample tblBankManageExample = new TblBankManageExample();
        TblBankManageExample.Criteria criteria = tblBankManageExample.createCriteria();
        criteria.andBankIdIn(Arrays.asList(bankManageReqParam.getBankIds()));
        TblBankManage tblBankManage = new TblBankManage();
        tblBankManage.setIsAvailable(AosContent.IS_AVAILABLE_FALSE);
        return tblBankManageMapper.updateByExampleSelective(tblBankManage, tblBankManageExample);
    }

    @Override
    public int insert(TblBankManage record) {
        return insertSelective(record);
    }

    @Override
    public int insertSelective(TblBankManage record) {
        return tblBankManageMapper.insertSelective(record);
    }

    @Override
    public TblBankManage selectByPrimaryKey(String bankId) {
        return tblBankManageMapper.selectByPrimaryKey(bankId);
    }

    @Override
    public int updateByPrimaryKeySelective(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblBankManage record) {
        return tblBankManageMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<TblBankManage> pageSelectBanks(BankManageReqParam bankReqParam) {
        PageHelper.startPage(bankReqParam.getCurrent(), bankReqParam.getPageSize());
        TblBankManageExample tblBankManageExample = new TblBankManageExample();
        TblBankManageExample.Criteria criteria = tblBankManageExample.createCriteria();
        if (StringUtils.isNotBlank(bankReqParam.getBankName())) {
            criteria.andBankNameEnEqualTo(bankReqParam.getBankName());
        } else if (StringUtils.isNotBlank(bankReqParam.getOrg())) {
            criteria.andOrgEqualTo(bankReqParam.getOrg());
        }
        List<TblBankManage> banks = tblBankManageMapper.selectByExample(tblBankManageExample);
        return new PageInfo<>(banks);
    }

    @Override
    public List<TblBankManage> selectBankInfo(BankManageReqParam bankReqParam) {
        TblBankManageExample tblBankManageExample = new TblBankManageExample();
        TblBankManageExample.Criteria criteria = tblBankManageExample.createCriteria();
        if (StringUtils.isNotBlank(bankReqParam.getBankName())) {
            criteria.andBankNameEnEqualTo(bankReqParam.getBankName());
        } else if (StringUtils.isNotBlank(bankReqParam.getOrg())) {
            criteria.andOrgEqualTo(bankReqParam.getOrg());
        }
        return tblBankManageMapper.selectByExample(tblBankManageExample);
    }
}
