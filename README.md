# Spring Boot & Microservices Interview Codebase

[中文](#中文说明) | [English](#english-documentation)

---

<a name="中文说明"></a>
## 中文说明

### 一、项目简介
本项目是一套针对 Java 后端研发、Spring 全家桶（Spring Framework / Spring MVC / Spring Boot / Spring Cloud）、MyBatis 原生及增强框架、以及经典 23 种设计模式的高频面试真题与核心原理解析代码库。

全工程遵循工业级编码规范，坚持“理论解析 + 源码对照 + 可执行代码验证”的教学标准。每个知识点均提供独立、可编译、可运行的 `main()` 入口与详尽的中文架构原理解析，彻底告别纸上谈兵。

---

### 二、技术栈与运行环境
- **核心开发语言**：Java 11 (兼容 Java 8 / 17 / 21)
- **核心框架**：Spring Boot 2.6.13
- **构建工具**：Apache Maven 3.6+
- **关键依赖**：
  - `spring-boot-starter-web` (Web MVC 核心机制)
  - `spring-boot-starter-aop` (AspectJ 切面与代理)
  - `mybatis-spring-boot-starter` (持久层框架)
- **校验工具**：已全量通过 IntelliJ IDEA MCP 构建与静态代码质量检查。

---

### 三、模块架构与全景目录

```text
src/main/java/com/jinlin/springbootinterview/
├── SpringbootinterviewApplication.java          # Spring Boot 默认主启动类
└── interview/
    ├── spring/                                 # 1. Spring 核心与 AOP 切面专题
    │   ├── SpringInterviewKnowledgeMasterIndex.java # Spring 面试知识全景总览
    │   └── aop/                                # IoC、三级缓存、静态代理、JDK动态代理与Spring切面
    │       ├── AopInterviewApplication.java    # AOP 运行总入口
    │       ├── aspect/LoggingAspect.java       # AspectJ 环绕/前置/返回/异常/最终通知切面
    │       ├── proxy/                          # 原生静态代理与 JDK 动态代理实现
    │       └── service/                        # 业务接口与实现类
    │
    ├── springmvc/                              # 2. Spring MVC 核心机制专题
    │   ├── SpringMvcInterviewApplication.java  # DispatcherServlet 12 步全链路模拟总入口
    │   ├── adapter/                            # 处理器适配器 (HandlerAdapter)
    │   ├── controller/                         # MVC 控制层演示
    │   ├── interceptor/                        # 拦截器执行链 (preHandle/postHandle/afterCompletion)
    │   └── mapping/                            # 处理器映射器 (HandlerMapping)
    │
    ├── springboot/                             # 3. Spring Boot 原理与 Starter 专题
    │   ├── SpringBootInterviewApplication.java # Spring Boot 核心机制总入口
    │   ├── autoconfigure/                      # 自动装配原理演示
    │   ├── starter/                            # 手写自定义 Starter (Properties + Service + AutoConfig)
    │   └── transaction/                        # 声明式事务 (@Transactional) 底层模拟
    │
    ├── mybatis/                                # 4. MyBatis 核心原理与设计模式专题
    │   ├── MyBatisInterviewApplication.java    # MyBatis 运行总入口与设计模式全解析
    │   ├── jdbc/                               # 传统原生 JDBC 六步流程详解
    │   ├── nativeflow/                         # 原生 SqlSessionFactory 与 MapperProxy 流程
    │   ├── plus/                               # MyBatis vs MyBatis-Plus 差异与选型对比
    │   └── sql/                                # #{} 与 ${} 底层实现与 SQL 注入攻防演示
    │
    ├── designpatterns/                         # 5. 经典设计模式专题 (严格按题号命名 Pattern01 ~ Pattern20)
    │   ├── DesignPatternsMasterApplication.java# 设计模式总索引入口
    │   ├── creational/                         # 创建型模式
    │   │   ├── Pattern01_SingletonPatternDemo.java      # 单例模式 (饿汉式两类 + 懒汉式DCL/静态内部类 + 枚举)
    │   │   ├── Pattern02_FactoryMethodPatternDemo.java  # 工厂方法模式
    │   │   ├── Pattern06_BuilderPatternDemo.java        # 建造者模式
    │   │   └── Pattern09_AbstractFactoryPatternDemo.java# 抽象工厂模式
    │   ├── structural/                         # 结构型模式
    │   │   ├── Pattern03_ProxyPatternDemo.java          # 代理模式 (静态代理 + JDK 动态代理)
    │   │   ├── Pattern10_AdapterPatternDemo.java        # 适配器模式
    │   │   ├── Pattern11_FacadePatternDemo.java         # 外观模式
    │   │   ├── Pattern13_BridgePatternDemo.java         # 桥接模式
    │   │   ├── Pattern14_CompositePatternDemo.java      # 组合模式
    │   │   └── Pattern15_DecoratorPatternDemo.java      # 装饰器模式
    │   └── behavioral/                         # 行为型模式
    │       ├── Pattern04_StrategyPatternDemo.java       # 策略模式 (消除 if-else)
    │       ├── Pattern05_ObserverPatternDemo.java       # 观察者模式 (事件发布订阅)
    │       ├── Pattern07_TemplateMethodPatternDemo.java # 模板方法模式 (骨架算法与钩子)
    │       ├── Pattern08_ChainOfResponsibilityPatternDemo.java # 责任链模式 (多级审批链)
    │       ├── Pattern12_IteratorPatternDemo.java       # 迭代器模式
    │       ├── Pattern16_StatePatternDemo.java          # 状态模式 (状态机驱动流转)
    │       ├── Pattern17_VisitorPatternDemo.java        # 访问者模式 (双分派机制)
    │       ├── Pattern18_MediatorPatternDemo.java       # 中介者模式 (星型拓扑解耦)
    │       ├── Pattern19_CommandPatternDemo.java        # 命令模式 (命令封装与多级 Undo)
    │       └── Pattern20_MementoPatternDemo.java        # 备忘录模式 (快照捕获与回滚恢复)
    │
    └── springcloud/                            # 6. Spring Cloud 微服务高频考点专题
        ├── SpringCloudInterviewApplication.java         # 微服务运行总入口
        ├── overview/                           # 区别与生态
        │   ├── MicroserviceComponentMetadata.java       # 三代组件元数据
        │   └── SpringCloudVsSpringBootExplanation.java  # Boot vs Cloud 及三大生态选型对比
        ├── loadbalancer/                       # 负载均衡算法专题
        │   ├── ServiceInstance.java                     # 集群服务节点模型
        │   ├── LoadBalancer.java                        # 负载均衡顶层通用接口
        │   ├── RoundRobinLoadBalancer.java              # 轮询算法实现
        │   ├── RandomLoadBalancer.java                  # 随机算法实现
        │   ├── WeightedRoundRobinLoadBalancer.java      # 加权轮询算法实现
        │   ├── ConsistentHashUserStickyLoadBalancer.java# 一致性哈希单用户粘性路由实现
        │   └── LoadBalancerAlgorithmsDemo.java          # 算法运行演示
        └── circuitbreaker/                     # 熔断与降级专题
            ├── CircuitState.java                        # 熔断器 CLOSED/OPEN/HALF_OPEN 三态枚举
            ├── RemoteOrderService.java                  # 模拟远程下游微服务
            ├── MiniCircuitBreaker.java                  # 完整熔断降级状态机引擎实现
            └── CircuitBreakerAndFallbackDemo.java       # 熔断恢复全链路流转实战
```

---

### 四、核心知识板块要点

#### 1. Spring 核心与 AOP 切面
- **控制反转 (IoC) 与 依赖注入 (DI)**：Spring 容器管理 Bean 的元数据定义 (`BeanDefinition`)、实例化、属性填充与初始化生命周期。
- **循环依赖解决机制**：三级缓存架构剖析（一级 `singletonObjects`、二级 `earlySingletonObjects`、三级 `singletonFactories`），深入解释为什么不能使用二级缓存（在存在 AOP 动态代理时，提前暴露 ObjectFactory 生成代理对象以维持单例性）。
- **代理机制**：对比静态代理、JDK 原生动态代理 (`InvocationHandler` + `Proxy.newProxyInstance`) 与 CGLIB 字节码生成，演示完整的 AspectJ 切面通知链路。

#### 2. Spring MVC 核心链路
- **分层架构**：Controller、Service、DAO 分层职责规范。
- **处理全流程**：`DispatcherServlet` 调度全链路（接收请求 -> 查找 `HandlerMapping` -> 拦截器 `preHandle` -> 获取 `HandlerAdapter` -> 参数解析与 Controller 反射调用 -> 返回值解析序列化 -> 拦截器 `postHandle` -> 渲染或直接写回 HTTP Body -> 拦截器 `afterCompletion` 资源清理）。

#### 3. Spring Boot 原理与 Starter
- **约定优于配置 (CoC)**：标准化目录结构、开箱即用起步依赖（Starter POMs）、内嵌 Servlet 容器。
- **自动装配机制**：`@SpringBootApplication` 底层三大核心注解（`@SpringBootConfiguration`、`@EnableAutoConfiguration`、`@ComponentScan`）与 `spring.factories` / SPI 机制。
- **手写 Starter 规范**：从配置绑定类 (`@ConfigurationProperties`) 到自动配置类 (`@EnableConfigurationProperties` + `@ConditionalOnMissingBean`) 的完整实现流程。
- **事务与拦截器**：声明式事务 (`@Transactional`) 代理控制机制；`Filter` 与 `HandlerInterceptor` 在调用栈与生命周期上的区别。

#### 4. MyBatis 核心原理
- **传统 JDBC 痛点**：硬编码 SQL、手动管理连接事务、频繁的 `ResultSet` 映射开销。
- **防注入核心**：`#{}` 预编译占位符 (`PreparedStatement`) vs `${}` 字符串拼接替换的底层机制对比与 SQL 注入演示。
- **原生调用流转**：`SqlSessionFactoryBuilder` -> `SqlSessionFactory` -> `SqlSession` -> `MapperProxy` 动态代理转换 SQL。
- **MyBatis 中的设计模式**：建造者模式、工厂方法、动态代理、缓存装饰器链、执行器模板方法、拦截器插件责任链、日志适配器体系。

#### 5. 经典 23 种设计模式（20 种高频精讲）
- 文件命名统一使用 `Pattern01_` 至 `Pattern20_`，与常见面试考题次序完全对齐。
- **单例模式** 全面覆盖：
  - 饿汉式：静态常量方式、静态代码块方式；
  - 懒汉式：双重检查锁定 (DCL + `volatile`)、静态内部类 (IoDH)；
  - 枚举单例：防反射破坏与防反序列化多次创建。
- **策略模式**：利用 Map 路由表彻底干掉恶臭的 `if-else / switch` 分支。
- **代理模式**：原生静态代理 vs JDK 动态代理对比。
- **状态模式**：电商订单生命周期流转状态机。
- **访问者模式**：双分派 (Double Dispatch) 机制在财务与 HR 审核中的实战应用。

#### 6. Spring Cloud 微服务治理
- **Spring Boot vs Spring Cloud**：单体微服务砖石 vs 分布式集群治理网络。
- **三代组件演进**：Netflix (Eureka/Ribbon/Hystrix/Zuul) -> Alibaba (Nacos/Sentinel/OpenFeign/Gateway/Seata) -> Spring 官方 (Consul/LoadBalancer/Resilience4j/Gateway)。
- **负载均衡五大算法**：轮询 (Round Robin)、随机 (Random)、加权轮询 (Weighted Round Robin)、最小活跃数、一致性哈希。
- **同一用户粘性路由**：基于 `userId` / `IP` 的 2^32 虚拟节点红黑树一致性哈希环顺时针寻径算法落地，解决缓存局部性与长连接会话保持。
- **服务熔断与服务降级**：雪崩效应机理；`CLOSED -> OPEN -> HALF_OPEN -> CLOSED` 三态状态机流转；Fallback 降级兜底方案。

---

### 五、快速开始与编译执行

#### 1. 克隆代码仓库
```bash
git clone https://github.com/LinSky-J/springboot-interview.git
cd springboot-interview
```

#### 2. 全量编译与测试检查
```bash
mvn clean test-compile
```
*提示：所有源码均已通过无 BOM 的 UTF-8 编码校验与编译测试。*

#### 3. 运行特定模块
通过命令行直接运行各模块的独立总入口类：

- **运行 20 种设计模式总入口**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.designpatterns.DesignPatternsMasterApplication
  ```
- **运行 Spring Cloud 微服务总入口**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springcloud.SpringCloudInterviewApplication
  ```
- **运行 Spring Boot 原理演示**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springboot.SpringBootInterviewApplication
  ```
- **运行 MyBatis 原理演示**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.mybatis.MyBatisInterviewApplication
  ```
- **运行 Spring MVC 处理流转演示**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springmvc.SpringMvcInterviewApplication
  ```
- **运行 Spring AOP 切面演示**：
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.spring.aop.AopInterviewApplication
  ```

---
---

<a name="english-documentation"></a>
## English Documentation

### 1. Project Overview
This repository provides an enterprise-grade codebase dedicated to high-frequency Java backend interview questions and architectural principles. It covers the core Spring Ecosystem (Spring Framework / Spring MVC / Spring Boot / Spring Cloud), MyBatis and MyBatis-Plus persistence frameworks, and the classic 23 Gang of Four (GoF) Design Patterns.

Every technical topic is implemented with runnable Java classes (`main()` entry points) and thorough architectural comments, adhering strictly to enterprise-level decoupling, clean code standards, and compiler safety.

---

### 2. Technology Stack & Prerequisites
- **Language**: Java 11 (compatible with Java 8 / 17 / 21)
- **Framework**: Spring Boot 2.6.13
- **Build System**: Apache Maven 3.6+
- **Core Dependencies**:
  - `spring-boot-starter-web` (Web MVC processing and controllers)
  - `spring-boot-starter-aop` (AspectJ aspects and proxy infrastructure)
  - `mybatis-spring-boot-starter` (SQL mapping and database access)
- **Validation**: 100% verified with zero compilation problems via IntelliJ IDEA MCP.

---

### 3. Repository Architecture & Layout

```text
src/main/java/com/jinlin/springbootinterview/
├── SpringbootinterviewApplication.java          # Spring Boot main application class
└── interview/
    ├── spring/                                 # 1. Spring Core & Spring AOP
    │   ├── SpringInterviewKnowledgeMasterIndex.java # Master summary of Spring architecture
    │   └── aop/                                # IoC, 3-level cache, proxies, and aspect advice
    │       ├── AopInterviewApplication.java    # Master runner for AOP
    │       ├── aspect/LoggingAspect.java       # AspectJ @Around/@Before/@AfterReturning aspects
    │       ├── proxy/                          # Static Proxy and JDK Dynamic Proxy implementations
    │       └── service/                        # Service interface and implementation
    │
    ├── springmvc/                              # 2. Spring MVC Architecture
    │   ├── SpringMvcInterviewApplication.java  # 12-step DispatcherServlet pipeline simulator
    │   ├── adapter/                            # HandlerAdapter implementations
    │   ├── controller/                         # MVC controllers
    │   ├── interceptor/                        # Interceptor chains (preHandle/postHandle/afterCompletion)
    │   └── mapping/                            # HandlerMapping routing
    │
    ├── springboot/                             # 3. Spring Boot Principles & Starters
    │   ├── SpringBootInterviewApplication.java # Master runner for Spring Boot mechanisms
    │   ├── autoconfigure/                      # Auto-configuration demonstrations
    │   ├── starter/                            # Custom Starter (Properties + Service + AutoConfig)
    │   └── transaction/                        # Declarative transaction (@Transactional) proxy demo
    │
    ├── mybatis/                                # 4. MyBatis Principles & Design Patterns
    │   ├── MyBatisInterviewApplication.java    # Master runner and 8 design patterns in MyBatis
    │   ├── jdbc/                               # 6-step native JDBC workflow demonstration
    │   ├── nativeflow/                         # Native SqlSessionFactory and MapperProxy pipeline
    │   ├── plus/                               # MyBatis vs MyBatis-Plus architectural comparison
    │   └── sql/                                # #{} vs ${} parameter replacement and SQL injection defense
    │
    ├── designpatterns/                         # 5. Classic Design Patterns (Ordered Pattern01 to Pattern20)
    │   ├── DesignPatternsMasterApplication.java# Master index for design patterns
    │   ├── creational/                         # Creational Patterns
    │   │   ├── Pattern01_SingletonPatternDemo.java      # Singleton (Eager, Lazy DCL, Inner Class, Enum)
    │   │   ├── Pattern02_FactoryMethodPatternDemo.java  # Factory Method Pattern
    │   │   ├── Pattern06_BuilderPatternDemo.java        # Builder Pattern
    │   │   └── Pattern09_AbstractFactoryPatternDemo.java# Abstract Factory Pattern
    │   ├── structural/                         # Structural Patterns
    │   │   ├── Pattern03_ProxyPatternDemo.java          # Proxy Pattern (Static + JDK Dynamic Proxy)
    │   │   ├── Pattern10_AdapterPatternDemo.java        # Adapter Pattern
    │   │   ├── Pattern11_FacadePatternDemo.java         # Facade Pattern
    │   │   ├── Pattern13_BridgePatternDemo.java         # Bridge Pattern
    │   │   ├── Pattern14_CompositePatternDemo.java      # Composite Pattern
    │   │   └── Pattern15_DecoratorPatternDemo.java      # Decorator Pattern
    │   └── behavioral/                         # Behavioral Patterns
    │       ├── Pattern04_StrategyPatternDemo.java       # Strategy Pattern (if-else elimination)
    │       ├── Pattern05_ObserverPatternDemo.java       # Observer Pattern (Event-driven publisher/subscriber)
    │       ├── Pattern07_TemplateMethodPatternDemo.java # Template Method Pattern (Skeletons & hooks)
    │       ├── Pattern08_ChainOfResponsibilityPatternDemo.java # Chain of Responsibility (Multi-level approvals)
    │       ├── Pattern12_IteratorPatternDemo.java       # Iterator Pattern
    │       ├── Pattern16_StatePatternDemo.java          # State Pattern (State machine lifecycle)
    │       ├── Pattern17_VisitorPatternDemo.java        # Visitor Pattern (Double dispatch mechanism)
    │       ├── Pattern18_MediatorPatternDemo.java       # Mediator Pattern (Star topology coordination)
    │       ├── Pattern19_CommandPatternDemo.java        # Command Pattern (Request encapsulation & multi-level undo)
    │       └── Pattern20_MementoPatternDemo.java        # Memento Pattern (Snapshot capture & rollback)
    │
    └── springcloud/                            # 6. Spring Cloud Microservices Infrastructure
        ├── SpringCloudInterviewApplication.java         # Master runner for microservices
        ├── overview/                           # Concepts and Generations
        │   ├── MicroserviceComponentMetadata.java       # Metadata entity for component comparisons
        │   └── SpringCloudVsSpringBootExplanation.java  # Boot vs Cloud and 3-generation component matrix
        ├── loadbalancer/                       # Load Balancing Algorithms
        │   ├── ServiceInstance.java                     # Cluster service node model
        │   ├── LoadBalancer.java                        # Common load balancing interface
        │   ├── RoundRobinLoadBalancer.java              # Round Robin algorithm
        │   ├── RandomLoadBalancer.java                  # Random algorithm
        │   ├── WeightedRoundRobinLoadBalancer.java      # Weighted Round Robin algorithm
        │   ├── ConsistentHashUserStickyLoadBalancer.java# Consistent Hashing for single user sticky routing
        │   └── LoadBalancerAlgorithmsDemo.java          # Algorithmic execution demo
        └── circuitbreaker/                     # Circuit Breaker & Fallback
            ├── CircuitState.java                        # CLOSED / OPEN / HALF_OPEN state enum
            ├── RemoteOrderService.java                  # Simulated remote downstream service
            ├── MiniCircuitBreaker.java                  # Full state machine circuit breaker engine
            └── CircuitBreakerAndFallbackDemo.java       # End-to-end circuit breaker and recovery demo
```

---

### 4. Technical Highlights

#### 1. Spring Framework Core & Spring AOP
- **Inversion of Control (IoC) & Dependency Injection (DI)**: Bean lifecycle from `BeanDefinition` parsing, instantiation, property population, to `BeanPostProcessor` initialization.
- **Circular Dependency Resolution**: In-depth analysis of Spring's 3-level cache architecture (`singletonObjects`, `earlySingletonObjects`, `singletonFactories`) and why 2-level cache is insufficient when dealing with AOP proxy generation.
- **Proxy Architecture**: Full comparison of Static Proxy, JDK Dynamic Proxy (`InvocationHandler` + `Proxy.newProxyInstance`), and CGLIB byte-code enhancement.

#### 2. Spring MVC Execution Pipeline
- **12-Step DispatcherServlet Flow**: End-to-end execution simulation covering `HandlerMapping` lookup, `HandlerInterceptor.preHandle()`, `HandlerAdapter` invocation, parameter resolution, controller execution, return value resolution (`HttpMessageConverter`), `postHandle()`, and `afterCompletion()`.

#### 3. Spring Boot Mechanics & Custom Starter
- **Convention over Configuration (CoC)**: Structured defaults, starter dependencies, and embedded web containers.
- **Auto-configuration Architecture**: Deep dive into `@SpringBootApplication`, `@EnableAutoConfiguration`, `AutoConfigurationImportSelector`, and conditional annotations (`@ConditionalOnClass`, `@ConditionalOnMissingBean`).
- **Production-grade Starter**: Complete implementation of configuration binding (`@ConfigurationProperties`), service bean injection, and `spring.factories` registration.

#### 4. MyBatis Internals & Design Patterns
- **JDBC Comparison**: Analyzing traditional JDBC verbosity, manual resource management, and parameter mapping overhead.
- **SQL Injection Defense**: Comprehensive analysis of precompiled statement placeholders (`#{}`) versus direct string substitution (`${}`).
- **Pattern Utilization**: 8 design patterns in MyBatis source code (Builder in `SqlSessionFactoryBuilder`, Dynamic Proxy in `MapperProxy`, Decorator chain in `Cache`, Template Method in `BaseExecutor`, Chain of Responsibility in `InterceptorChain`, Adapter in `Log`).

#### 5. 23 Classic Design Patterns (20 Interview Essentials)
- Systematically aligned with the exact question numbering (`Pattern01_` through `Pattern20_`).
- **Singleton Pattern**: Full comparison of Eager Initialization (static constant and static block), Lazy Initialization (Double-Checked Locking with `volatile` and Static Inner Class IoDH), and Enum Singleton.
- **Strategy Pattern**: Eliminating complex `if-else / switch` blocks with map-based dispatch.
- **State Pattern**: Finite state machine transitions across an e-commerce order lifecycle.
- **Visitor Pattern**: Double dispatch mechanism in employee HR attendance and payroll calculation.

#### 6. Spring Cloud Microservices
- **Boot vs Cloud**: Single-service building blocks versus distributed cluster orchestration.
- **Three Component Generations**: Netflix OSS (legacy) vs Spring Cloud Alibaba (mainstream) vs Spring Cloud Official (cloud-native).
- **Load Balancing Algorithms**: Round Robin, Random, Weighted Round Robin, and Consistent Hashing.
- **Sticky User Routing**: 2^32 virtual-node hash ring on `TreeMap` routing users consistently to the same backend node, maximizing local cache hits.
- **Circuit Breaker & Fallback**: Cascading failure mitigation, `CLOSED -> OPEN -> HALF_OPEN -> CLOSED` state machine transitions, probe requests, and fallback execution.

---

### 5. Quick Start & Execution

#### 1. Clone Repository
```bash
git clone https://github.com/LinSky-J/springboot-interview.git
cd springboot-interview
```

#### 2. Compile & Test
```bash
mvn clean test-compile
```

#### 3. Run Specific Entry Points
Execute the standalone master classes directly via command line:

- **Run 20 Design Patterns Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.designpatterns.DesignPatternsMasterApplication
  ```
- **Run Spring Cloud Microservices Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springcloud.SpringCloudInterviewApplication
  ```
- **Run Spring Boot Principles Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springboot.SpringBootInterviewApplication
  ```
- **Run MyBatis Core Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.mybatis.MyBatisInterviewApplication
  ```
- **Run Spring MVC Pipeline Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.springmvc.SpringMvcInterviewApplication
  ```
- **Run Spring AOP Demo**:
  ```bash
  java -cp target/classes com.jinlin.springbootinterview.interview.spring.aop.AopInterviewApplication
  ```

---

### 6. License
This project is open-sourced under the [MIT License](LICENSE).
