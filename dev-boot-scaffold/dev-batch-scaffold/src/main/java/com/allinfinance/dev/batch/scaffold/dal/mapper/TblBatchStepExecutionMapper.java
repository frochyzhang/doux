package com.allinfinance.dev.batch.scaffold.dal.mapper;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchStepExecution;
import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchStepExecutionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchStepExecutionMapper {
    long countByExample(TblBatchStepExecutionExample example);

    int deleteByPrimaryKey(Long stepExecutionId);

    int insert(TblBatchStepExecution record);

    int insertSelective(TblBatchStepExecution record);

    List<TblBatchStepExecution> selectByExample(TblBatchStepExecutionExample example);

    TblBatchStepExecution selectByPrimaryKey(Long stepExecutionId);

    int updateByExampleSelective(@Param("record") TblBatchStepExecution record, @Param("example") TblBatchStepExecutionExample example);

    int updateByExample(@Param("record") TblBatchStepExecution record, @Param("example") TblBatchStepExecutionExample example);

    int updateByPrimaryKeySelective(TblBatchStepExecution record);

    int updateByPrimaryKey(TblBatchStepExecution record);
}
