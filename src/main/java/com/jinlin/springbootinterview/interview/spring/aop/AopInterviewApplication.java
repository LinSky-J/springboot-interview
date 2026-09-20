package com.jinlin.springbootinterview.interview.spring.aop;

import com.jinlin.springbootinterview.interview.spring.aop.proxy.TransactionAndLoggingInvocationHandler;
import com.jinlin.springbootinterview.interview.spring.aop.proxy.UserStaticProxy;
import com.jinlin.springbootinterview.interview.spring.aop.service.UserService;
import com.jinlin.springbootinterview.interview.spring.aop.service.impl.UserServiceImpl;

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
 * 2. 动态代理和静态代理的区别？能否用静态代理实现 AOP？
 *    - 静态代理：编译期生成代理类，强耦合接口，代码膨胀。
 *    - 动态代理：运行期利用反射/字节码动态生成代理，灵活解耦。
 *    - 能否用静态代理实现 AOP：完全可以！典型代表如 AspectJ（编译期/类加载期静态织入）。
 * 
 * 3. Spring AOP 底层实现机制：JDK 动态代理 vs CGLIB
 *    - JDK 动态代理：针对接口，利用 java.lang.reflect.Proxy 动态生成实现了相同接口的字节码。
 *    - CGLIB 代理：针对类，利用 ASM 字节码技术动态继承目标类生成子类。
 *    - Spring Boot 2.x 默认策略：spring.aop.proxy-target-class=true，优先使用 CGLIB。
 * ============================================================================
 */
public class AopInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("1. 静态代理模式运行验证");
        System.out.println("=================================================");
        UserService target = new UserServiceImpl();
        UserService staticProxy = new UserStaticProxy(target);
        staticProxy.addUser("王五");

        System.out.println();
        System.out.println("=================================================");
        System.out.println("2. JDK 动态代理模式运行验证");
        System.out.println("=================================================");
        UserService dynamicProxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionAndLoggingInvocationHandler(target)
        );
        dynamicProxy.deleteUser("UID_9999");
        System.out.println("动态代理类真实运行时类名: " + dynamicProxy.getClass().getName());
    }
}
