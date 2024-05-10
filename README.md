# Dev-Framework技术文档

[TOC]

---
> 概述：
> 三部众多系统中，各技术组件版本存在不一致且杂乱的情况，无形中增加了各系统间的技术壁垒，不易于维护，且各系统各自维护第三方技术组件，对于组件的使用方式等存在差异。为了统一各技术组件，决定自研统一技术框架Dev-Framework，该框架经过多个版本迭代，易用性和可靠性层面均已得到验证，可放心使用。为方便各应用系统开发人员更好的使用Dev-Framework，特在此对框架中众多组件做如下说明。

- 版本信息
  `当前版本为： doux.version=2.0.0-RELEASE`
- 基础依赖

```xml

<dependency>
  <artifactId>dev-parent</artifactId>
  <groupId>cn.lezoo.doux</groupId>
  <version>${doux.version}</version>
</dependency>
```

## 1. 基础组件

---

### 1.1 基础组件应用层[**dev-framework**]

> 该部分主要是对基础设施层的组件做了对Frameless封装，该部分组件的使用无需依赖于springboot环境，可独立使用。
---

#### 1.1.1 dev-extension

##### a. 组件介绍

> 在日常开发中，对于同一个功能可能有多种实现方式，比较常见的就有JDBC的driver，不同的数据库厂商对于数据库连接有自己的实现方式；负载均衡有随机、轮询、加权随机等多种方式。

> 因此在一些框架运用场景中，系统可能已经默认一些实现方式，若不同的厂商或客户存在自定义的实现方式或非默认的方式，此时则无法采用对应的框架。而原生SPI的每一个扩展都需要制定一个别名，并在META-INF中已key-value的形式显示添加扩展类，非常复杂。

> 因此有必要提供一种基于SPI实现高效便捷扩展框架的方法，以通过增加映射表，实现框架的扩展功能，且非常高效便捷，降低了成本。

##### b. 使用介绍

- maven依赖

```xml

<dependency>
  <artifactId>dev-extension</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

- 使用说明

> 扩展组件的使用一共分为4个步骤: 扩展接口定义、扩展实现、声明扩展映射文件、使用具体扩展

**`定义扩展接口`**

```java 
package com.allinfinance.dev.rpc.scaffold.api;

@Extensible
public interface ProcessService {}
```

**`实现扩展接口`**

```java
package com.allinfinance.dev.rpc.scaffold.service;

@Extension("default")
public class DefaultProcessServiceImpl implements ProcessService {
}
```

**`声明扩展映射文件`**

![声明映射文件](/Users/huanghf/Downloads/声明映射文件.png)

```java
default=com.allinfinance.dev.rpc.scaffold.service.DefaultProcessServiceImpl
```

**`使用具体扩展`**

```java
ExtensionLoader<ProcessService> extensionLoader=ExtensionLoaderFactory.getExtensionLoader(ProcessService.class);
        ProcessService extension=extensionLoader.getExtension("default");
```

- **注意事项**
  被扩展的接口需要加上`@Extensible`注解标记； 扩展实现需要加上`@Extension`注解标记，同时指定扩展别名； 扩展映射文件需要加在`classpath:/META-INF/services/allinfinance`
  目录下，若同一个接口有多个实现，可在一个映射文件中体现。同时需要注意的是，映射文件中的别名与`@Extension`注解中的别名务必要保持一致。 使用扩展需要注意，该框架默认返回的`extensionLoader`
  以及`extension`均为单例，使用需注意是否该扩展是否无状态。如果要开启多例支持，需在`@Extensible`注解中指定`singleton=false`。

---

#### 1.1.2 dev-connection-driver

##### a. 组件介绍

> 在基于定制化扩展框架的基础上，设计了该连接池组件，默认具有多种池化方式，并且在连接池的上层已抽象出统一的连接交互接口，针对不同的协议交互仅需对指定扩展进行执行操作即可，提高了连接池的扩展性和扩展效率。

##### b. 使用介绍

- maven依赖

```xml

<dependency>
  <artifactId>dev-connection-driver</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
<dependency>
<artifactId>dev-connection-wrapper</artifactId>
<groupId>com.allinfinance.dev</groupId>
</dependency>

```

- 使用说明
  `引入模块`
  ![引入模块](/Users/huanghf/Downloads/引入模块.png)
  `初始化连接元数据`

```java
Properties properties=new Properties();
        PropertiesParseUtils.fromBean(properties,metadataConfigure);
        PropertiesParseUtils.fromBean(properties,connectionPoolConfigure);
        ExtensionLoader<ServerMetadataFactory> serverMetadataExtensionLoader=ExtensionLoaderFactory.getExtensionLoader(ServerMetadataFactory.class);
        ServerMetadataFactory factory=serverMetadataExtensionLoader.getExtension(connectionPoolConfigure.getConnectionPoolType());

        factory.setProperties(properties);
        return factory.getMetadata();
```

`获取连接`

```java
ServerMetadata serverMetadata=serverMetadataList.get(index);
        conn=serverMetadata.getConnection();
```

`发送数据`

```java
try{
        String response=connection.send(msg);
        if(logger.isDebugEnabled()){
        logger.debug("接收到响应：{}",response);
        }
        return response;
        }catch(Throwable e){
        return null;
        }finally{
        connection.close();
        }

