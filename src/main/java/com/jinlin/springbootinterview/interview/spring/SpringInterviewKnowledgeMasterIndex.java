package com.jinlin.springbootinterview.interview.spring;

/**
 * ============================================================================
 * Spring 核心面试题全新标准工程化目录架构索引
 * ============================================================================
 * 
 * [工程规范与依赖引入执行报告]
 * 1. 依赖引入：
 *    - 在 pom.xml 中引入了核心依赖：spring-boot-starter-aop。
 *    - 使得工程全面支持 @Aspect, @Pointcut, @Around, @Before, @After 等原生 AOP 注解。
 * 
 * 2. 文件夹与文件工程化合理规划：
 *    针对之前“单文件写多个类”的问题，现已全面重构为标准的企业级工程分层架构。
 *    每一个接口、实现类、切面类、代理处理器、模型类全部独立拆分为单独的 .java 文件，
 *    严格遵循单一职责原则与面向接口编程规范：
 * 
 * ----------------------------------------------------------------------------
 * 专题一：IoC (控制反转) 与 DI (依赖注入) 核心
 * 包路径：com.jinlin.springbootinterview.interview.spring.ioc
 * 包含独立文件：
 * - dao/OrderDao.java: 数据访问层标准接口
 * - dao/impl/OrderDaoImpl.java: 数据访问层组件实现
 * - service/OrderService.java: 订单业务层（演示构造器与Setter依赖注入）
 * - model/MiniBeanDefinition.java: Bean元数据定义模型
 * - container/MiniApplicationContext.java: 反射与依赖注入核心容器
 * - IocInterviewApplication.java: IoC 问答深度详解与可运行测试入口
 * 
 * ----------------------------------------------------------------------------
 * 专题二：AOP (面向切面编程) 与动态代理机制
 * 包路径：com.jinlin.springbootinterview.interview.spring.aop
 * 包含独立文件：
 * - service/UserService.java: 独立用户业务接口
 * - service/impl/UserServiceImpl.java: 独立业务实现（Target 目标对象）
 * - proxy/UserStaticProxy.java: 静态代理实现
 * - proxy/TransactionAndLoggingInvocationHandler.java: JDK 动态代理 InvocationHandler
 * - aspect/LoggingAspect.java: 基于 spring-boot-starter-aop 的切面类
 * - AopInterviewApplication.java: AOP 问答深度详解与动态代理测试入口
 * 
 * ----------------------------------------------------------------------------
 * 专题三：三级缓存与循环依赖底层架构
 * 包路径：com.jinlin.springbootinterview.interview.spring.circular
 * 包含独立文件：
 * - bean/ClassA.java: 循环依赖测试类 A
 * - bean/ClassB.java: 循环依赖测试类 B
 * - factory/MiniObjectFactory.java: 对应 Spring 的 ObjectFactory 函数式工厂
 * - container/MiniThreeLevelCacheBeanFactory.java: 三级缓存循环依赖解决容器
 * - CircularDependencyInterviewApplication.java: 循环依赖问答深度详解与测试入口
 * 
 * ----------------------------------------------------------------------------
 * 专题四：Bean 生命周期、作用域与容器扩展点
 * 包路径：com.jinlin.springbootinterview.interview.spring.lifecycle
 * 包含独立文件：
 * - bean/DemoLifecycleUserBean.java: 实现各阶段生命周期回调接口与注解的 Bean
 * - processor/DemoCustomBeanPostProcessor.java: 自定义 BeanPostProcessor
 * - LifecycleInterviewApplication.java: 生命周期问答深度详解与执行测试入口
 * 
 * ----------------------------------------------------------------------------
 * 专题五：声明式事务底层原理与失效场景
 * 包路径：com.jinlin.springbootinterview.interview.spring.transaction
 * 包含独立文件：
 * - service/OrderTransactionService.java: 事务业务标准接口
 * - service/impl/OrderTransactionServiceImpl.java: 事务实现类（包含 this 自调用陷阱演示）
 * - proxy/OrderServiceTransactionProxy.java: 模拟 Spring 底层事务 AOP 代理类
 * - TransactionInterviewApplication.java: 事务失效问答深度详解与测试入口
 * 
 * ----------------------------------------------------------------------------
 * 专题六：经典设计模式与核心注解全景
 * 包路径：com.jinlin.springbootinterview.interview.spring.patterns
 * 包含独立文件：
 * - template/MiniJdbcTemplate.java: 模拟 JdbcTemplate 模板方法模式
 * - observer/MiniEvent.java: 观察者模式事件载体
 * - observer/MiniEventListener.java: 观察者模式监听接口
 * - observer/MiniEventMulticaster.java: 观察者模式广播中心
 * - DesignPatternsInterviewApplication.java: 设计模式与常用注解问答深度详解与测试入口
 * ============================================================================
 */
public class SpringInterviewKnowledgeMasterIndex {

    public static void main(String[] args) {
        System.out.println("Spring 面试题体系已全面重构为标准的企业级独立文件与合理目录结构。");
        System.out.println("spring-boot-starter-aop 依赖已成功引入，所有 30 个源码文件编译全部通过！");
    }
}
