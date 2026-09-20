package com.jinlin.springbootinterview.interview.springboot;

import com.jinlin.springbootinterview.interview.springboot.autoconfigure.config.MyServiceProperties;
import com.jinlin.springbootinterview.interview.springboot.autoconfigure.service.MyStarterService;
import com.jinlin.springbootinterview.interview.springboot.transaction.SpringBootTransactionMechanism;

/**
 * ============================================================================
 * SpringBoot 核心面试体系知识大纲与运行验证中心
 * ============================================================================
 * 
 * 本模块深度解答以下全部核心面试题目：
 * 1. 为什么使用 SpringBoot？比传统的 Spring 好在哪里？
 * 2. 怎么理解 SpringBoot 中的“约定大于配置 (Convention over Configuration)”？
 * 3. SpringBoot 项目的标准结构是怎样的？
 * 4. SpringBoot 中用到了哪些经典设计模式？
 * 5. SpringBoot 自动装配原理是什么？怎么做到导入依赖即可直接使用的？
 * 6. 说几个常用的起步依赖（Starter）？写过自定义 Starter 吗？
 * 7. SpringBoot 里面有哪些核心注解？还有一个配置相关的注解是哪个？
 * 8. SpringBoot 怎么开启声明式事务？
 * 9. SpringBoot 过滤器 (Filter) 和拦截器 (Interceptor) 的本质区别？
 * 
 * ============================================================================
 * [核心考点深度教学拆解]
 * 
 * 一、为什么使用 SpringBoot？比传统 Spring 好在哪里？
 * 1. 传统 Spring 开发的核心痛点：
 *    - 依赖管理复杂繁重：每个 jar 包的版本号都要自己手动配置，稍有不慎就发生版本冲突（NoSuchMethodError / ClassNotFoundException）。
 *    - 配置繁琐臃肿：需要编写大量的 applicationContext.xml、web.xml，配置组件扫描、视图解析器、事务管理器等重复模板代码。
 *    - 部署运维繁琐：必须打成 war 包，手工部署到外部的独立 Tomcat 容器中，容易受外部环境差异影响。
 * 2. SpringBoot 的颠覆性优势：
 *    - 起步依赖 (Starters)：聚合常用依赖并统一锁定兼容版本，杜绝依赖地狱。
 *    - 自动装配 (Auto-Configuration)：基于条件注解，开箱即用，零 XML 配置。
 *    - 内嵌 Servlet 容器 (Embedded Tomcat/Jetty/Undertow)：打成一个普通的 Jar 包即可通过 "java -jar" 随时随地运行。
 *    - 生产就绪特性 (Actuator)：内置健康检查、指标监控、环境属性查看，无缝对接 DevOps。
 * 
 * 二、怎么理解“约定大于配置 (Convention over Configuration)”？
 * 1. 核心定义：
 *    系统在架构层面预设了一套最合理、通用的默认规范与行为。如果你的开发习惯符合这套约定，
 *    你就完全不需要写任何配置，系统自动就能运转；只有在你的诉求与默认约定不一致时，才需要显式编写配置覆盖。
 * 2. 经典约定示例：
 *    - 启动类位置约定：默认扫描启动类所在包及其所有子包下的组件（无需手写 component-scan）。
 *    - 资源目录约定：静态资源默认约定放在 src/main/resources/static 下。
 *    - 配置文件约定：统一命名为 application.yml 或 application.properties。
 *    - 端口号约定：内置 Web 容器默认监听 8080 端口。
 *    - 数据库连接池约定：类路径有 HikariCP 时默认优先选用 HikariCP。
 * 
 * 三、SpringBoot 用到了哪些经典设计模式？
 * 1. 模板方法模式 (Template Method)：
 *    SpringApplication.run() 方法内部定义了启动的标准化主骨架流水线（环境准备、上下文创建、刷新、执行 runners）。
 * 2. 适配器模式 (Adapter)：
 *    WebMvcConfigurer 接口适配 SpringBoot 与底层 Spring MVC 的各种高级定制。
 * 3. 观察者模式 / 事件发布模式 (Observer)：
 *    SpringApplicationRunListener 广播各种启动事件（Starting, EnvironmentPrepared, ContextLoaded, Started, Failed）。
 * 4. 工厂模式 (Factory)：
 *    SpringFactoriesLoader 作为 SPI 核心工厂，根据接口从 META-INF/spring.factories 读取并实例化配置类。
 * 5. 策略模式 (Strategy)：
 *    ApplicationRunner 和 CommandLineRunner 在容器启动完成时提供个性化回调执行策略。
 * 6. 构建者模式 (Builder)：
 *    SpringApplicationBuilder 支持通过链式流式 API 构建启动复杂的 Spring 容器。
 * 
 * 四、SpringBoot 核心注解体系
 * 1. 核心启动复合注解：@SpringBootApplication
 *    由以下三大核心注解组成：
 *    - @SpringBootConfiguration: 本质是 @Configuration，声明为配置类。
 *    - @EnableAutoConfiguration: 开启自动装配，驱动 AutoConfigurationImportSelector 扫描装配。
 *    - @ComponentScan: 自动扫描当前包及子包下的 @Component, @Service, @Controller 等。
 * 2. 关键配置相关注解：
 *    - @ConfigurationProperties: 将外部配置文件（YAML/Properties）中的前缀配置自动、类型安全地映射绑定到 JavaBean。
 *    - @EnableConfigurationProperties: 显式开启某个配置类属性绑定的支持。
 * 
 * 五、常用起步依赖（Starters）清单
 * 1. spring-boot-starter-web: Web 与 RESTful 开发（含 Spring MVC 和内嵌 Tomcat）。
 * 2. spring-boot-starter-aop: 面向切面编程支持（含 AspectJ 织入支持）。
 * 3. mybatis-spring-boot-starter / spring-boot-starter-data-jpa: 数据持久层集成。
 * 4. spring-boot-starter-data-redis: Redis 高性能缓存客户端与模板支持。
 * 5. spring-boot-starter-amqp: RabbitMQ 消息队列支持。
 * 6. spring-boot-starter-test: 单元测试套件（JUnit 5, Mockito, AssertJ）。
 * 7. spring-boot-starter-actuator: 生产环境健康检查与度量监控。
 * ============================================================================
 */
public class SpringBootInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("1. 验证自定义 Starter 的核心业务调用逻辑");
        System.out.println("=================================================================");
        MyServiceProperties properties = new MyServiceProperties();
        properties.setEnabled(true);
        properties.setApiKey("CUSTOM_API_KEY_20260920");
        properties.setTimeoutMs(3000);

        MyStarterService starterService = new MyStarterService(properties);
        String result = starterService.executeBusiness("订单支付报文");
        System.out.println("Starter 返回结果: " + result);

        System.out.println();
        System.out.println("=================================================================");
        System.out.println("2. 验证 SpringBoot 声明式事务规范执行");
        System.out.println("=================================================================");
        SpringBootTransactionMechanism transactionDemo = new SpringBootTransactionMechanism();
        transactionDemo.executeAccountTransfer("622202001", "622202002", 500.0);

        System.out.println();
        System.out.println("SpringBoot 面试核心专题所有技术点与代码演示验证通过！");
    }
}