```

- **注意事项**
  dev-connection-wrapper中对ServerMetadata以及ServerMetadataFactory已提供默认frameless实现，提供了基于双List模型以及双端阻塞队列模型的实现。

---

## 2. SpringBoot聚合组件[**dev-boot-starters**]

---

### 2.1 **dev-rpc-scaffold-boot-starter**

本组件提供了对sofa-rpc的封装，使用者**可以在简单的yml配置后，如同使用原生SpringBoot框架一样的享受rpc服务**。

#### 2.1.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-rpc-scaffold-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.1.2 **使用说明**

**`配置示例`**

```yaml
com:
  alipay:
    sofa:
      rpc:
        registry-address: nacos://10.250.28.142:8848/vctss-sit
        bolt-port: 12204
  allinfinance:
    rpc:
      consumer:
        common-reference-registry: 10.250.28.142:8848/public-sit
        reference-list:
          - interface-name: com.allinfinance.vctss.api.business.ChangeBindingCardService
            timeout: 5000
          - interface-name: com.allinfinance.vctss.api.tsm.TokenUpdateResultNotifyService
        common-reference-list:
          - interface-name: com.allinfinance.vctss.api.tsm.TokenUpdateResultNotifyService
            timeout: 5000
          - interface-name: com.allinfinance.vctss.api.tsm.TokenUpdateResultNotifyService
      provider:
        service-package: com.allinfinance.vctss.provider
        exclude-service-list:
          - com.allinfinance.vctss.api.business.PhysicalCardStatusUpdateService
```

##### 2.1.2.1 **对于服务提供方**

> `com.allinfinance.rpc.provider.service-package`为必需配置，所有服务实现类需使用@Component注解标识，本组件通过扫描SpringBean的方式将**service-package**包下的所有实现类发布到注册中心。如需要临时剔除某些实现类，可以在`com.allinfinance.rpc.provider.exclude-service-list`中配置需要排除的实现类。

##### 2.1.2.2 **对于服务消费方**

> `com.allinfinance.rpc.consumer.reference-list`为必需配置，使用者可以通过@Autowired注解将需要rpc服务依赖到本地实现中。

`注：无论是服务提供方还是服务消费方都需要注册中心配置`

#### 2.1.3 **配置参考手册**

##### 2.1.3.1 **注册中心配置**

> 配置前缀：`com.alipay.sofa.rpc`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| registry-address  | 注册中心地址  | String   | 无   |
| bolt-port   | 服务提供方端口  | Integer   | 无|

##### 2.1.3.2 **RpcConfigurationProperties**

> 配置类：
> `config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties`
> 配置前缀：`com.allinfinance.rpc`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| consumer   | RPC消费方配置   | Consumer   | 无   |
| provider   | RPC提供方配置   | Provider   | 无   |
| bootstrap   | RPC网关相关配置   | Bootstrap   | 无   |

##### 2.1.3.3 **RpcConfigurationProperties.Consumer**

> 配置类：
> `config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Consumer`
> 配置前缀：`com.allinfinance.rpc.consumer`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| common-reference-registry  |  公共服务所在注册中心地址 | String   | 无   |
| reference-list  |  引用接口列表，以数组形式提供  | List<Reference>   | 无   |
| common-reference-list   | 引用公共服务列表，以数组形式提供   | List<Reference>   | 无|

- 关于公共服务引用的说明 Consumer类中的`common-reference-registry`和`common-reference-list`
  配置为引用与com.alipay.sofa.rpc.registry-address配置的命名空间不同的其他命名空间的rpc服务时需要的配置

##### 2.1.3.4 **RpcConfigurationProperties.Provider**

> 配置类：
> `config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Provider`
> 配置前缀：`com.allinfinance.rpc.provider`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| exclude-service-list  |  剔除不需要对外发布的服务 | Set<String>   | 无   |
| unique-id  | 服务提供方unique-id | String   | 无  |
| service-package  | 服务实现类所在包路径 | String   | 无  |

##### 2.1.3.5 **RpcConfigurationProperties.Consumer.Reference.**

> 配置类：
> `config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Consumer.Reference`
> 配置前缀：`com.allinfinance.rpc.consumer.reference-list`、`com.allinfinance.rpc.consumer.reference-list`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| timeout  |  客户端调用超时时间 | Integer   | 30000   |
| invoke-type  |  客户端调用类型 | String   | 默认为sync，设置为future时异步调用  |
| retries  |  客户端调用失败重试次数，默认不重试 | Integer   | 0   |
| cluster  |  集群模式 | String   | 默认为failover  |
| interface-name  |  引用接口全限定类名  | String   | 无   |

---

### 2.2 **dev-dispatch-scaffold-boot-starter**

本组件提供了对xxl-job的封装，使用者在yml文件中配置好调度平台的地址和执行器的端口后，可通过xxl-job-admin的管理页面发起调度。

#### 2.2.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-dispatch-scaffold-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.2.2 **使用说明**

**`配置示例`**

```yaml
com:
  allinfinance:
    xxl:
      job:
        # 批量调度平台地址
        admin-addresses: http://10.250.28.142:9091/xxl-job-admin/
        # 批量执行器名称
        appname: ${spring.application.name}
        executor:
          log-path: ${HOME}/logs/xxl-job/jobhandler
          port: 9998
```

**`IJobHandler接口`**

```java
public interface IJobHandler {
  /**
   * 设置任务名
   *
   * @return 任务名
   */
  String dispatcherName();

