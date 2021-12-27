package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.service.LoginService;
import com.allinfinance.dev.xxl.job.admin.service.XxlJobService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@RestController
public class IndexController {

    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private LoginService loginService;


    @GetMapping("/")
    public Result index(Model model) {

        Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
//        model.addAllAttributes(dashboardMap);

        return Result.success(dashboardMap);
    }

    @GetMapping("/chartInfo")
    public Result chartInfo(Date startDate, Date endDate) {
        return Result.success(xxlJobService.chartInfo(startDate, endDate));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
