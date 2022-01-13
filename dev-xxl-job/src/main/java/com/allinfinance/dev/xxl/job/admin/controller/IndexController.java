package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.common.DateUtils;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.dto.ChartInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.IndexInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.service.XxlJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(value = "IndexController", tags = {"首页图标数据接口"})
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    private XxlJobService xxlJobService;

    @ApiOperation("调度中心信息")
    @GetMapping
    public Result index() {
        IndexInfoResponseDTO indexInfoResponseDTO = xxlJobService.dashboardInfo();
        return Result.success(indexInfoResponseDTO);
    }

    @ApiOperation("调度任务信息汇总")
    @GetMapping("/chartInfo")
    public Result chartInfo(@RequestParam(name = "startDate", required = false) String startDate,
                            @RequestParam(name = "endDate", required = false) String endDate) {
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
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
        ChartInfoResponseDTO chartInfoResponseDTO = xxlJobService.chartInfo(startTime, endTime);
        return Result.success(chartInfoResponseDTO);
    }
}