  /**
   * 任务执行方法
   */
  void execute() throws Exception;
}
```

使用者将需要调度的任务实现上述`IJobHandler`接口，用不同的`dispatcherName`区分不同的调度任务，在`execute()`方法中执行具体的批量逻辑。

- 注意事项

> - 如需使用调度任务参数，可通过`XxlJobHelper.getJobParam()`方法获取
> - 任务执行方法`execute()`执行过程中抛出异常会触发xxl-job的重试机制，需要注意异常的捕获和处理

#### 2.2.3 **配置参考手册**

##### 2.2.3.1 **JobExecutorProperties**

> 配置类：
> `config.cn.lezoo.doux.dispatch.scaffold.JobExecutorProperties`
> 配置前缀：`com.allinfinance.xxl.job`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| admin-addresses  | xxl-job-admin集群地址（以逗号分隔） | String   | 无 |
| app-name | 执行器名称 | String   | allinfinance-dev3-executor  |

##### 2.2.3.2 **XxlJobCustomExecutor**

> 配置类：
> `executor.cn.lezoo.doux.dispatch.scaffold.XxlJobCustomExecutor`
> 配置前缀：`com.allinfinance.xxl.job.executor`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| address | xxl-job执行器地址（地址不存在的时候取下面的ip：port） | String   | 无 |
| ip | 执行器ip | String   | 无  |
| port | 执行器port | Integer   | 9998 |
| log-path | 执行器日志路径 | String   | ${HOME}/logs/xxl-job/jobhandler |
| logretentiondays | 执行器日志保存天数 | Integer   | 30 |
| pool-core-size | 执行器核心线程数 | Integer   | 10 |
| pool-maximum-size | 执行器最大线程数 | Integer   | 50 |

---

### 2.3 **dev-batch-scaffold-boot-starter**

本组件提供了对Spring Batch的封装，并内置Spring Batch的数据库相关表的MyBatis映射，而且依赖了`dev-dispatch-scaffold-boot-starter`
，使用者可以直接以xxl-job方式发起任务调度。

#### 2.3.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-batch-scaffold-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.3.2 **使用说明**

**`AbstractSpringBatchHandler`**

```java
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
```

**BatchJobServiceImpl`**

```java

@Component
public class BatchJobServiceImpl implements BatchJobService {
  private static final Logger logger = LoggerFactory.getLogger(BatchJobServiceImpl.class);
  @Autowired
  private JobLauncher jobLauncher;
  @Autowired
  private JobOperator jobOperator;

  /**
   * 发布一个批量任务
   *
   * @param job           任务对象
   * @param jobParameters 任务参数
   * @return JobExecution
   */
  @Override
  public JobExecution startNewJob(Job job, JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    logger.info("创建新的批量任务，任务名：{}", job.getName());
    return jobLauncher.run(job, jobParameters);
  }

  /**
   * 终止一个批量任务
   *
   * @param job           任务对象
   * @param jobParameters 任务参数
   * @return JobExecution
   */
  @Override
  public boolean stopJob(Job job, JobParameters jobParameters) throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
    Set<Long> executions = jobOperator.getRunningExecutions(job.getName());
    return jobOperator.stop(executions.iterator().next());
  }

  /**
   * 重启一个批量任务
   *
   * @param job           任务对象
   * @param jobParameters 任务参数
   * @return JobExecution
   */
  @Override
  public long restartJob(Job job, JobParameters jobParameters) throws NoSuchJobException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobParametersInvalidException, JobRestartException {
    Set<Long> executions = jobOperator.getRunningExecutions(job.getName());
    return jobOperator.restart(executions.iterator().next());
  }
}
```

**`Spring Batch Job配置示例`**

```java

@Configuration
public class BatchCutJobConfig extends AbstractBatchJob {
  @Autowired
  private ClearParameterUpdateTasklet updateTasklet;

  @Bean("batchCutJob")
  public Job batchCutJob(Step batchCutStep) {
    return jobBuilderFactory.get("batchCutJob")
            .start(batchCutStep)
            .build();
  }

  @Bean("batchCutStep")
  protected Step batchCutStep() {
    return stepBuilderFactory.get("batchCutStep")
            .tasklet(updateTasklet)
            .build();
  }
}
```

**`xxl-job调度Spring Batch Job代码示例`**

```java

@Component
public class BatchCutJobHandler extends AbstractSpringBatchHandler {
  private static final Logger logger = LoggerFactory.getLogger(BatchCutJobHandler.class);

  @Autowired
  private ClearParameterConfig clearParameterConfig;

  @Override
  public String dispatcherName() {
    return "batchCutHandler";
  }

  /**
   * 获取batch job名称
   *
   * @return job名称
   */
  @Override
  protected String jobName() {
    return "batchCutJob";
  }

  /**
   * 批量参数预处理
   *
   * @return 参数map
   * @throws Exception
   */
  @Override
  protected Map<String, JobParameter> prepareParameter() throws Exception {
    if (!BatchUtils.verifyClearDate(clearParameterConfig.getRelativeClearDate(), clearParameterConfig.getClearDate())) {
      logger.info("当日已进行日切，无需重复发起!");
      return null;
    }
    String jobParam = XxlJobHelper.getJobParam();
    logger.info("接收到批量参数：[{}]", jobParam);
    return JobParamsUtils.parseJobParamsToMap(jobParam);
  }
}
```

- 批量任务类需要继承抽象类`job.cn.lezoo.doux.batch.scaffold.AbstractBatchJob`，然后再进行任务的各种Step、Tasklet以及Listener的编排。
-

在使用xxl-job调度时，可以根据实际场景需要考虑是否用封装过后的调度抽象类`job.cn.lezoo.doux.batch.scaffold.AbstractSpringBatchHandler`
  来代替原有实现`IJobHandler`接口的形式，前者在`execute()`方法上进行了一层封装：即将批量任务执行过程中的异常捕获后不会继续向上层抛出，从而避免了批量任务再已经调度成功后又重复发起调度，造成无效地重试。

