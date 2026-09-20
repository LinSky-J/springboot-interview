package com.jinlin.springbootinterview.interview.spring.lifecycle;

import com.jinlin.springbootinterview.interview.spring.lifecycle.bean.DemoLifecycleUserBean;

/**
 * ============================================================================
 * 专题四：Spring Bean 生命周期、作用域与容器扩展点问答与验证中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. 单例与非单例（多例 prototype）的生命周期是否一样？
 *    - 完全不同！单例 Bean 从实例化、属性填充、初始化一直到销毁全流程由 Spring 统一管理。
 *    - 原型 Bean 容器创建并初始化后立即交给调用方，容器不持有其引用，更不负责调用销毁方法。
 * 
 * 2. Spring 容器里真正存的是什么？
 *    - DefaultListableBeanFactory 中的核心数据结构：
 *      * beanDefinitionMap: 存储类定义的元数据（作用域、类名、懒加载等）。
 *      * singletonObjects: 存储成熟单例对象的单例池（一级缓存）。
 *      * beanPostProcessors: 存储所有拦截后置处理器。
 * 
 * 3. Bean 完整的生命周期时序：
 *    实例化 -> 属性赋值 -> Aware 感知接口 -> BPP 前置 -> @PostConstruct -> InitializingBean ->
 *    自定义 init-method -> BPP 后置（AOP代理生成） -> 业务使用 -> @PreDestroy -> DisposableBean -> 自定义 destroy-method。
 * ============================================================================
 */
public class LifecycleInterviewApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("=================================================");
        System.out.println("模拟驱动 DemoLifecycleUserBean 完整生命周期各环节");
        System.out.println("=================================================");

        DemoLifecycleUserBean bean = new DemoLifecycleUserBean();
        bean.setUserName("赵六");
        bean.setBeanName("demoLifecycleUserBean");

        bean.postConstruct();
        bean.afterPropertiesSet();
        bean.customInit();

        bean.doWork();

        System.out.println("----------------- 模拟容器关闭销毁 -----------------");
        bean.preDestroy();
        bean.destroy();
        bean.customDestroy();
    }
}
