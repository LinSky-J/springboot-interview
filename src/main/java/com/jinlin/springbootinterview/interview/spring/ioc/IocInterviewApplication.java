package com.jinlin.springbootinterview.interview.spring.ioc;

import com.jinlin.springbootinterview.interview.spring.ioc.container.MiniApplicationContext;
import com.jinlin.springbootinterview.interview.spring.ioc.dao.impl.OrderDaoImpl;
import com.jinlin.springbootinterview.interview.spring.ioc.model.MiniBeanDefinition;
import com.jinlin.springbootinterview.interview.spring.ioc.service.OrderService;

/**
 * ============================================================================
 * 专题一：Spring IoC (控制反转) 与 DI (依赖注入) 验证与问答中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. 依赖倒置（DIP）、控制反转（IoC）与依赖注入（DI）的关系：
 *    - DIP：面向对象设计原则（高层和低层模块均依赖抽象，抽象不依赖细节）。
 *    - IoC：架构设计模式，把对象的生命周期和装配控制权交给第三方容器。
 *    - DI：IoC 的具体技术实现手段（构造器注入、Setter注入、反射字段注入）。
 * 
 * 2. 什么是反射？有哪些核心使用场景？
 *    - 在运行期动态获取类的全部信息并能够操作对象的方法与属性。
 *    - 场景：Spring 容器装配、动态代理、MyBatis ORM 映射、JSON 序列化。
 * 
 * 3. 设计一个 Spring IoC 容器的关键模块：
 *    - BeanDefinition（元数据）
 *    - BeanDefinitionReader（扫描与解析）
 *    - BeanDefinitionRegistry（注册表）
 *    - singletonObjects（单例池/一级缓存）
 *    - BeanPostProcessor（初始化前后切面扩展）
 * ============================================================================
 */
public class IocInterviewApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("=================================================");
        System.out.println("运行 Mini-IoC 容器：验证反射装配与依赖注入闭环");
        System.out.println("=================================================");

        MiniApplicationContext context = new MiniApplicationContext();
        context.registerBeanDefinition("orderDao", new MiniBeanDefinition(OrderDaoImpl.class));
        context.registerBeanDefinition("orderService", new MiniBeanDefinition(OrderService.class));

        context.refresh();

        OrderService orderService = (OrderService) context.getBean("orderService");
        orderService.createOrder("ORDER_SPRING_20260917");
    }
}
