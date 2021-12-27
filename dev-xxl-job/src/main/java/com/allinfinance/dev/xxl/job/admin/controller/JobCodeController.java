package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLogGlue;
import com.allinfinance.dev.xxl.job.admin.core.util.I18nUtil;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogGlueDao;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * job code controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {

    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobLogGlueDao xxlJobLogGlueDao;

    @RequestMapping
    public Result index(HttpServletRequest request, int jobId) {
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobId);
        List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueDao.findByJobId(jobId);

        if (jobInfo == null) {
            throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
            throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
        }

        // valid permission
        JobInfoController.validPermission(request, jobInfo.getJobGroup());

        Map<String, Object> maps = new HashMap<>();
        // Glue类型-字典
        maps.put("GlueTypeEnum", GlueTypeEnum.values());

        maps.put("jobInfo", jobInfo);
        maps.put("jobLogGlues", jobLogGlues);
        return Result.success(maps);
    }

    @RequestMapping("/save")
    @ResponseBody
    public Result save(Model model, int id, String glueSource, String glueRemark) {
        // valid
        if (glueRemark == null) {
            return Result.failure("500", (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")));
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            return Result.failure("500", I18nUtil.getString("jobinfo_glue_remark_limit"));
        }
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        if (xxlJobInfo == null) {
            return Result.failure("500", I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }

        // update new code
        xxlJobInfo.setGlueSource(glueSource);
        xxlJobInfo.setGlueRemark(glueRemark);
        xxlJobInfo.setGlueUpdatetime(new Date());

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoDao.update(xxlJobInfo);

        // log old code
        XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
        xxlJobLogGlue.setJobId(xxlJobInfo.getId());
        xxlJobLogGlue.setGlueType(xxlJobInfo.getGlueType());
        xxlJobLogGlue.setGlueSource(glueSource);
        xxlJobLogGlue.setGlueRemark(glueRemark);

        xxlJobLogGlue.setAddTime(new Date());
        xxlJobLogGlue.setUpdateTime(new Date());
        xxlJobLogGlueDao.save(xxlJobLogGlue);

        // remove code backup more than 30
        xxlJobLogGlueDao.removeOld(xxlJobInfo.getId(), 30);

        return Result.success();
    }

}
