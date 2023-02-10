package com.allinfinance.dev.batch.scaffold.dal.mapper;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobInstance;
import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobInstanceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchJobInstanceMapper {
    long countByExample(TblBatchJobInstanceExample example);

    int deleteByPrimaryKey(Long jobInstanceId);

    int insert(TblBatchJobInstance record);

    int insertSelective(TblBatchJobInstance record);

    List<TblBatchJobInstance> selectByExample(TblBatchJobInstanceExample example);

    TblBatchJobInstance selectByPrimaryKey(Long jobInstanceId);

    int updateByExampleSelective(@Param("record") TblBatchJobInstance record, @Param("example") TblBatchJobInstanceExample example);

    int updateByExample(@Param("record") TblBatchJobInstance record, @Param("example") TblBatchJobInstanceExample example);

    int updateByPrimaryKeySelective(TblBatchJobInstance record);

    int updateByPrimaryKey(TblBatchJobInstance record);
}