- 成功进入到调度方法后，使用`service.cn.lezoo.doux.batch.scaffold.BatchJobService`的方法来进行Spring Batch Job的发起、停止或重试。

- **注意事项**

> 参数`spring.batch.job.enabled`默认为true，会在启动时执行一次批量任务，一般需要将其关闭（置为false）。

---

### 2.4 **dev-datasource-scaffold-boot-starter**

本组件整合了MyBatis和Druid，并设置了大量的默认参数，在非特殊情况下，使用者仅需添加必要的几项参数即可使用。

#### 2.4.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-datasource-scaffold-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.4.2 **使用说明**

`配置示例`

```yaml
com:
  allinfinance:
    datasource:
      url: jdbc:db2://10.250.20.209:50000/qpsdb20:currentSchema=DB2INST1;
      username: qps
      password: qps
      druid:
        validation-query: select 1 from sysibm.sysdummy1
      mybatis:
        configuration:
          # 数据库操作类型（批量插入更新）
          default-executor-type: batch
```

- **必要参数列表**

> 配置类：
> `config.cn.lezoo.doux.datasource.scaffold.DevDatasourceProperties`
> 配置前缀：`com.allinfinance.datasource`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| url  | 数据库连接地址 | String   | 无默认值，必须填写 |
| username | 数据库用户名 | String   | 无默认值，必须填写  |
| password | 数据库用户密码 | String   | 无默认值，必须填写 |

