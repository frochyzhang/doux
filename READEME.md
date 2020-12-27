# 开发三部基础开发框架

## 基本描述

本框架目前针对批量及Socket Server进行了简化开发封装，开发人员可以根据此框架轻松搭建批量处理服务及Socket Server基础模型。



[TOC]



















## Socket通信模块

### 概述：

socket通信模块主要集成了[Spring-5.3.1](https://docs.spring.io/spring-framework/docs/current/reference/html)及[Mina-2.0.16](https://mina.apache.org) ，将复杂的mina配置放在底层，简化开发。且支持同时起多个服务，只需要添加配置文件即可，**配置文件如下：除必填项外，其余均有默认值，开发人员可按需调整**。

如参数表所示：IOHandler需由开发人员指定，除messageReceived外，其余方法均已在框架中实现，二次开发中只需实现messageReceived方法即可，decoder及encoder**默认已实现按字节读取**，报文头长度**默认6位**，编解码字符集均**默认UTF-8**，开发人员可按需调整。

### 实现方式：

基于mina实现基础Socket Server框架，在MinaApplication.run()方法中完成进程保持不退出，由调用方指定Spring基础上下文，系统启动时会按约定文件名依次加载配置文件，并根据配置文件依次初始化Mina的IOAcceptor。初始化时会根据调用方传入的IOHandler类名通过SpringConfigTool获取到已注入容器的Bean；若调用方自定义Decoder或Encoder，根据传入的${dev.socket.decoder}和${dev.socket.encoder}通过反射机制完成初始化。**注意：为简化开发，线程池默认且只实现了newChachedThreadPool这种方式；超时时间控制没有对读和写分开控制。**

### 参数表：

| 参数名                    | 数据类型    | 默认值                                                   |
| ------------------------- | ----------- | -------------------------------------------------------- |
| dev.socket.appName        | String      | dev01                                                    |
| **dev.socket.port**       | **Integer** | **必填**                                                 |
| dev.socket.processorCount | Integer     | 10                                                       |
| dev.socket.decode.length  | Integer     | 6                                                        |
| dev.socket.encode.length  | Integer     | 6                                                        |
| dev.socket.decode.charset | String      | UTF-8                                                    |
| dev.socket.encode.charset | String      | UTF-8                                                    |
| dev.socket.bufferSize     | Integer     | 8096                                                     |
| dev.socket.timeout        | Integer     | 30                                                       |
| **dev.socket.handler**    | **String**  | **必填**                                                 |
| dev.socket.decoder        | String      | com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder |
| dev.socket.encoder        | String      | com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder |

### 使用举例：

- 依赖信息：

```xml
<dependency>
	<groupId>com.allinfinance.dev</groupId>
	<artifactId>dev-socket</artifactId>
	<version>1.0</version>
</dependency>
```

- Spring配置文件(application-context-test.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.allinfinance.dev"/>

    <bean id="testMsgHandler" class="com.allinfinance.dev.socket.handler.AbstractMessageIOHandler"/>
</beans>
```

- 启动类

```java
public static void main(String[] args) {
    new ClassPathXmlApplicationContext("classpath:application-context-test.xml");
    MinaApplication.run(TestMain.class);
}
```

- IOHandler类

```java
@Override
    public void messageReceived(IoSession session, Object message) throws Exception {
//        super.messageReceived(session, message);
        String reqMsg = (String) message;
        logger.info("收到请求消息:{}", reqMsg);
        String respMsg = "【服务端响应数据】" + reqMsg;
        logger.info(Arrays.toString(respMsg.getBytes(StandardCharsets.UTF_8)));
        logger.info("返回应答消息:{}|{}", respMsg,respMsg.getBytes(StandardCharsets.UTF_8).length);
        session.write(respMsg);
    }
```

- 配置文件(socket-test.properties)

  配置文件必须满足socket-*.properties格式

```properties
dev.socket.handler=com.allinfinance.dev.socket.handler.AbstractMessageIOHandler
dev.socket.appName=dev03
dev.socket.port=4493
```

- 执行结果

![截屏2020-12-10 21.10.57](/Users/zhangyong/Library/Application Support/typora-user-images/截屏2020-12-10 21.10.57.png)





## 批量处理模块

### 概述：

批量处理模块主要是完成了对Spring batch的封装集成，通过此模块，可完成对任务的启动、停止、暂停、重启（断点，全量）、获取任务基本信息以及查询执行状态是失败及暂停状态的任务（可对其完成单个重启、批量重启等操作）。

批量触发方式已有如下实现方式：resume

- 通过Quartz实现数据库定时轮询触发，使用该触发方式，需插入数据表TBL_BAT_CTL；需在任务配置文件中插入如下配置：

  ```xml
  <import resource="batch-quartz-default-job.xml"/>
  ```

- 通过前置任务的执行状态触发当前任务，使用该触发方式，需插入数据表TBL_JOB_RELATIONS；需在任务配置文件中插入如下配置:

  ```xml
  <import resource="batch-related-default-job.xml"/>
  ```

- 其余实现方式：如联机出发、表字段状态触发，均可通过调用**任务启动**接口完成自定义触发。

固定格式的文件Reader、Writer已有如下实现：

- Reader：
  - 含有特殊分隔符的非定长文件
  - 定长文件
- Writer
  - 含有特殊分隔符的非定长文件
  - 定长文件

如下参数表所示，默认使用druid作为数据库连接池：

- jdbc.validationQuery：开发人员可根据使用数据库类型自定义；
- jdbc.config.decrypt：是否开启数据库连接密码加密功能；
- jdbc.config.decrypt.key：加密公钥（依赖上一项，上一项为true时，该项必须存在且正确，反之，该项可不出现）；
- dev.batch.mapper.basePackage：数据表与实体类映射关系接口类所在包全路径，该项必须存在且正确。；
- dev.batch.mapper.aop.expression：dao层接口切入点的SpEL表达式

### 实现方式：

批量部分主要实现了如下功能：

- mapper接口类路径支持配置

  在Mybatis的加载机制中，org.mybatis.spring.mapper.MapperScannerConfigurer的加载顺序在property-placeholder之前，无法完成完成MapperScannerConfigurer的初始化，故自定义MapperScan注解，将原有从spring容器中获取占位符属性值的方式改为从配置文件中获取，此时不受Spring的Bean加载顺序控制。举例如下：

  - 原有方式：

  ```java
  <bean id="batCtlScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  		<property name="basePackage" value="com.allinfinance.dev.batch.dao.mapper"/>
  </bean>
  ```

  - 目前调用方式：

  ```java
  @MapperScanner(basePackages = {"${dev.batch.mapper.basePackage}", CommonConstants.DEFAULT_MAPPER_PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
  ```

- 任务创建、暂停、继续、重拉、状态、基本信息

  以上均通过JobOperator实现，JobOperator依赖于JobRepository、JobLauncher、JobExecutor、JobRegistry。

### 参数表：

| 参数名                          | 数据类型 | 默认值                                | 备注                                                         |
| ------------------------------- | -------- | ------------------------------------- | ------------------------------------------------------------ |
| dev.batch.jdbc.jdbc.driver      | String   | -                                     | -                                                            |
| jdbc.url                        | String   | -                                     | -                                                            |
| jdbc.username                   | String   | -                                     | -                                                            |
| jdbc.password                   | String   | -                                     | -                                                            |
| jdbc.validationQuery            | String   | SELECT * FROM TBL_BAT_CTL             | 数据库连接验证                                               |
| jdbc.initialSize                | Integer  | 5                                     | 连接池初始化                                                 |
| jdbc.minIdle                    | Interger | 5                                     | 最小连接数                                                   |
| jdbc.maxActive                  | Integer  | 200                                   | 最大连接数                                                   |
| jdbc.maxWait                    | Integer  | 3000                                  | 最大等待连接数                                               |
| jdbc.timeBER                    | Integer  | 9000                                  | 检测间隔                                                     |
| jdbc.minTimeEI                  | Boolean  | 30000                                 | 连接最小生存时间                                             |
| jdbc.testWI                     | Boolean  | true                                  | -                                                            |
| jdbc.testOB                     | Boolean  | true                                  | -                                                            |
| jdbc.testOR                     | Boolean  | false                                 | -                                                            |
| jdbc.psCache                    | Boolean  | false                                 | PSCache开关                                                  |
| jdbc.config.decrypt             | Boolean  | false                                 | 数据库密码是否加密                                           |
| jdbc.config.decrypt.key         | String   | -                                     | 加密公钥（依赖上一项，上一项为true时，该项必须存在且正确，反之，该项可不出现） |
| dev.batch.mapper.basePackage    | String   | -                                     | mapper接口所在包路径，该项必须出现且正确                     |
| dev.batch.mapper.aop.expression | String   | execution(* com.allinfinance.*.*(..)) | dao层接口切入点的SpEL表达式                                  |

### 使用举例（使用quartz实现定时轮询调度）：

- 依赖信息

```xml
<dependency>
    <groupId>com.allinfinance.dev</groupId>
    <artifactId>dev-batch</artifactId>
    <version>1.0</version></version>
</dependency>
```

- 配置文件（db.properties）

```properties
jdbc.driver=org.postgresql.Driver
jdbc.url=jdbc:postgresql://192.168.101.196:5432/devdb
jdbc.username=dev
jdbc.password=dev
jdbc.validationQuery=
jdbc.publicKey=
dev.batch.mapper.basePackage=com.allinfinance.qps.batch.mapper
dev.batch.mapper.aop.expression=execution(* com.allinfinance.qps.batch.service.*.*(..))
```

- job配置文件

```xml
<import resource="batch-quartz-default-job.xml"/>
<batch:job id="testJob">
    <batch:step id="testJobStep">
        <batch:tasklet ref="testJobTasklet"/>
    </batch:step>
</batch:job>
```

- Spring配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.allinfinance.dev"/>
    <import resource="classpath:job.xml"/>
</beans>
```

- Quartz Job触发器

```java
package com.allinfinance.dev.batch.quartz;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.com.allinfinance.dev.core.util.StopWatch;

import java.com.allinfinance.dev.core.util.HashMap;

/**
 * @author 张勇
 * @description
 * @date 2020/12/5 19:58
 */
@Component("testCronJob")
public class TestJobQuartz implements Job {
    private static final Logger logger = LoggerFactory.getLogger(TestJobQuartz.class);

    private static final String RUN_MONTH_KEY = "run.month";

    @Autowired
    private IBasicBatchService iBasicBatchService;
    @Autowired
    private org.springframework.batch.core.Job testJob;
    @Autowired
    private org.springframework.batch.core.Job readFileJob;

    private static Long counter = 0L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("testJob start...");
        StopWatch sw = new StopWatch();
        sw.start();

        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            HashMap<String, String> jobParams = new HashMap<>();
            jobParams.put(RUN_MONTH_KEY, String.valueOf(System.currentTimeMillis()));
            String sysOrgId = jobDataMap.getString("sysOrgId");
            if (StringUtils.isEmpty(sysOrgId)) {
                logger.error("机构号不存在!");
                return;
            }

            jobParams.put("sysOrgId", sysOrgId);
            logger.info("jobParams:{}", jobParams.toString());

            JobExecution je = iBasicBatchService.startNewBatch(testJob, jobParams);
            if (null != je && !je.getStatus().isUnsuccessful()) {
                logger.info("批量执行成功: {}", testJob.getName());
            } else {
                logger.error("批量执行失败: {}", testJob.getName());
            }
        } catch (Exception e) {
            logger.error("批量执行异常!", e);
        }

        sw.stop();
        logger.info("Time elapsed:{},Execute quartz ledgerJob:{}", sw.prettyPrint(), ++counter);
    }
}
```

- Job Tasklet

```java
package com.allinfinance.dev.batch.task;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import com.allinfinance.dev.batch.dao.service.TblBatCtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 张勇
 * @description
 * @date 2020/12/5 19:54
 */
public class TestJobTasklet implements Tasklet {
    private static final Logger logger = LoggerFactory.getLogger(TestJobTasklet.class);

    private String sysOrgId;

    @Autowired
    private JobOperator jobOperator;
    @Autowired
    private TblBatCtlService tblBatCtlService;
    @Autowired
    private IBasicBatchService basicBatchService;


    public void setSysOrgId(String sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getSysOrgId() {
        return sysOrgId;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.info("机构{}的测试任务!", sysOrgId);
        for (String summaryInfo : basicBatchService.getJobSummaryInfo(stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName())) {
            logger.info("summaryInfo: {}", summaryInfo);

        }
        return RepeatStatus.FINISHED;
    }
}
```

- 启动方式

```java
public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context-test.xml");

    logger.info("Batch启动成功!");
}
```

- Job操作接口

```java
public interface IBasicBatchService {
  public JobExecution startNewBatch(Job job, HashMap<String, String> jobParams);
  Long startBatch(String jobName, String parameters) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException;
  void pauseBatch(String jobName);
  Long resumeBatch(Long jobExecutionId) throws JobInstanceAlreadyCompleteException;
  List<Long> resumeBatch(String jobName) throws JobInstanceAlreadyCompleteException;
  List<Long> resumeInBatch(List<Long> jobExecutionIds) throws JobInstanceAlreadyCompleteException;
  List<String> getJobSummaryInfo(String jobName);
  List<BatchJobDto> getJobInfoStoppedFailed();
}
```

## 常用工具接口

- @Check 字段校验

  - type：字段类型
  - length：字段长度
  - minLength：字段长度最小值
  - max Length：字段长度最大值
  - regex：字段校验正则表达式

- xml<->bean：提供了XStream和JAXB两种实现方式

  **XStream性能问题**：之前系统中XStream对象初始化会在每次调用时重新初始化，严重影响了系统性能，在此本版本中，已修复该初始化方式，将XStream对象设置为静态不可变。

  - xml->bean
  - bean->xml

- PSql中Mybatis配置文件不能出现注释。



todo-list

- 待修复“报文长度未到齐”的校验逻辑。
- mina socket client
  - start创建新job
- restart对job进行无状态关联的重启
  - pause传入参数改为jobExecutionId list
  - resume针对失败、暂停状态的job进行断点重启
- getSummaryInfo接口返回数据解析