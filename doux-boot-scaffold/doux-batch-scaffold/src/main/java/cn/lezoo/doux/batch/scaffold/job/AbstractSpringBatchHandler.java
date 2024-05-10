package cn.lezoo.doux.batch.scaffold.job;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.batch.scaffold.service.BatchJobService;
import cn.lezoo.doux.dispatch.scaffold.api.IJobHandler;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author qipeng
 * @date 2022/11/9 11:54
 * @desc 将泛型限定为JobParameter
 */
public abstract class AbstractSpringBatchHandler implements IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSpringBatchHandler.class);

    @Autowired
    private BatchJobService batchJobService;

    /**
     * 批量参数预处理
     *
     * @return 参数map
     * @throws Exception
     */
    protected abstract Map<String, JobParameter> prepareParameter() throws Exception;

    /**
     * 获取batch job名称
     *
     * @return job名称
     */
    protected abstract String jobName();

    /**
     * 任务执行方法
     */
    @Override
    public void execute() throws Exception {
        Map<String, JobParameter> parameter = this.prepareParameter();
        try {
            Job job = SpringUtil.getBean(jobName());
            batchJobService.startNewJob(job, new JobParameters(parameter));
            XxlJobHelper.log("任务调度成功！");
        } catch (Exception e) {
            logger.error("任务【{}】执行失败!", jobName(), e);
        }
    }
}