> 配置类：
> `config.cn.lezoo.doux.datasource.scaffold.DevMybatisProperties`
> 配置前缀：`com.allinfinance.datasource.mybatis`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| mapper-locations | Mapper文件存放位置 | String   | classpath*:/mapper/*Mapper.xml |

> 配置类：
> `cn.lezoo.doux.datasource.scaffold.DruidDataSourceWrapper `
> 配置前缀：`com.allinfinance.datasource.druid`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| validation-query | 数据库连接检查语句 | String | 默认为MySQL的query语句，其他需要自行填写 |

`其余默认参数如下：`

```propertoies
com.allinfinance.datasource.type=com.alibaba.druid.pool.DruidDataSource
#============================druid's configuration======================================
com.allinfinance.datasource.druid.initial-size=5
com.allinfinance.datasource.druid.min-idle=5
com.allinfinance.datasource.druid.max-active=20
com.allinfinance.datasource.druid.max-wait=60000
com.allinfinance.datasource.druid.time-between-eviction-runs-millis=60000
com.allinfinance.datasource.druid.min-evictable-idle-time-millis=300000
com.allinfinance.datasource.druid.validation-query=SELECT 1
com.allinfinance.datasource.druid.test-while-idle=true
com.allinfinance.datasource.druid.test-on-borrow=false
com.allinfinance.datasource.druid.test-on-return=false
com.allinfinance.datasource.druid.pool-prepared-statements=true
com.allinfinance.datasource.druid.max-pool-prepared-statement-per-connection-size=20
com.allinfinance.datasource.druid.filters=stat,wall
com.allinfinance.datasource.druid.use-global-data-source-stat=true
com.allinfinance.datasource.druid.connect-properties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
com.allinfinance.datasource.druid.stat-view-servlet.login-username=admin
com.allinfinance.datasource.druid.stat-view-servlet.login-password=123456
com.allinfinance.datasource.druid.stat-view-servlet.reset-enable=false
com.allinfinance.datasource.druid.stat-view-servlet.url-pattern=/druid/*
com.allinfinance.datasource.druid.web-stat-filter.url-pattern=/*
com.allinfinance.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
#============================encryption configuration======================================
com.allinfinance.datasource.encrypt.encrypted=false
com.allinfinance.datasource.transaction.enabled=true
```

---

### 2.5 **dev-connection-boot-starter**

本组件整合了`dev-connection-wrapper`和`dev-extension`提供了默认基于netty的长连接池实现，并支持对连接池数据结构以及连接池连通性校验方法进行SPI扩展。

#### 2.5.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-connection-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.5.2 **使用说明**

`配置示例`

```yaml
com:
  allinfinance:
    connection:
      pool:
        connection-driver: hsp
        connection-pool-type: pool
        ping-service: default
      scaffold:
        server-metadata-map:
          server-1:
            server-ip: 10.250.28.239
            server-port: 6666
            max-active-connections: 50
            ping-enabled: false
            ping-query-content: "00"
            ping-verify-message: ""
            length-field: 2
            buffer-size: 65536
            default-network-timeout: 5000
          server-2:
            server-ip: 10.250.28.239
            server-port: 6666
            ping-enabled: false
            ping-query-content: "00"
            ping-verify-message: ""
            length-field: 2
            buffer-size: 65536
            default-network-timeout: 5000
```

#### 2.5.3 **配置参考手册**

##### 2.5.3.1 **ConnectionPoolConfigure**

> 配置类：
> `configure.cn.lezoo.doux.connection.pool.scaffold.ConnectionPoolConfigure`
> 配置前缀：`com.allinfinance.connection.pool`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| connection-driver  | 连接池驱动，当前提供default和hsp两种实现 | String | default |
| connection-pool-type | 连接池底层实现：双列表(pool)/阻塞队列(queue) | String | 无默认值，必须填写  |
| ping-service | 连接检查服务别名 | String | default |
| warmup | 是否开启预热 | String | true |

##### 2.5.3.2 **ScaffoldConfigure**

> 配置类：
> `configure.cn.lezoo.doux.connection.pool.scaffold.ScaffoldConfigure`
> 配置前缀：`com.allinfinance.connection.scaffold`

- **`ScaffoldConfigure`**
  `configure.cn.lezoo.doux.connection.pool.scaffold.ServerMetadataConfigure`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| server-metadata-map  | 服务端参数列表 | Map<String, ServerMetadataConfigure> | 无 |

- **`ServerMetadataConfigure`**

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| server-ip  | 服务端ip | String | 无 |
| server-port | 服务端port | String | 无 |
| max-active-connections | 最大活跃连接数 | String | 10 |
| max-idle-connections | 最大空闲连接数 | String | 5 |
| max-checkout-time | 最大空闲连接检查时间，超过该时间后检查连接活跃度，单位：毫秒 | String | 5000 |
| default-network-timeout | 请求超时时间，超过该时间表示请求超时，单位：毫秒 | String | 30 |
| retry-time-to-wait | 连接重试等待时间，单位：毫秒 | String | 10 |
| max-local-bad-connection-to-tolerance | 本地能容忍的最大无效连接数 | String | 3 |
| ping-query-content | 连接检查请求内容 | String | "" |
| ping-verify-content | 连接检查验证内容 | String | "" |
| ping-enabled | 连接检查开关 | String | true |
| ping-connections-not-used | 最近一次使用该连接的时间差，单位：毫秒 | String | 0 |
| length-field | 报文长度域长度 | String | 2 |
| buffer-size | socket缓冲区大小 | String | 65535 |

---

### 2.6 **dev-socket-server-boot-starter**

本组件整合`dev-socket-server-wrapper`
提供了默认基于netty的服务端实现，并支持对服务端接口`cn.lezoo.doux.framework.socket.server.driver.SocketServerWrapper`
进行SPI扩展。

#### 2.6.1 **maven依赖**

```xml

<dependency>
  <artifactId>dev-socket-server-boot-starter</artifactId>
  <groupId>com.allinfinance.dev</groupId>
</dependency>
```

#### 2.6.2 **使用说明**

`配置示例`

```yaml
com:
  allinfinance:
    socket:
      server:
        bootstrap:
          server-enabled: true
          bootstrap: default
        scaffold:
          server-metadata-list:
            - name: test-server
              port: 12468
              decode-msg-length: 4
              encode-msg-length: 4
              decode-charset: UTF-8
              encode-charset: UTF-8
              handler-class-name: com.allinfinance.example.socket.server.handler.TestNettyHandler
              decoder-class-name: codec.cn.lezoo.doux.infrastructure.socket.server.netty.DemuxingMessageDecoder
              encoder-class-name: codec.cn.lezoo.doux.infrastructure.socket.server.netty.DemuxingMessageEncoder
              server-driver: netty
```

#### 2.6.3 **配置参考手册**

##### 2.6.3.1 **ServerBootstrapConfigure**

> 配置类：
> `configure.cn.lezoo.doux.socket.server.scaffold.ServerBootstrapConfigure`
> 配置前缀：`com.allinfinance.socket.server.bootstrap`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| server-enabled  | 服务端启动开关 | Boolean | 无 |
| bootstrap | 服务端实现类型 | String | default |

##### 2.6.3.2 **ScaffoldConfigure**

> 配置类：
> `configure.cn.lezoo.doux.socket.server.scaffold.SocketScaffoldConfigure`
> 配置前缀：`com.allinfinance.socket.server.scaffold`

- **`SocketScaffoldConfigure`**

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| server-metadata-list | 服务端参数列表 | Map<String, ServerMetadataConfigure> | 无 |

- **`ServerMetadataConfigure`**
  `configure.cn.lezoo.doux.socket.server.scaffold.ServerMetadataConfigure`

| **配置项** | **说明** | **类型** | **默认值** |
| ------ | ------ | ------ | ------ |
| name | 服务端名称 | String | 无 |
| port | 服务端端口 | String | 无 |
| decode-msg-length | 解码报文头长度 | String | 6 |
| encode-msg-length | 编码报文头长度 | String | 6 |
| decode-charset | 解码格式 | String | UTF-8 |
| encode-charset | 编码格式 | String | UTF-8 |
| handler-class-name | 编码格式 | String | 无 |
| decoder-class-name | 编码格式 | String | DemuxingMessageDecoder全类名 |
| encoder-class-name | 编码格式 | String | DemuxingMessageEncoder全类名 |
| server-driver | 服务端驱动实现 | String | netty |

---

## 3.公共服务组件

---

### 3.1 综合网关[**dev-gateway**]

#### 3.1.1 网关注册

##### a.流程介绍

> 在应用前置exporter启动时会从nacos读取相关配置并调用网关注册服务，将需要注册的server配置信息传给网关，网关会引用exporter发布的ProcessService服务并保存配置信息和监听端口。

> 网关重启后会调用网关所在注册中心命名空间中的ProcessService服务获取应用exporter前置的bootstrap配置信息，引用ProcessService服务、保存配置信息并监听端口。

##### b.使用介绍

> 应用需实现exporter模块，引入dev-rpc-scaffold并进行网关注册相关配置

**`maven依赖`**

```xml

<dependency>
  <groupId>com.allinfinance.dev</groupId>
  <artifactId>dev-rpc-scaffold-boot-starter</artifactId>
</dependency>
```

**`实现ProcessorKeyService`**
> dev-rpc-scaffold中有默认的ProcessService实现，会在收到网关转发的请求后调用ProcessorKeyService中的getProcessorKey()方法获取该请求对应的BusinessProcessor

- getProcessorKey()方法 通过网关的ProcessRequestDTO中的请求报文获取processorKey

  ```java
  @Service
  public class ProcessorKeyServiceImpl implements ProcessorKeyService {
      private static final Logger logger = LoggerFactory.getLogger(ProcessorKeyServiceImpl.class);
  
      @Override
      public String getProcessorKey(ProcessRequestDTO processRequestDTO) {
          String processorKey;
          RequestTypeEnum requestType = processRequestDTO.getRequestType();
          switch (requestType) {
              case TCP:
                  ServiceDTO serviceDTO = XStreamUtils.xmlToBean(processRequestDTO.getRequestDTO().getRequestMsg(), ServiceDTO.class);
                  processorKey = serviceDTO.getHeader().getServiceId();
                  break;
              case HTTP:
                  HttpRequestDTO httpRequestDTO = (HttpRequestDTO) processRequestDTO.getRequestDTO();
                  processorKey = httpRequestDTO.getUrl();
                  break;
              default:
                  logger.error("不支持的请求类型, requestType: {}", requestType);
                  return null;
          }
          return processorKey;
      }
  }
  ```

**`实现BusinessProcessor`**

> dev-rpc-scaffold中有默认的processor配置，应用exporter可继承抽象的AbstractBusinessProcessor并实现processorKey()和process()方法，实现对交易的报文处理和调用后台服务

- processorKey()方法 提供应用exporter中不同processor的唯一标识。socket请求可根据请求报文中的唯一标识区分，http请求可根据url、url中的请求参数、httpHeader或请求报文中的唯一标识区分

- process()方法 对交易的报文处理、调用后台服务、组装返回给网关的统一响应ProcessResponseDTO，可在processor中获取调用后台服务的异常信息并返回相应的错误码和错误描述，防止异常信息直接抛给调用方

**`ProcessService扩展`**
> 默认的ProcessService仅实现了简单的交易分发，若需要进行其他操作例如签名验签等，则可引入dev-extension对ProcessService进行扩展

- 实现ProcessService接口

```java

@Extension("qps")
public class ProcessServiceImpl extends AbstractProcessService {
}
```

- 声明扩展映射文件
  ![声明映射文件](/Users/huanghf/Downloads/声明映射文件.png)

```java
qps=com.allinfinance.qps.exporter.process.ProcessServiceImpl
```

- 使用具体扩展 dev-rpc-scaffold中已实现对ProcessService扩展的使用，仅需在exporter配置文件中声明使用扩展的别名，见配置示例中的process-service-extension配置
  ```yaml
  process-service-extension: qps
  ```

**`exporter配置网关集群`**

```yaml
group-id: gateway
gate-cluster-address: 10.250.28.1.17:8848,10.250.28.142,10.250.28.141:8848
```

- group-id：网关集群分组标识
- gate-cluster-address：网关集群地址

**`exporter配置示例`**

```yaml
com:
  alipay:
    sofa:
      rpc:
        registry-address: nacos://10.250.28.142:8848/wk2021
        bolt-port: 38191
  allinfinance:
    rpc:
      bootstrap:
        # 网关开关
        enable: true
        # 通用网关地址
        gate-registry: 10.250.28.142:8848/wk2021
        exporter-port: 13001
        app-unique-id: ${spring.application.name}
        process-service-extension: qps
        group-id: gateway
        gate-cluster-address: 10.250.28.1.17:8848,10.250.28.142,10.250.28.141:8848
        app-list:
          - type: HTTP
            listen-port: 8034
            app-desc: "银联交易监听"
            http-config:
              url-list:
                - url: /qps-p/http/qps/cupsnicService/
                  requestMethod: POST
          - type: TCP
            listen-port: 9034
            app-desc: "核心交易监听"
```

#### 3.1.2 配置手册

##### 3.1.2.1 RpcConfigurationProperties.Bootstrap

> 配置类：`config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Bootstrap`
> 配置前缀：`com.allinfinance.rpc.bootstrap`

| 配置项 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| enable   | 是否启用网关注册配置   | Boolean   | 无   |
| exporter-port   | exporter的ProcessService服务发布的端口   | Integer   | 无   |
| gate-registry   | rpc网关注册中心地址   | String   | 无   |
| app-unique-id   | 应用exporter唯一标识   | String   | 无   |
| process-service-extension   | ProcessService扩展实现别名   | String   | default   |
| group-id   | 网关集群分组标识（需和网关配置保持一致）   | String   | 无   |
| gate-cluster-address   | 网关集群地址，之间以逗号分隔，例如：127.0.0.1:8081,127.0.0.1:8082   | String   | 无   |
| app-list   | 注册应用详细配置   | List<AppConfigList>   | 无   |

##### 3.1.2.2 RpcConfigurationProperties.Bootstrap.AppConfigList

> 配置类：`config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Bootstrap.AppConfigList`
> 配置前缀：`com.allinfinance.rpc.bootstrap.appList`

| 配置项 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| type   | 请求类型   | Enum   | TCP、HTTP   |
| app-desc   | 应用注册配置描述   | String   | 无   |
| listen-port   | 监听端口   | Integer   | 无   |
| tcp-config   | TCP server配置   | TcpConfig   | 默认TcpConfig   |
| http-config   | HTTP server配置   | HttpConfig   | Text   |

##### 3.1.2.3 RpcConfigurationProperties.Bootstrap.AppConfigList.TcpConfig

> 配置类：`config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Bootstrap.AppConfigList.TcpConfig`
> 配置前缀：`com.allinfinance.rpc.bootstrap.appList.tcpConfig`

| 配置项 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| processor-count   | 请求受理线程数量   | Integer   | 10   |
| thread-count   | 业务处理线程数量   | Integer   | 50   |
| decode-msg-length   | 接收报文长度   | Integer   | 6   |
| encode-msg-length   | 请求报文长度   | Integer   | 6   |
| decode-charset   | 接收报文编码   | String   | UTF-8   |
| encode-charset   | 请求报文编码   | String   | UTF-8   |
| buffer-size   | 缓冲池大小   | Integer   | 8192   |
| timeout   | 超时时间，单位：秒   | Integer   | 10   |
| handler-class-name   | 业务处理类   | String   | DefaultTcpIOHandler全类名   |
| decoder-class-name   | 报文解码处理类   | String   | DemuxingMessageDecoder全类名   |
| encoder-class-name   | 报文编码处理类   | String   | DemuxingMessageEncoder全类名   |
| so-linger   | 是否开启soLiner   | Boolean   | false   |

##### 3.1.2.4 RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig

> 配置类：`config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig`
> 配置前缀：`com.allinfinance.rpc.bootstrap.appList.httpConfig`

| 配置项 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| tcp-no-delay   | 是否开启TCP_NODELAY   | Boolean   | true   |
| so-re-use-addr   | 是否开启TCP_REUSEADDR   | Boolean   | true   |
| so-keep-alive   | 是否开启SO_KEEPALIVE   | Boolean   | true   |
| so-rcv-buf   | 请求缓冲池大小   | Integer   | 2048   |
| so-snd-buf   | 响应缓冲池大小   | Integer   | 2048   |
| thread-count   | 处理http请求核心线程池大小   | Integer   | Runtime.getRuntime().availableProcessors()   |
| url-list   | 处理的URL列表   | List<UrlConfig>   | 无   |

##### 3.1.2.5 RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig

> 配置类：`config.cn.lezoo.doux.rpc.scaffold.RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig`
> 配置前缀：`com.allinfinance.rpc.bootstrap.appList.httpConfig.urlList`

| 配置项 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| url   | URL   | String   | 无   |
| request-method   | 请求类型   | org.springframework.http.HttpMethod   | 无   |

#### 3.1.3 网关交易分发

##### a.socket请求

> socket server在注册时会根据端口绑定appUniqueId，在收到socket请求后即可根据appUniqueId调用对应的ProcessService

##### b.http请求

> http server在注册时会保存exporter送的appUniqueId和UrlConfig信息，在收到http请求后会首先根据appUniqueId查找保存的UrlConfig信息，判断是否包含请求的url，如果包含则直接根据appUniqueId调用对应的ProcessService，如果不包含则去遍历其他应用的UrlConfig列表查找监听了这个端口的应用，然后判断该应用的url列表是否包含请求的url，如果包含则根据appUniqueId调用对应的ProcessService

#### 3.1.4 网关集群同步

> 网关集群内部同步通过sofa-jraft实现

##### a.应用注册

exporter启动时会调用网关集群leader节点的注册服务，将需要注册的配置信息送给网关，leader节点会进行网关集群内部的同步操作

##### b.应用下线

exporter下线时会调用网关集群leader节点的下线服务，将appUniqueId送给网关，leader节点会进行网关集群内部的同步操作。在进行下线操作前，会调用exporter的ProcessService服务的verify()
方法，判断exporter是否全部下线，如未全部下线则不进行下线操作。

---

### 3.2 综合客户端[**dev-ccp**]

提供了包括HttpClient、SocketClient和FileTransmitService的服务。

#### 3.2.1 使用介绍

- maven依赖

```xml

<dependency>
  <groupId>com.allinfinance.dev</groupId>
  <artifactId>dev-rpc-scaffold-boot-starter</artifactId>
</dependency>
```

- 配置示例与说明 参考dev-rpc-scaffold中关于公共服务引用的配置说明

#### 3.2.2 接口参数列表

##### 3.2.2.1 HttpClientService

```java
/**
 * http请求
 *
 * @param httpRequestDTO 请求内容封装
 * @return 响应请求结果
 */
