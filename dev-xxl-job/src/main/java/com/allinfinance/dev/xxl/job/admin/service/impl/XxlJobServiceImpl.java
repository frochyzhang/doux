package com.allinfinance.dev.xxl.job.admin.service.impl;

import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLogReport;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogReportDao;
import com.allinfinance.dev.xxl.job.admin.dto.ChartInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.IndexInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.TriggerDayInfo;
import com.allinfinance.dev.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobLogReportDao xxlJobLogReportDao;

    @Override
    public IndexInfoResponseDTO dashboardInfo() {
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

        return indexInfoResponseDTO;
    }

    @Override
    public ChartInfoResponseDTO chartInfo(Date startDate, Date endDate) {
        // process
        AtomicInteger triggerCountRunningTotal = new AtomicInteger();
        AtomicInteger triggerCountSucTotal = new AtomicInteger();
        AtomicInteger triggerCountFailTotal = new AtomicInteger();

        List<XxlJobLogReport> logReportList = xxlJobLogReportDao.queryLogReport(startDate, endDate);

        ChartInfoResponseDTO chartInfoResponseDTO = new ChartInfoResponseDTO();
        if (CollectionUtils.isEmpty(logReportList)) {
            List<TriggerDayInfo> triggerDayInfoList = IntStream.rangeClosed(-6, 0)
                    .mapToObj(value -> {
                        TriggerDayInfo triggerDayInfo = new TriggerDayInfo();
                        triggerDayInfo.setTriggerDay(DateUtil.formatDate(DateUtil.addDays(new Date(), value)));
                        return triggerDayInfo;
                    }).collect(Collectors.toList());
            chartInfoResponseDTO.setTriggerDayList(triggerDayInfoList);
            return chartInfoResponseDTO;
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
        return chartInfoResponseDTO;
    }

}
