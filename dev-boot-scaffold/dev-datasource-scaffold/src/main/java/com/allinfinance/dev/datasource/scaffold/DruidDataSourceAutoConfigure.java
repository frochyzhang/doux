/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.allinfinance.dev.datasource.scaffold;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.allinfinance.dev.datasource.scaffold.config.DatasourceEncryptProperties;
import com.allinfinance.dev.datasource.scaffold.properties.DruidStatProperties;
import com.allinfinance.dev.datasource.scaffold.stat.DruidFilterConfiguration;
import com.allinfinance.dev.datasource.scaffold.stat.DruidSpringAopConfiguration;
import com.allinfinance.dev.datasource.scaffold.stat.DruidStatViewServletConfiguration;
import com.allinfinance.dev.datasource.scaffold.stat.DruidWebStatFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author lihengming [89921218@qq.com]
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
@ComponentScan(basePackages = DruidDataSourceAutoConfigure.SCAFFOLD_BASE_PACKAGE_PREFIX)
@PropertySource(value = "classpath:datasource.properties")
public class DruidDataSourceAutoConfigure {
    public static final String SCAFFOLD_BASE_PACKAGE_PREFIX = "com.allinfinance.dev.datasource.scaffold";
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceAutoConfigure.class);
    @Autowired
    private DatasourceEncryptProperties datasourceEncryptProperties;
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Primary
    @ConditionalOnMissingBean
    @Bean(initMethod = "init")
    public DruidDataSourceWrapper druidDataSourceWrapper() throws Exception {
        LOGGER.info("Init DruidDataSource");
        DruidDataSourceWrapper druidDataSourceWrapper = new DruidDataSourceWrapper();
        druidDataSourceWrapper.setName("allinfinance-datasource");
        if (!ObjectUtils.isEmpty(datasourceEncryptProperties) && datasourceEncryptProperties.isEncrypted()) {
            if (!StringUtils.isEmpty(datasourceEncryptProperties.getPublicKey())) {
                druidDataSourceWrapper.setPassword(ConfigTools.decrypt(datasourceEncryptProperties.getPublicKey(), dataSourceProperties.getPassword()));
            } else {
                throw new RuntimeException("数据库密码加密已打开，但未设置加密公钥!");
            }
        }

        return druidDataSourceWrapper;
    }

    public DruidDataSourceAutoConfigure() {
        LOGGER.info("DataSource 脚手架加载成功!");
    }
}