HttpResponseDTO request(HttpRequestDTO httpRequestDTO);
```

- HttpRequestDTO

| 参数 | 说明 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| httpMethod   | http请求方法 | Enum | 无 |
| header   | http header内容 | HashMap<String, String> | 无 |
| mediaType   | 调用方提供string，实现方自行解析 | String | 无 |
| body   | 请求体 | String | 无 |
| url   | url | String | 无 |
| retryTime   | 重试次数 | Integer | 无 |
| time   | 超时时间，单位：秒 | Integer | 无 |

- HttpResponseDTO

| 参数     | 说明             | 类型    | 默认值 |
| -------- | ---------------- | ------- | ------ |
| success  | http调用是否成功 | Boolean | 无     |
| response | 响应内容         | String  | 无     |

#### 3.2.2.3 SocketService

```java
/**
 * 客户端请求
 *
 * @param socketRequestDTO 请求连接参数
 * @param message          请求内容
 * @return 请求响应
 */
SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO,String message);
```

- SocketRequestDTO

| 参数             | 说明                                                         | 类型   | 默认值      |
| ---------------- | ------------------------------------------------------------ | ------ | ----------- |
| remoteIp         | 目标ip                                                       | String | 无          |
| remotePort       | 目标端口                                                     | String | 无          |
| clientAppName    | 客户端名称（作为hashMap的key），例如：无卡非金融-qpsDiy，无卡金融：qps8583 | String | 无          |
| timeout          | 服务端超时时间                                               | String | 30000       |
| checkMac         | 是否检查mac                                                  | String | false       |
| msgLengthSize    | 报文长度                                                     | String | 6           |
| msgEncode        | 报文编码格式                                                 | String | UTF-8       |
| connectionDriver | 底层实现的选择                                               | String | socketNetty |
| socketClient     | 客户端实现                                                   | String | default     |
| soLingerEnable   | 是否开启SO_LINGER                                            | String | false       |

- SocketResponseDTO

| 参数     | 说明               | 类型    | 默认值 |
| -------- | ------------------ | ------- | ------ |
| success  | Socket调用是否成功 | Boolean | 无     |
| response | 响应内容           | String  | 无     |

#### 3.2.2.3 TransmitService

```java
/**
 * 从本地上传文件到远程
 *
 * @param requestDTO 请求信息
 * @return 传输结果
 */
