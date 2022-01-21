package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.constant.XxlJobResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLogGlue;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogGlueDao;
import com.allinfinance.dev.xxl.job.admin.dto.JobGlueIndexInfoResponseDTO;
import com.xxl.job.core.glue.GlueTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * job code controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(value = "JobCodeController", tags = {"GLUE模式代码接口"})
@RestController
@RequestMapping("/job/codes")
public class JobCodeController {
    private static final Logger logger = LoggerFactory.getLogger(JobCodeController.class);

    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobLogGlueDao xxlJobLogGlueDao;

    @GetMapping("{jobId}")
    @ApiOperation("查询GLUE任务信息")
    public Result index(@PathVariable int jobId) {
        logger.info("查询GLUE任务信息, jobId: {}", jobId);
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobId);
        List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueDao.findByJobId(jobId);

        if (jobInfo == null) {
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EXIST);
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
            return Result.failure(XxlJobResultCodeEnum.JOB_GLUE_TYPE_INVALID);
        }

        // TODO: 2022/1/4 权限控制待定
        // valid permission
        //JobInfoController.validPermission(request, jobInfo.getJobGroup());

        JobGlueIndexInfoResponseDTO jobGlueIndexInfoResponseDTO = new JobGlueIndexInfoResponseDTO();
        jobGlueIndexInfoResponseDTO.setGlueTypeList(Arrays.stream(GlueTypeEnum.values()).map(GlueTypeEnum::getDesc)
                .collect(Collectors.toList()));
        jobGlueIndexInfoResponseDTO.setJobInfo(jobInfo);
        jobGlueIndexInfoResponseDTO.setJobLogGlues(jobLogGlues);

        logger.debug("GLUE任务信息: {}", jobGlueIndexInfoResponseDTO);
        return Result.success(jobGlueIndexInfoResponseDTO);
    }

    @PutMapping
    @ApiOperation("保存GLUE任务代码")
    public Result save(@RequestParam(name = "jobId") int jobId, @RequestParam(name = "glueSource") String glueSource,
                       @RequestParam(name = "glueRemark") String glueRemark) {
        logger.info("保存GLUE任务信息, jobId: {}, 源码备注: {}", jobId, glueRemark);
        logger.debug("GLUE任务源码: {}", glueSource);
        // valid
        if (glueRemark == null) {
            return Result.failure(ResultCodeEnum.PARAM_IS_BLANK);
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            return Result.failure(XxlJobResultCodeEnum.GLUE_REMARK_LENGTH_LIMIT);
        }
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
        if (xxlJobInfo == null) {
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EXIST);
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

        logger.info("保存GLUE任务信息完成");
        return Result.success();
    }
}
