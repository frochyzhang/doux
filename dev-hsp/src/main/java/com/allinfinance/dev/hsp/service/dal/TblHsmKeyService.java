package com.allinfinance.dev.hsp.service.dal;

import com.allinfinance.dev.hsp.mapper.TblHsmKeyMapper;
import com.allinfinance.dev.hsp.model.TblHsmKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classname  com.allinfinance.dev.hsp.service.dal.TblHsmKeyService
 *
 * @Description TODO
 * @Date 2021/3/25 15:25
 * @Created by ZhangYong
 */
@Service
public class TblHsmKeyService {

    @Autowired
    private TblHsmKeyMapper tblHsmKeyMapper;

    public int deleteByPrimaryKey(Integer keyId) {
        return tblHsmKeyMapper.deleteByPrimaryKey(keyId);
    }

    public int insert(TblHsmKey record) {
        return tblHsmKeyMapper.insert(record);
    }

    public int insertSelective(TblHsmKey record) {
        return tblHsmKeyMapper.insertSelective(record);
    }

    public TblHsmKey selectByPrimaryKey(Integer keyId) {
        return tblHsmKeyMapper.selectByPrimaryKey(keyId);
    }

    public int updateByPrimaryKeySelective(TblHsmKey record) {
        return tblHsmKeyMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblHsmKey record) {
        return tblHsmKeyMapper.updateByPrimaryKey(record);
    }

    public TblHsmKey selectByKeyIndex(String keyIndex) {
        return tblHsmKeyMapper.selectByKeyIndex(keyIndex);
    }
}