TransmitResponseDTO upload(TransmitRequestDTO requestDTO);

/**
 * 从远程下载文件到本地
 *
 * @param requestDTO 请求信息
 * @return 传输结果
 */
        TransmitResponseDTO download(TransmitRequestDTO requestDTO);

/**
 * 从远程传输文件到远程
 *
 * @param requestDTO 请求信息
 * @return 传输结果
 */
        TransmitResponseDTO transmit(TransmitRequestDTO requestDTO);
```

- TransmitRequestDTO

| 参数      | 说明                                                         | 类型          | 默认值 |
| --------- | ------------------------------------------------------------ | ------------- | ------ |
| source    | 远程源信息，download和both必填                               | RemoteMessage | 无     |
| target    | 远程目标信息，upload和both必填                               | RemoteMessage | 无     |
| localPath | 本地文件存放路径                                             | String        | 无     |
| fileName  | 文件名                                                       | String        | 无     |
| timeout   | 超时时间(ms)                                                 | Integer       | 无     |
| append    | localPath是否为追加目录，true则在默认目录基础上追加localPath，false则(localPath)为自定义目录 | Boolean       | 3000   |

- RemoteMessage

| 参数         | 说明                | 类型                    | 默认值 |
| ------------ | ------------------- | ----------------------- | ------ |
| ip           | 远程用户ip          | String                  | 无     |
| port         | 远程用户端口        | Integer                 | 无     |
| username     | 远程用户名          | String                  | 无     |
| password     | 远程用户密码        | String                  | 无     |
| path         | 远程文件路径        | String                  | 无     |
| transmitMode | 传输方式：ftp、sftp | TransmitMode：FTP, SFTP | 无     |

- TransmitResponseDTO

| 参数    | 说明                     | 类型    | 默认值 |
| ------- | ------------------------ | ------- | ------ |
| success | 传输是否成功             | Boolean | 无     |
| reason  | 传输失败原因，成功则不填 | String  | 无     |

---

### 3.3 加密服务[**dev-hsp**]

提供了连接加密机进行签名验签的rpc服务

#### 3.3.1 使用介绍

- maven依赖

```xml

