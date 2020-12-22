package com.allinfinance.dev.batch.quartz;

import com.allinfinance.dev.batch.model.TblBatCtl;
import com.allinfinance.dev.batch.service.TblBatCtlService;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * QuartzManager
 *
 * @author hongmr
 * @date 2017/6/12
 */
public class QuartzManager implements BeanFactoryAware {

    private final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    private final String jobFlag = "2019.2";
    private final String jobFlag1 = "0";
    @Autowired
    private IQuartzBatchService iQuartzBatchService;
    @Autowired
    private TblBatCtlService tblBatCtlService;
    private static BeanFactory beanFactory = null;

    private void reScheduleJob() {
        // 通过查询数据库里计划任务来配置计划任务
        logger.info("current reScheduleJob time: {}!", new Date());
        List<TblBatCtl> quartzList = tblBatCtlService.selectAll();
        if (quartzList != null && quartzList.size() > 0) {
            for (TblBatCtl tbcq : quartzList) {
                logger.debug("tbcq.getTriggerName() ====" + tbcq.getTriggerName() + " tbcq.getReserve1()" + tbcq.getReserve1());
                configQuatrz(tbcq);
            }
        }
    }

    private boolean configQuatrz(TblBatCtl tbcq) {
        boolean result;
        try {
            // 运行时可通过动态注入的scheduler得到trigger
            CronTrigger trigger = iQuartzBatchService.getTrigger(tbcq.getTriggerName(), Scheduler.DEFAULT_GROUP);
            // 如果计划任务已存在则调用修改方法
            if (trigger != null) {
                change(tbcq, trigger);
            } else {
                // 如果计划任务不存在并且数据库里的任务状态为可用时,则创建计划任务
                if (jobFlag.equals(tbcq.getReserve1()) && jobFlag1.equals(tbcq.getRecordStat())) {
                    this.createCronTriggerBean(tbcq);
                }
            }
            result = true;
        } catch (Exception e) {
            result = false;
            logger.error("调度计划任务异常:", e);
        }
        return result;
    }

    private void change(TblBatCtl tbcq, CronTrigger trigger)
            throws Exception {
        // 如果任务为可用
        //madw@ 2019新版批量并行修改,只执行0状态批量
        if (jobFlag.equals(tbcq.getReserve1()) && jobFlag1.equals(tbcq.getRecordStat())) {
            // 判断从DB中取得的任务时间和现在的quartz线程中的任务时间是否相等
            // 如果相等，则表示用户并没有重新设定数据库中的任务时间，这种情况不需要重新rescheduleJob
            if (!trigger.getCronExpression().equalsIgnoreCase(tbcq.getCronExpression())) {
                iQuartzBatchService.modifyCronJobTime(trigger, tbcq.getCronExpression());
            }
        } else {
            // 不可用
            iQuartzBatchService.removeCronJob(tbcq.getJobDetailName(), Scheduler.DEFAULT_GROUP, tbcq.getTriggerName(), Scheduler.DEFAULT_GROUP);
        }
    }

    /**
     * 创建/添加计划任务
     *
     * @param tbcq 计划任务配置对象
     */
    private void createCronTriggerBean(TblBatCtl tbcq) {
        // 新建一个基于Spring的管理Job类
        HashMap<String, String> jobParams = new HashMap<>(16);
        jobParams.put("sysOrgId", tbcq.getSysOrgId());
        iQuartzBatchService.addCronJob(tbcq.getJobDetailName(), Scheduler.DEFAULT_GROUP, beanFactory.getBean(tbcq.getTargetObject()).getClass(),
                tbcq.getTriggerName(), Scheduler.DEFAULT_GROUP, tbcq.getCronExpression(), jobParams);
    }

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        beanFactory = factory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
