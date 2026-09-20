package com.jinlin.springbootinterview.interview.spring.aop;

import com.jinlin.springbootinterview.interview.spring.aop.proxy.TransactionAndLoggingInvocationHandler;
import com.jinlin.springbootinterview.interview.spring.aop.proxy.UserStaticProxy;
import com.jinlin.springbootinterview.interview.spring.aop.service.UserService;
import com.jinlin.springbootinterview.interview.spring.aop.service.impl.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Proxy;

/**
 * ============================================================================
 * 专题二：Spring AOP (面向切面编程) 核心问答与验证中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. 什么是 Spring AOP？主要想解决什么问题？
 *    - 解耦核心业务与系统级横切逻辑（事务、安全、日志、限流）。
 *    - 让业务代码专注于纯粹的核心实现，通用逻辑提取为可复用的切面（Aspect）。
 * 
 * 2. 切面具体在工程中的哪个地方？
 *    - 切面定义类位于：aspect/LoggingAspect.java
 *    - 核心注解体系：
 *      * @Aspect: 标记此类为切面。
 *      * @Component: 注册为 Spring 容器管理的 Bean。
 *      * @Pointcut: 指定切入点表达式，拦截 service 包下的所有方法。
 *      * @Around, @Before, @AfterReturning, @AfterThrowing, @After: 五大通知。
 * 
 * 3. 为什么之前运行 main 方法没有看到切面日志？
 *    - 因为原 main 方法前面两段是在演示“纯 Java 原生静态代理”和“底层 JDK 动态代理”，
 *      对象是手工 new 出来的（new UserServiceImpl()），脱离了 Spring IoC 容器。
 *    - Spring AOP 的核心前提：【目标对象必须由 Spring 容器创建与管理】！
 *      只有从 Spring 容器 getBean 获取的对象，才会被 BeanPostProcessor 织入 LoggingAspect 生成代理对象！
 *    - 下方第 3 部分已加入 Spring 容器启动并加载 LoggingAspect 的完整实测运行代码。
 * ============================================================================
 */
public class AopInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("1. 静态代理模式运行验证（纯原生 Java 手写）");
        System.out.println("=================================================");
        UserService target = new UserServiceImpl();
        UserService staticProxy = new UserStaticProxy(target);
        staticProxy.addUser("王五");

        System.out.println();
        System.out.println("=================================================");
        System.out.println("2. JDK 动态代理模式运行验证（纯原生 Java Proxy.newProxyInstance）");
        System.out.println("=================================================");
        UserService dynamicProxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionAndLoggingInvocationHandler(target)
        );
        dynamicProxy.deleteUser("UID_9999");
        System.out.println("动态代理类真实运行时类名: " + dynamicProxy.getClass().getName());

        System.out.println();
        System.out.println("=================================================");
        System.out.println("3. Spring 容器驱动 AOP 切面验证（LoggingAspect 真正生效的地方）");
        System.out.println("=================================================");
        // 启动独立的 Spring 注解上下文，开启包扫描与 @EnableAspectJAutoProxy
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopTestConfiguration.class);
        
        // 从容器中获取由 Spring 自动织入 LoggingAspect 后的代理 Bean
        UserService springAopProxy = context.getBean(UserService.class);
        System.out.println("Spring 容器中 UserService 的真实类型: " + springAopProxy.getClass().getName());
        
        // 执行业务方法，此时 LoggingAspect 中的通知将被完整触发！
        springAopProxy.addUser("张三(由Spring切面拦截)");

        // 关闭容器
        context.close();
    }
}

/**
 * Spring AOP 独立测试配置类
 * 
 * 教学解析：
 * 1. @Configuration: 声明为配置类。
 * 2. @ComponentScan: 扫描当前 aop 包下的 @Component（切面）和 @Service（目标业务类）。
 * 3. @EnableAspectJAutoProxy: 开启 Spring AOP 注解自动代理支持（底层激活 AnnotationAwareAspectJAutoProxyCreator）。
 */
@Configuration
@ComponentScan("com.jinlin.springbootinterview.interview.spring.aop")
@EnableAspectJAutoProxy
class AopTestConfiguration {
}