<dependency>
  <groupId>com.allinfinance.dev</groupId>
  <artifactId>dev-rpc-scaffold-boot-starter</artifactId>
</dependency>
```

- 配置示例与说明 参考dev-rpc-scaffold中关于公共服务引用的配置说明

#### 3.3.2 接口参数列表

#### 3.3.2.1 SignatureService

```java
/**
 * 用SM2私钥做签名--D306
 *
 * @param requestDTO 签名参数
 * @return 签名信息
 */
HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> getSignatureBySM2PrivateKey(SignatureGetBySM2PrivateKeyRequestDTO requestDTO);

/**
 * 用SM2公钥做验签--D307
 *
 * @param requestDTO 验签参数
 * @return 验签结果
 */
        HspBaseResponseDTO verifySignatureBySM2PublicKey(SignatureVerifyBySM2PublicKeyRequestDTO requestDTO);
```

- SignatureGetBySM2PrivateKeyRequestDTO

| 参数       | 说明                                  | 类型   | 是否必输 | 格式限制         |
| ---------- | ------------------------------------- | ------ | -------- | ---------------- |
| privateKey | 外部输入密钥，HEX，加密机DC00接口申请 | String | 是       | 1～65535 * 2字节 |
| certId     | 证书序列号，明文                      | String | 是       | 1～65535 / 2字节 |
| data       | 数据，明文                            | String | 是       | 1～65535 / 2字节 |

- SignatureGetBySM2PrivateKeyResponseDTO

| 参数       | 说明                 | 类型   | 格式限制 |
| ---------- | -------------------- | ------ | -------- |
| signatureR | 签名结果的R部分，HEX | String | 64字节   |
| signatureS | 签名结果的S部分，HEX | String | 64字节   |

- SignatureVerifyBySM2PublicKeyRequestDTO

| 参数            | 说明             | 类型   | 是否必输 | 格式限制         |
| --------------- | ---------------- | ------ | -------- | ---------------- |
| plainPublicKeyX | 公钥明文X, HEX   | String | 是       | 64字节           |
| plainPublicKeyY | 公钥明文Y, HEX   | String | 是       | 64字节           |
| signatureR      | 签名结果R, HEX   | String | 是       | 64字节           |
| signatureS      | 签名结果S, HEX   | String | 是       | 64字节           |
| certId          | 证书序列号，明文 | String | 是       | 1～65535 / 2字节 |
| data            | 数据，明文       | String | 是       | 1～65535 / 2字节 |

- HspBaseResponseDTO

| 参数     | 说明     | 类型    | 默认值 |
| -------- | -------- | ------- | ------ |
| success  | 是否成功 | Boolean | 无     |
| desc     | 描述     | String  | 无     |
| response | 响应数据 | Object  | 无     |

---

### 3.4 批量管理平台[**dev-job**]

***!!!FBI Warning!!!***

**To be continue**

**敬请期待！**