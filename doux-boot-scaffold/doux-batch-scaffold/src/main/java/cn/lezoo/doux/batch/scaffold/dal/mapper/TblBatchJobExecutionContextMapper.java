package cn.lezoo.doux.batch.scaffold.dal.mapper;

import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchJobExecutionContext;
import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchJobExecutionContextExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchJobExecutionContextMapper {
    long countByExample(TblBatchJobExecutionContextExample example);

    int deleteByPrimaryKey(Long jobExecutionId);

    int insert(TblBatchJobExecutionContext record);

    int insertSelective(TblBatchJobExecutionContext record);

    List<TblBatchJobExecutionContext> selectByExampleWithBLOBs(TblBatchJobExecutionContextExample example);

    List<TblBatchJobExecutionContext> selectByExample(TblBatchJobExecutionContextExample example);

    TblBatchJobExecutionContext selectByPrimaryKey(Long jobExecutionId);

    int updateByExampleSelective(@Param("record") TblBatchJobExecutionContext record, @Param("example") TblBatchJobExecutionContextExample example);

    int updateByExampleWithBLOBs(@Param("record") TblBatchJobExecutionContext record, @Param("example") TblBatchJobExecutionContextExample example);

    int updateByExample(@Param("record") TblBatchJobExecutionContext record, @Param("example") TblBatchJobExecutionContextExample example);

    int updateByPrimaryKeySelective(TblBatchJobExecutionContext record);

    int updateByPrimaryKeyWithBLOBs(TblBatchJobExecutionContext record);

    int updateByPrimaryKey(TblBatchJobExecutionContext record);
}
