package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.common.dictionary.result.Result;
import com.allinfinance.dev.common.util.common.DateUtils;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLogReport;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogReportDao;
import com.allinfinance.dev.xxl.job.admin.dto.ChartInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.IndexInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.TriggerDayInfo;
import com.xxl.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(value = "IndexController", tags = {"首页图标数据接口"})
@RestController
@RequestMapping("/job/report")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobLogReportDao xxlJobLogReportDao;

    @ApiOperation("调度中心信息")
    @GetMapping
    public Result index() {
        logger.info("查询首页调度中心信息");
        int jobInfoCount = xxlJobInfoDao.findAllCount();
        int jobLogCount = 0;
        int jobLogSuccessCount = 0;
        XxlJobLogReport xxlJobLogReport = xxlJobLogReportDao.queryLogReportTotal();
        if (xxlJobLogReport != null) {
            jobLogCount = xxlJobLogReport.getRunningCount() + xxlJobLogReport.getSucCount() + xxlJobLogReport.getFailCount();
            jobLogSuccessCount = xxlJobLogReport.getSucCount();
        }

        // executor count
        Set<String> executorAddressSet = new HashSet<>();
        List<XxlJobGroup> groupList = xxlJobGroupDao.findAll();

        if (groupList != null && !groupList.isEmpty()) {
            for (XxlJobGroup group : groupList) {
                if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
                    executorAddressSet.addAll(group.getRegistryList());
                }
            }
        }

        int executorCount = executorAddressSet.size();

        IndexInfoResponseDTO indexInfoResponseDTO = new IndexInfoResponseDTO();
        indexInfoResponseDTO.setJobInfoCount(jobInfoCount);
        indexInfoResponseDTO.setJobLogCount(jobLogCount);
        indexInfoResponseDTO.setJobLogSuccessCount(jobLogSuccessCount);
        indexInfoResponseDTO.setExecutorCount(executorCount);

        logger.info("调度中心信息: {}", indexInfoResponseDTO);
        return Result.success(indexInfoResponseDTO);
    }

    @ApiOperation("调度任务信息汇总")
    @GetMapping("/chartInfo")
    public Result chartInfo(@RequestParam(name = "startDate", required = false) String startDate,
                            @RequestParam(name = "endDate", required = false) String endDate) {
        logger.info("查询调度中心报表信息, 开始日期: {}, 结束日期: {}", startDate, endDate);
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            logger.info("日期区间限定为空, 默认查询一周内的调度数据");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            endTime = calendar.getTime();
            //默认显示一周的调度数据
            calendar.add(Calendar.DAY_OF_MONTH, -6);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime();
        } else {
            startTime = DateUtils.parseDateString(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDateString(endDate));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            endTime = calendar.getTime();
        }

        // process
        AtomicInteger triggerCountRunningTotal = new AtomicInteger();
        AtomicInteger triggerCountSucTotal = new AtomicInteger();
        AtomicInteger triggerCountFailTotal = new AtomicInteger();

        List<XxlJobLogReport> logReportList = xxlJobLogReportDao.queryLogReport(startTime, endTime);

        ChartInfoResponseDTO chartInfoResponseDTO = new ChartInfoResponseDTO();
        if (CollectionUtils.isEmpty(logReportList)) {
            List<TriggerDayInfo> triggerDayInfoList = IntStream.rangeClosed(-6, 0)
                    .mapToObj(value -> {
                        TriggerDayInfo triggerDayInfo = new TriggerDayInfo();
                        triggerDayInfo.setTriggerDay(DateUtil.formatDate(DateUtil.addDays(new Date(), value)));
                        return triggerDayInfo;
                    }).collect(Collectors.toList());
            chartInfoResponseDTO.setTriggerDayList(triggerDayInfoList);
            return Result.success(chartInfoResponseDTO);
        }

        List<TriggerDayInfo> triggerDayInfoList = logReportList.stream()
                .map(xxlJobLogReport -> {
                    int triggerDayCountRunning = xxlJobLogReport.getRunningCount();
                    int triggerDayCountSuc = xxlJobLogReport.getSucCount();
                    int triggerDayCountFail = xxlJobLogReport.getFailCount();

                    TriggerDayInfo triggerDayInfo = new TriggerDayInfo();
                    triggerDayInfo.setTriggerDay(DateUtil.formatDate(xxlJobLogReport.getTriggerDay()));
                    triggerDayInfo.setTriggerDayCountRunning(triggerDayCountRunning);
                    triggerDayInfo.setTriggerDayCountSuc(triggerDayCountSuc);
                    triggerDayInfo.setTriggerDayCountFail(triggerDayCountFail);

                    triggerCountRunningTotal.addAndGet(triggerDayCountRunning);
                    triggerCountSucTotal.addAndGet(triggerDayCountSuc);
                    triggerCountFailTotal.addAndGet(triggerDayCountFail);

                    return triggerDayInfo;
                }).collect(Collectors.toList());

        chartInfoResponseDTO.setTriggerDayList(triggerDayInfoList);
        chartInfoResponseDTO.setTriggerCountRunningTotal(triggerCountRunningTotal.get());
        chartInfoResponseDTO.setTriggerCountSucTotal(triggerCountSucTotal.get());
        chartInfoResponseDTO.setTriggerCountFailTotal(triggerCountFailTotal.get());

        logger.debug("调度中心报表信息: {}", chartInfoResponseDTO);
        return Result.success(chartInfoResponseDTO);
    }
}
