# 开发三部基础开发框架

### 1. Socket Server模块

#### 1.1. 概述：

socket通信模块主要集成了[Spring-5.3.1](https://docs.spring.io/spring-framework/docs/current/reference/html)及[Mina-2.0.16](https://mina.apache.org)
，将复杂的mina配置放在底层，简化开发。且支持同时起多个服务，只需要添加配置文件即可，**配置文件如下：除必填项外，其余均有默认值，开发人员可按需调整**。

如参数表所示：IOHandler需由开发人员指定，除messageReceived外，其余方法均已在框架中实现，二次开发中只需实现messageReceived方法即可，decoder及encoder**默认已实现按字节读取**
，报文头长度**默认6位**，编解码字符集均**默认UTF-8**，开发人员可按需调整。

#### 1.2. 实现方式：

基于mina实现基础Socket Server框架，在MinaApplication.run()方法中完成进程保持不退出，由调用方指定Spring基础上下文，系统启动时会按约定文件名（socket-*
.properties）依次加载配置文件，并根据配置文件依次初始化Mina的IOAcceptor。初始化时会根据调用方传入的IOHandler类名通过SpringConfigTool获取到已注入容器的Bean；若调用方自定义Decoder或Encoder，根据传入的${dev.socket.decoder}和${dev.socket.encoder}通过反射机制完成初始化。

**注意：**

- **为简化开发，线程池默认且只实现了newFixedThreadPool这种方式；**

- **超时时间控制没有对读和写分开控制。**

- **针对高并发场景下会出现大量TIME_WAIT连接，严重影响服务端性能。由于各客户端实现方式不一，基础框架对此做了可选优化，通过${dev.socket.soLinger}对该项配置优化。**

#### 1.3. 参数表：

| 参数名                    | 数据类型    | 默认值                                                       |
| ------------------------- | ----------- | ------------------------------------------------------------ |
| dev.socket.appName        | String      | dev01                                                        |
| **dev.socket.port**       | **Integer** | **必填**                                                     |
| dev.socket.processorCount | Integer     | 10                                                           |
| dev.socket.threadCount    | Integer     | 50                                                           |
| dev.socket.decode.length  | Integer     | 6                                                            |
| dev.socket.encode.length  | Integer     | 6                                                            |
| dev.socket.decode.charset | String      | UTF-8                                                        |
| dev.socket.encode.charset | String      | UTF-8                                                        |
| dev.socket.bufferSize     | Integer     | 8096                                                         |
| dev.socket.timeout        | Integer     | 30                                                           |
| **dev.socket.handler**    | **String**  | **必填**                                                     |
| dev.socket.decoder        | String      | com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder |
| dev.socket.encoder        | String      | com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder |
| dev.socket.soLinger       | Boolean     | false                                                        |

##### 参数说明：

SO_LINGER的作用：设置函数close()关闭TCP连接时的行为。缺省close()的行为是，如果有数据残留在socket发送缓冲区中则系统将继续发送这些数据给对方，等待被确认，然后返回。有两种解决方案：

- 立即关闭连接，通过发送RST分组(而不是用正常的FIN|ACK|FIN|ACK四个分组)来关闭该连接。至于发送缓冲区中如果有未发送完的数据，则丢弃。主动关闭一方的TCP状态则跳过TIMEWAIT，直接进入CLOSED。
- 的
  将连接的关闭设置一个超时。如果socket发送缓冲区中仍残留数据，进程进入睡眠，内核进入定时状态去尽量去发送这些数据。在超时之前，如果所有数据都发送完且被对方确认，内核用正常的FIN|ACK|FIN|ACK四个分组来关闭该连接，close()
  成功返回。如果超时之时，数据仍然未能成功发送及被确认，用上述a方式来关闭此连接。close()返回EWOULDBLOCK。

#### 1.4. 使用举例：

详见[git仓库dev-0104分支dev-socket-example](http://10.250.20.182:8899/dev/dev/-/tree/dev-0104/dev-example/dev-socket-example)

#### 1.5. 注意事项：

为实现配置文件加载，需添加VM启动参数socket-config-path指定socket-*.properties文件所在路径。

### 2. 批量处理模块

#### 2.1. 概述：

批量处理模块主要是完成了对Spring batch的封装集成，通过此模块，可完成对任务的启动、停止、暂停、重启（断点，全量）、获取任务基本信息以及查询执行状态是失败及暂停状态的任务（可对其完成单个重启、批量重启等操作）。

批量触发方式已有如下实现方式：

- 通过Quartz实现数据库定时轮询触发，使用该触发方式，需插入数据表TBL_BAT_CTL；需在任务配置文件中插入如下配置：

  ```xml
  <import resource="batch-quartz-default-job.xml"/>
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

#### 2.2. 实现方式：

批量部分主要实现了如下功能：

- 任务创建、暂停、继续、重拉、状态、基本信息

  以上均通过JobOperator实现，JobOperator依赖于JobRepository、JobLauncher、JobExecutor、JobRegistry。

  - 任务创建: basicBatchService.startJob(String jobName, String parameters)
- 暂停任务: basicBatchService.pauseJob(List<Long> executionIdList)
  - 继续任务:
    - basicBatchService.resumeJob(Long jobExecutionId)
    - basicBatchService.resumeJob(List<Long> jobExecutionIdList)
    - basicBatchService.resumeJob(String jobName)
  - 任务重拉（无状态）
- 获取任务基本信息: basicBatchService.getJobSummaryInfo(List<Long> jobExecutionIdList)

#### 2.3. 使用举例（使用quartz实现定时轮询调度）：

详见[git仓库dev-0104分支dev-batch-example](http://10.250.20.182:8899/dev/dev/-/tree/dev-0104/dev-example/dev-batch-example)

### 3. 数据源模块

#### 3.1. 概述

数据源模块集成了mybatis及alibaba druid，完成了datasource、sqlSessionFactory、trasnactionmanager以及针对CRUD操作中出现的异常捕获等功能。

#### 3.2. 实现方式

- mapper接口类路径支持配置

在Mybatis的加载机制中，org.mybatis.spring.mapper.MapperScannerConfigurer的加载顺序在property-placeholder之前，无法完成完成MapperScannerConfigurer的初始化，故自定义MapperScan注解，将原有从spring容器中获取占位符属性值的方式改为从配置文件中获取，此时不受Spring的Bean加载顺序控制。举例如下：

原有方式：

```java
<bean id="batCtlScanner"class="org.mybatis.spring.mapper.MapperScannerConfigurer">
<property name="basePackage"value="com.allinfinance.dev.batch.dao.mapper"/>
</bean>
```

目前调用方式：

```java
@MapperScanner(basePackages = {"${dev.batch.mapper.basePackage}", CommonConstants.DEFAULT_MAPPER_PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
```

#### 3.3. 参数表

| 参数名                          | 数据类型 | 默认值                                | 备注                                                         |
| ------------------------------- | -------- | ------------------------------------- | ------------------------------------------------------------ |
| dev.batch.jdbc.jdbc.driver      | String   | -                                     | -                                                            |
| jdbc.url                        | String   | -                                     | -                                                            |
| jdbc.username                   | String   | -                                     | -                                                            |
| jdbc.password                   | String   | -                                     | -                                                            |
| jdbc.validationQuery            | String   | -                                     | 数据库连接验证                                               |
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
| dev.batch.mapper.aop.expression | String   | execution(* com.allinfinance.*
.*(..)) | dao层接口切入点的SpEL表达式                                  |

#### 3.4. 使用举例

详见批量处理模块定时轮训调度中实现。

### 4. RPC框架-Dubbo

#### 4.1. 概述

Dubbo是一款高性能、轻量级的JAVA RPC框架，它提供了三大核心能力：面向接口的远程方法调用、智能容错和负载均衡，以及服务自动注册和发现。

#### 4.2. 主要核心部件

**Remoting:** 网络通信框架，实现了 sync-over-async 和 request-response 消息机制.

**RPC:** 一个**远程过程调用**的抽象，支持**负载均衡**、**容灾**和**集群**功能

**Registry:** 服务目录框架用于服务的注册和服务事件发布和订阅

#### 4.3. 工作原理

![dubbo-architucture](http://dubbo.apache.org/imgs/user/dubbo-architecture.jpg)

**Provider**：暴露服务方称之为“服务提供者”。

**Consumer**：调用**远程服务**方称之为“服务消费者”。

**Registry**：服务注册与发现的中心目录服务称之为“服务注册中心”。

**Monitor**：统计服务的调用次数和调用时间的日志服务称之为“服务监控中心”。

(1) 连通性：

注册中心负责服务地址的注册与查找，相当于目录服务，服务提供者和消费者只在启动时与注册中心交互，注册中心不转发请求，压力较小

监控中心负责统计各服务调用次数，调用时间等，统计先在内存汇总后每分钟一次发送到监控中心服务器，并以报表展示

服务提供者向注册中心注册其提供的服务，并汇报调用时间到监控中心，此时间不包含网络开销

服务消费者向注册中心获取服务提供者地址列表，并根据负载算法直接调用提供者，同时汇报调用时间到监控中心，此时间包含网络开销

注册中心，服务提供者，服务消费者三者之间均为长连接，监控中心除外

注册中心通过**长连接**感知服务提供者的存在，服务提供者宕机，注册中心将立即推送事件通知消费者

注册中心和监控中心全部宕机，不影响已运行的提供者和消费者，消费者在**本地缓存**了提供者列表

注册中心和监控中心都是可选的，服务消费者可以直连服务提供者

(2) 健壮性：

监控中心宕掉不影响使用，只是丢失部分采样数据

数据库宕掉后，注册中心仍能通过缓存提供服务列表查询，但不能注册新服务

注册中心对等集群，任意一台宕掉后，将自动切换到另一台

注册中心全部宕掉后，服务提供者和服务消费者仍能通过本地缓存通讯

服务提供者无状态，任意一台宕掉后，不影响使用

服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复

(3) 伸缩性：

注册中心为对等集群，可动态增加机器部署实例，所有客户端将自动发现新的注册中心

服务提供者无状态，可动态增加机器部署实例，注册中心将推送新的服务提供者信息给消费者

#### 4.4. 特性

- 面向接口代理的高性能RPC调用

  提供高性能的基于代理的远程调用能力，服务以接口为粒度，为开发者屏蔽远程调用底层细节。

- 智能负载均衡

  内置多种负载均衡策略，智能感知下游节点健康状况，显著减少调用延迟，提高系统吞吐量。

- 服务自动注册与发现

  支持多种注册中心服务，服务实例上下线实时感知。

- 高度可扩展能力

  遵循微内核+插件的设计原则，所有核心能力如Protocol、Transport、Serialization被设计为扩展点，平等对待内置实现和第三方实现。

- 运行期流量调度

  内置条件、脚本等路由策略，通过配置不同的路由规则，轻松实现灰度发布，同机房优先等功能。

- 可视化的服务治理与运维

  提供丰富服务治理、运维工具：随时查询服务元数据、服务健康状态及调用统计，实时下发路由策略、调整配置参数。

#### 4.5. 参数表

| 参数名                      | 数据类型 | 默认值    |
| --------------------------- | -------- | --------- |
| dev.dubbo.application.name  | String   | -         |
| dev.dubbo.registry.address  | String   | -         |
| dev.dubbo.registry.protocol | String   | zookeeper |
| dev.dubbo.registry.timeout  | Integer  | 10000     |
| dev.dubbo.protocol.name     | String   | dubbo     |
| dev.dubbo.protocol.port     | Integer  | -         |
| dev.dubbo.provider.timeout  | Integer  | 10000     |
| dev.dubbo.provider.retries  | Integer  | 0         |
| dev.dubbo.consumer.check    | Boolean  | false     |
| dev.dubbo.consumer.timeout  | Integer  | 10000     |
| dev.dubbo.consumer.retries  | Integer  | 0         |

#### 4.6. 使用举例

详见[git仓库dev-0104分支dev-dubbo-example](http://10.250.20.182:8899/dev/dev/-/tree/dev-0104/dev-example/dev-dubbo-example)

### 5. 消息框架-RabbitMQ

#### 5.1. AMQP和队列角色

AMQP（高级消息队列协议）是一个网络协议。它支持符合要求的客户端应用（application）和消息中间件代理（messaging middleware broker）之间进行通信。

AMQP的实体和路由规则是由应用本身定义的，而不是由消息代理定义。包括像声明队列和交换机，定义他们之间的绑定，订阅队列等等关于协议本身的操作。

这虽然能让开发人员自由发挥，但也需要他们注意潜在的定义冲突。当然这在实践中很少会发生，如果发生，会以配置错误（misconfiguration）的形式表现出来。

应用程序（Applications）声明AMQP实体，定义需要的路由方案，或者删除不再需要的AMQP实体。

消息代理（message
brokers）从发布者（publishers）亦称生产者（producers）那儿接收消息，并根据既定的路由规则把接收到的消息发送给处理消息的消费者（consumers）。由于AMQP是一个网络协议，所以这个过程中的发布者，消费者，消息代理
可以存在于不同的设备上。

#### 5.2. 模型简介

![enter image description here](https://www.rabbitmq.com/img/tutorials/intro/hello-world-example-routing.png)

消息（message）被发布者（publisher）发送给交换机（exchange），交换机常常被比喻成邮局或者邮箱。然后交换机将收到的消息根据路由规则分发给绑定的队列（queue）。最后AMQP代理会将消息投递给订阅了此队列的消费者，或者消费者按照需求自行获取。发布者（publisher）发布消息时可以给消息指定各种消息属性（message
meta-data）。有些属性有可能会被消息代理（brokers）使用，然而其他的属性则是完全不透明的，它们只能被接收消息的应用所使用。

从安全角度考虑，网络是不可靠的，接收消息的应用也有可能在处理消息的时候失败。基于此原因，AMQP模块包含了一个消息确认（message
acknowledgements）的概念：当一个消息从队列中投递给消费者后（consumer），消费者会通知一下消息代理（broker），这个可以是自动的也可以由处理消息的应用的开发者执行。当“消息确认”被启用的时候，消息代理不会完全将消息从队列中删除，直到它收到来自消费者的确认回执（acknowledgement）。

在某些情况下，例如当一个消息无法被成功路由时，消息或许会被返回给发布者并被丢弃。或者，如果消息代理执行了延期操作，消息会被放入一个所谓的死信队列中。此时，消息发布者可以选择某些参数来处理这些特殊情况。

队列，交换机和绑定统称为AMQP实体（AMQP entities）。

#### 5.3. 交换机和交换机类型

交换机是用来发送消息的AMQP实体。交换机拿到一个消息之后将它路由给一个或零个队列。它使用哪种路由算法是由交换机类型和被称作绑定（bindings）的规则所决定的。AMQP 0-9-1的代理提供了四种交换机

| Name（交换机类型）            | Default pre-declared names（预声明的默认名称） |
| ----------------------------- | ---------------------------------------------- |
| Direct exchange（直连交换机） | (Empty string) and amq.direct                  |
| Fanout exchange（扇型交换机） | amq.fanout                                     |
| Topic exchange（主题交换机）  | amq.topic                                      |
| Headers exchange（头交换机）  | amq.match (and amq.headers in RabbitMQ)        |

除交换机类型外，在声明交换机时还可以附带许多其他的属性，其中最重要的几个分别是：

- Name
- Durability （消息代理重启后，交换机是否还存在）
- Auto-delete （当所有与之绑定的消息队列都完成了对此交换机的使用后，删掉它）
- Arguments（依赖代理本身）

交换机可以有两个状态：持久（durable）、暂存（transient）。持久化的交换机会在消息代理（broker）重启后依旧存在，而暂存的交换机则不会（它们需要在代理再次上线后重新被声明）。然而并不是所有的应用场景都需要持久化的交换机。

##### 5.3.1 直连交换机

直连型交换机（direct exchange）是根据消息携带的路由键（routing key）将消息投递给对应队列的。直连交换机用来处理消息的单播路由（unicast routing）（尽管它也可以处理多播路由）。下边介绍它是如何工作的：

- 将一个队列绑定到某个交换机上，同时赋予该绑定一个路由键（routing key）
- 当一个携带着路由键为`R`的消息被发送给直连交换机时，交换机会把它路由给绑定值同样为`R`的队列。

直连交换机经常用来循环分发任务给多个工作者（workers）。当这样做的时候，我们需要明白一点，在AMQP 0-9-1中，消息的负载均衡是发生在消费者（consumer）之间的，而不是队列（queue）之间。

直连型交换机图例：

![enter image description here](https://www.rabbitmq.com/img/tutorials/intro/exchange-direct.png)

##### 5.3.2 扇型交换机

扇型交换机（funout
exchange）将消息路由给绑定到它身上的所有队列，而不理会绑定的路由键。如果N个队列绑定到某个扇型交换机上，当有消息发送给此扇型交换机时，交换机会将消息的拷贝分别发送给这所有的N个队列。扇型用来交换机处理消息的广播路由（broadcast
routing）。

因为扇型交换机投递消息的拷贝到所有绑定到它的队列，所以他的应用案例都极其相似：

- 大规模多用户在线（MMO）游戏可以使用它来处理排行榜更新等全局事件
- 体育新闻网站可以用它来近乎实时地将比分更新分发给移动客户端
- 分发系统使用它来广播各种状态和配置更新
- 在群聊的时候，它被用来分发消息给参与群聊的用户。（AMQP没有内置presence的概念，因此XMPP可能会是个更好的选择）

扇型交换机图例：

![enter image description here](https://www.rabbitmq.com/img/tutorials/intro/exchange-fanout.png)

#### 5.4. 队列

AMQP中的队列（queue）跟其他消息队列或任务队列中的队列是很相似的：它们存储着即将被应用消费掉的消息。队列跟交换机共享某些属性，但是队列也有一些另外的属性。

- Name
- Durable（消息代理重启后，队列依旧存在）
- Exclusive（只被一个连接（connection）使用，而且当连接关闭后队列即被删除）
- Auto-delete（当最后一个消费者退订后即被删除）
- Arguments（一些消息代理用他来完成类似与TTL的某些额外功能）

队列在声明（declare）后才能被使用。如果一个队列尚不存在，声明一个队列会创建它。如果声明的队列已经存在，并且属性完全相同，那么此次声明不会对原有队列产生任何影响。如果声明中的属性与已存在队列的属性有差异，那么一个错误代码为406的通道级异常就会被抛出。

##### 5.4.1 队列名称

队列的名字可以由应用（application）来取，也可以让消息代理（broker）直接生成一个。队列的名字可以是最多255字节的一个utf-8字符串。若希望AMQP消息代理生成队列名，需要给队列的name参数赋值一个空字符串：在同一个通道（channel）的后续的方法（method）中，我们可以使用空字符串来表示之前生成的队列名称。之所以之后的方法可以获取正确的队列名是因为通道可以默默地记住消息代理最后一次生成的队列名称。

以"amq."开始的队列名称被预留做消息代理内部使用。如果试图在队列声明时打破这一规则的话，一个通道级的403 (ACCESS_REFUSED)错误会被抛出。

##### 4.4.2 队列持久化

持久化队列（Durable queues）会被存储在磁盘上，当消息代理（broker）重启的时候，它依旧存在。没有被持久化的队列称作暂存队列（Transient queues）。并不是所有的场景和案例都需要将队列持久化。

持久化的队列并不会使得路由到它的消息也具有持久性。倘若消息代理挂掉了，重新启动，那么在重启的过程中持久化队列会被重新声明，无论怎样，只有经过持久化的消息才能被重新恢复。

#### 5.5. 绑定

绑定（Binding）是交换机（exchange）将消息（message）路由给队列（queue）所需遵循的规则。如果要指示交换机“E”将消息路由给队列“Q”，那么“Q”就需要与“E”进行绑定。绑定操作需要定义一个可选的路由键（routing
key）属性给某些类型的交换机。路由键的意义在于从发送给交换机的众多消息中选择出某些消息，将其路由给绑定的队列。

打个比方：

- 队列（queue）是我们想要去的位于纽约的目的地
- 交换机（exchange）是JFK机场
- 绑定（binding）就是JFK机场到目的地的路线。能够到达目的地的路线可以是一条或者多条

拥有了交换机这个中间层，很多由发布者直接到队列难以实现的路由方案能够得以实现，并且避免了应用开发者的许多重复劳动。

如果AMQP的消息无法路由到队列（例如，发送到的交换机没有绑定队列），消息会被就地销毁或者返还给发布者。如何处理取决于发布者设置的消息属性。

#### 5.6. 消费者

消息如果只是存储在队列里是没有任何用处的。被应用消费掉，消息的价值才能够体现。在AMQP 0-9-1 模型中，有两种途径可以达到此目的：

- 将消息投递给应用 ("push API")
- 应用根据需要主动获取消息 ("pull API")

使用push
API，应用（application）需要明确表示出它在某个特定队列里所感兴趣的，想要消费的消息。如是，我们可以说应用注册了一个消费者，或者说订阅了一个队列。一个队列可以注册多个消费者，也可以注册一个独享的消费者（当独享消费者存在时，其他消费者即被排除在外）。

每个消费者（订阅者）都有一个叫做消费者标签的标识符。它可以被用来退订消息。消费者标签实际上是一个字符串。

##### 5.6.1 消息确认

消费者应用（Consumer applications） - 用来接受和处理消息的应用 -
在处理消息的时候偶尔会失败或者有时会直接崩溃掉。而且网络原因也有可能引起各种问题。这就给我们出了个难题，AMQP代理在什么时候删除消息才是正确的？AMQP 0-9-1 规范给我们两种建议：

- 当消息代理（broker）将消息发送给应用后立即删除。（使用AMQP方法：basic.deliver或basic.get-ok）
- 待应用（application）发送一个确认回执（acknowledgement）后再删除消息。（使用AMQP方法：basic.ack）

前者被称作自动确认模式（automatic acknowledgement model），后者被称作显式确认模式（explicit acknowledgement
model）。在显式模式下，由消费者应用来选择什么时候发送确认回执（acknowledgement）。应用可以在收到消息后立即发送，或将未处理的消息存储后发送，或等到消息被处理完毕后再发送确认回执（例如，成功获取一个网页内容并将其存储之后）。

如果一个消费者在尚未发送确认回执的情况下挂掉了，那AMQP代理会将消息重新投递给另一个消费者。如果当时没有可用的消费者了，消息代理会死等下一个注册到此队列的消费者，然后再次尝试投递。

##### 5.6.2 拒绝消息

当一个消费者接收到某条消息后，处理过程有可能成功，有可能失败。应用可以向消息代理表明，本条消息由于“拒绝消息（Rejecting
Messages）”的原因处理失败了（或者未能在此时完成）。当拒绝某条消息时，应用可以告诉消息代理如何处理这条消息——销毁它或者重新放入队列。当此队列只有一个消费者时，请确认不要由于拒绝消息并且选择了重新放入队列的行为而引起消息在同一个消费者身上无限循环的情况发生。

##### 5.6.3 预取消息

在多个消费者共享一个队列的案例中，明确指定在收到下一个确认回执前每个消费者一次可以接受多少条消息是非常有用的。这可以在试图批量发布消息的时候起到简单的负载均衡和提高消息吞吐量的作用。（例如，如果生产应用每分钟才发送一条消息，这说明处理工作尚在运行。）

注意，RabbitMQ只支持通道级的预取计数，而不是连接级的或者基于大小的预取。

#### 5.7. 消息属性和有效负载（消息主体）

AMQP模型中的消息（Message）对象是带有属性（Attributes）的。有些属性及其常见，以至于AMQP 0-9-1 明确的定义了它们，并且应用开发者们无需费心思思考这些属性名字所代表的具体含义。例如：

- Content type（内容类型）
- Content encoding（内容编码）
- Routing key（路由键）
- Delivery mode (persistent or not)
  投递模式（持久化 或 非持久化）
- Message priority（消息优先权）
- Message publishing timestamp（消息发布的时间戳）
- Expiration period（消息有效期）
- Publisher application id（发布应用的ID）

有些属性是被AMQP代理所使用的，但是大多数是开放给接收它们的应用解释器用的。有些属性是可选的也被称作消息头（headers）。他们跟HTTP协议的X-Headers很相似。消息属性需要在消息被发布的时候定义。

AMQP的消息除属性外，也含有一个有效载荷 -
Payload（消息实际携带的数据），它被AMQP代理当作不透明的字节数组来对待。消息代理不会检查或者修改有效载荷。消息可以只包含属性而不携带有效载荷。它通常会使用类似JSON这种序列化的格式数据，为了节省，协议缓冲器和MessagePack将结构化数据序列化，以便以消息的有效载荷的形式发布。AMQP及其同行者们通常使用"
content-type" 和 "content-encoding" 这两个字段来与消息沟通进行有效载荷的辨识工作，但这仅仅是基于约定而已。

消息能够以持久化的方式发布，AMQP代理会将此消息存储在磁盘上。如果服务器重启，系统会确认收到的持久化消息未丢失。简单地将消息发送给一个持久化的交换机或者路由给一个持久化的队列，并不会使得此消息具有持久化性质：它完全取决与消息本身的持久模式（persistence
mode）。将消息以持久化方式发布时，会对性能造成一定的影响（就像数据库操作一样，健壮性的存在必定造成一些性能牺牲）。

#### 5.8. 消息确认

由于网络的不确定性和应用失败的可能性，处理确认回执（acknowledgement）就变的十分重要。有时我们确认消费者收到消息就可以了，有时确认回执意味着消息已被验证并且处理完毕，例如对某些数据已经验证完毕并且进行了数据存储或者索引操作。

这种情形很常见，所以 AMQP 0-9-1 内置了一个功能叫做 消息确认（message
acknowledgements），消费者用它来确认消息已经被接收或者处理。如果一个应用崩溃掉（此时连接会断掉，所以AMQP代理亦会得知），而且消息的确认回执功能已经被开启，但是消息代理尚未获得确认回执，那么消息会被从新放入队列（并且在还有还有其他消费者存在于此队列的前提下，立即投递给另外一个消费者）。

协议内置的消息确认功能将帮助开发者建立强大的软件。

#### 5.9. 连接

**AMQP连接通常是长连接。**AMQP是一个使用TCP提供可靠投递的应用层协议。AMQP使用认证机制并且提供TLS（SSL）保护。当一个应用不再需要连接到AMQP代理的时候，需要优雅的释放掉AMQP连接，而不是直接将TCP连接关闭。

#### 5.10. 通道

有些应用需要与AMQP代理建立多个连接。无论怎样，同时开启多个TCP连接都是不合适的，因为这样做会消耗掉过多的系统资源并且使得防火墙的配置更加困难。AMQP
0-9-1提供了通道（channels）来处理多连接，可以把通道理解成共享一个TCP连接的多个轻量化连接。

在涉及多线程/进程的应用中，为每个线程/进程开启一个通道（channel）是很常见的，并且这些通道不能被线程/进程共享。

一个特定通道上的通讯与其他通道上的通讯是完全隔离的，因此每个AMQP方法都需要携带一个通道号，这样客户端就可以指定此方法是为哪个通道准备的。

#### 5.11. 虚拟主机

有些应用需要与AMQP代理建立多个连接。无论怎样，同时开启多个TCP连接都是不合适的，因为这样做会消耗掉过多的系统资源并且使得防火墙的配置更加困难。AMQP
0-9-1提供了通道（channels）来处理多连接，可以把通道理解成共享一个TCP连接的多个轻量化连接。

在涉及多线程/进程的应用中，为每个线程/进程开启一个通道（channel）是很常见的，并且这些通道不能被线程/进程共享。

一个特定通道上的通讯与其他通道上的通讯是完全隔离的，因此每个AMQP方法都需要携带一个通道号，这样客户端就可以指定此方法是为哪个通道准备的。

#### 5.12. 参数表

| 参数名                   | 数据类型 | 默认值 |
| ------------------------ | -------- | ------ |
| dev.rabbitmq.host        | String   | -      |
| dev.rabbitmq.port        | Integer  | -      |
| dev.rabbitmq.username    | String   | -      |
| dev.rabbitmq.password    | String   | -      |
| dev.rabbitmq.virtualHost | String   | /      |

#### 5.13. 使用举例

详见[git仓库dev-0104分支dev-mq-rabbit-example](http://10.250.20.182:8899/dev/dev/-/tree/dev-0104/dev-example/dev-mq-rabbit-example)

### 6. MQ - RPC 差异化统一平台

#### 6.1. 概述

在分布式部署架构下，MQ及RPC的使用节能达到目的，但由于MQ潜在的不稳定因素，故而需考虑MQ和RPC的并行运行，为减少开发人员在MQ和RPC使用上的难度，本框架对RPC和MQ的差异在使用上进行了统一。

#### 6.2. 实现方式

读取spring容器配置${dev.mrp.switch}，该配置可选值为【MQ/RPC】，根据配置，有统一平台自动完成MQ消息发布或RPC调用。基本请求参数详见*
com.allinfinance.dev.mrp.param.RequestParams*。

```java
    private String rpcInterface;
private String rpcMethod;

private String routingKey;
private String exchangeName;
```

调用结果详见*com.allinfinance.dev.mrp.param.GenericReponse*

```java
    private Boolean respStatus;
private T rpcInvokeData;
```

#### 6.3. 使用举例

详见[git仓库dev-0104分支dev-dubbo-example](http://10.250.20.182:8899/dev/dev/-/tree/dev-0104/dev-example/dev-dubbo-example)

### 7. 其余常用工具

#### 7.1. XML报文与实体类的互转及基础字段校验

在日常开发中，经常会遇到XML报文与实体类的互相转换，为避免重复造轮子，将其加入了基础框架中并实现了XStream及JAXB两种主流XML转换类库。并集成了注解校验字段内容是否合规，极大程度上减少了业务处理层繁杂的字段校验。

校验注解详情如下，注解生效开关：${dev.xml.field.verify}，默认值为false

```java
public @interface Check {
  /**
   * 字段类型
   */
    Class<?> type() default String.class;

    /**
     * 字段长度
     */
    int length() default 0;

    /**
     * 字段长度最大值
     */
    int maxLength() default 0;

  /**
     * 字段长度最小值
     */
    int minLength() default 0;

  /**
     * 字段校验正则
     */
    String regex() default "";
}
```

#### 7.2. Socket client

集成apache mina的socket客户端，可根据传入参数判断采用8583报文格式传输或者采用普通XML报文传输。

#### 7.3. Http client

集成apache-httpcomponents的http客户端，调用方可根据具体需求自定义http报文头，重试次数及超时时长。

| 参数名                   | 数据类型 | 默认值 |
| ------------------------ | -------- | ------ |
| dev.http.maxPoolSize        | Integer   | -      |
| dev.http.initPoolSize        | Integer  | -      |
| dev.http.charSet    | String   | -      |
| dev.http.socketSoTimeOut    | Integer   | -      |

#### 7.4. 文件传输工具

集成了文件传输中常用的基于FTP协议和SFTP协议的文件上传及下载。