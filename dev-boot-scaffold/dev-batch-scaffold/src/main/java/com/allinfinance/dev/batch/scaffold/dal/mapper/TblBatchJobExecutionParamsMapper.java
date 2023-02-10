package com.allinfinance.dev.batch.scaffold.dal.mapper;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecutionParams;
import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecutionParamsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchJobExecutionParamsMapper {
    long countByExample(TblBatchJobExecutionParamsExample example);

    int insert(TblBatchJobExecutionParams record);

    int insertSelective(TblBatchJobExecutionParams record);

    List<TblBatchJobExecutionParams> selectByExample(TblBatchJobExecutionParamsExample example);

    int updateByExampleSelective(@Param("record") TblBatchJobExecutionParams record, @Param("example") TblBatchJobExecutionParamsExample example);

    int updateByExample(@Param("record") TblBatchJobExecutionParams record, @Param("example") TblBatchJobExecutionParamsExample example);
}
