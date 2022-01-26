package com.allinfinance.dev.batch.scaffold.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2022/1/25 22:48
 */
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {
//    public JobRepository createJobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
//
//        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
//        jobRepositoryFactoryBean.setDataSource(dataSource);
//        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
//        // TODO: 2022/1/25 此处应该可配置
//        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.MYSQL.name());
//
//        return jobRepositoryFactoryBean.getObject();
//    }
//
//    @Bean
//    public BatchConfigurer batchConfigurer(DataSource dataSource) {
//        return new DefaultBatchConfigurer(dataSource) {
//            @Override
//            public JobRepository getJobRepository() {
//                try {
//                    return createJobRepository();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }
}
