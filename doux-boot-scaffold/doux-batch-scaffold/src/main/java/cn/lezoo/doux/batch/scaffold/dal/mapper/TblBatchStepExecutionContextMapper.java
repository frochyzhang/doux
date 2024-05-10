package cn.lezoo.doux.batch.scaffold.dal.mapper;

import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchStepExecutionContext;
import cn.lezoo.doux.batch.scaffold.dal.model.TblBatchStepExecutionContextExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/25 11:11
 */
public interface TblBatchStepExecutionContextMapper {
    long countByExample(TblBatchStepExecutionContextExample example);

    int deleteByPrimaryKey(Long stepExecutionId);

    int insert(TblBatchStepExecutionContext record);

    int insertSelective(TblBatchStepExecutionContext record);

    List<TblBatchStepExecutionContext> selectByExampleWithBLOBs(TblBatchStepExecutionContextExample example);

    List<TblBatchStepExecutionContext> selectByExample(TblBatchStepExecutionContextExample example);

    TblBatchStepExecutionContext selectByPrimaryKey(Long stepExecutionId);

    int updateByExampleSelective(@Param("record") TblBatchStepExecutionContext record, @Param("example") TblBatchStepExecutionContextExample example);

    int updateByExampleWithBLOBs(@Param("record") TblBatchStepExecutionContext record, @Param("example") TblBatchStepExecutionContextExample example);

    int updateByExample(@Param("record") TblBatchStepExecutionContext record, @Param("example") TblBatchStepExecutionContextExample example);

    int updateByPrimaryKeySelective(TblBatchStepExecutionContext record);

    int updateByPrimaryKeyWithBLOBs(TblBatchStepExecutionContext record);

    int updateByPrimaryKey(TblBatchStepExecutionContext record);
}
