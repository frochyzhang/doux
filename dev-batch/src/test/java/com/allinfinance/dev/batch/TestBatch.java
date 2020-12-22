package com.allinfinance.dev.batch;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * @author 张勇
 * @description
 * @date 2020/12/4 17:06
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:application-context-test.xml")
public class TestBatch {
    private static final Logger logger = LoggerFactory.getLogger(TestBatch.class);

    @Autowired
    private IBasicBatchService iBasicBatchService;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context-test.xml");

        logger.info("Batch启动成功!");
    }

//    @Bean
//    public void test(){
//        HashMap<String, String> jobParams = new HashMap<>();
//        jobParams.put("run.month", String.valueOf(System.currentTimeMillis()));
//        jobParams.put("filePath", "/Users/zhangyong/workspace/allinfinance/dev/STMT_000064500000__E");
//
//        JobExecution je = iBasicBatchService.startNewBatch(readFileJob, jobParams);
//        if (null != je && !je.getStatus().isUnsuccessful()) {
//            logger.info("批量执行成功: {}", readFileJob.getName());
//        } else {
//            logger.error("批量执行失败: {}", readFileJob.getName());
//        }
//    }
}
