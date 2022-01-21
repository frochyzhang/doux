package com.allinfinance.dev.xxl.job.admin.dao;

import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

    // exist jobId not use jobGroup, not exist use jobGroup
    List<XxlJobLog> pageList(@Param("offset") Integer offset,
                             @Param("pageSize") Integer pageSize,
                             @Param("jobGroup") Integer jobGroup,
                             @Param("jobId") Integer jobId,
                             @Param("triggerTimeStart") Date triggerTimeStart,
                             @Param("triggerTimeEnd") Date triggerTimeEnd,
                             @Param("logStatus") Integer logStatus);

    int pageListCount(@Param("jobGroup") Integer jobGroup,
                      @Param("jobId") Integer jobId,
                      @Param("triggerTimeStart") Date triggerTimeStart,
                      @Param("triggerTimeEnd") Date triggerTimeEnd,
                      @Param("logStatus") Integer logStatus);

    XxlJobLog load(@Param("id") long id);

    long save(XxlJobLog xxlJobLog);

    int updateTriggerInfo(XxlJobLog xxlJobLog);

    int updateHandleInfo(XxlJobLog xxlJobLog);

    int delete(@Param("jobId") int jobId);

    Map<String, Object> findLogReport(@Param("from") Date from,
                                      @Param("to") Date to);

    List<Long> findClearLogIds(@Param("jobGroup") Integer jobGroup,
                               @Param("jobId") Integer jobId,
                               @Param("clearBeforeTime") Date clearBeforeTime,
                               @Param("clearBeforeNum") Integer clearBeforeNum,
                               @Param("pageSize") Integer pageSize);

    int clearLog(@Param("logIds") List<Long> logIds);

    List<Long> findFailJobLogIds(@Param("pageSize") int pageSize);

    int updateAlarmStatus(@Param("logId") long logId,
                          @Param("oldAlarmStatus") int oldAlarmStatus,
                          @Param("newAlarmStatus") int newAlarmStatus);

    List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

}
