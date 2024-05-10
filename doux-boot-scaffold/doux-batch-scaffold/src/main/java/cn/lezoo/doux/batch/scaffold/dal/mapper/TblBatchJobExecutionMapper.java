package cn.lezoo.doux.batch.scaffold.dal.mapper;

import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchJobExecution;
import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchJobExecutionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchJobExecutionMapper {
    long countByExample(TblBatchJobExecutionExample example);

    int deleteByPrimaryKey(Long jobExecutionId);

    int insert(TblBatchJobExecution record);

    int insertSelective(TblBatchJobExecution record);

    List<TblBatchJobExecution> selectByExample(TblBatchJobExecutionExample example);

    TblBatchJobExecution selectByPrimaryKey(Long jobExecutionId);

    int updateByExampleSelective(@Param("record") TblBatchJobExecution record, @Param("example") TblBatchJobExecutionExample example);

    int updateByExample(@Param("record") TblBatchJobExecution record, @Param("example") TblBatchJobExecutionExample example);

    int updateByPrimaryKeySelective(TblBatchJobExecution record);

    int updateByPrimaryKey(TblBatchJobExecution record);
}
