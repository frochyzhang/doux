package com.allinfinance.dev.hsp.mapper;

import com.allinfinance.dev.hsp.model.TblHsmKey;

public interface TblHsmKeyMapper {
    int deleteByPrimaryKey(Integer keyId);

    int insert(TblHsmKey record);

    int insertSelective(TblHsmKey record);

    TblHsmKey selectByPrimaryKey(Integer keyId);

    int updateByPrimaryKeySelective(TblHsmKey record);

    int updateByPrimaryKey(TblHsmKey record);

    TblHsmKey selectByKeyIndex(String keyIndex);
}