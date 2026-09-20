package com.jinlin.springbootinterview.interview.spring.lifecycle.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 完整生命周期演示 Bean
 * 
 * 教学解析：
 * 实现了 BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean，
 * 并结合 @PostConstruct 与 @PreDestroy 注解，演示生命周期的完整执行流程。
 */
public class DemoLifecycleUserBean implements BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean {

    private String userName;

    public DemoLifecycleUserBean() {
        System.out.println("[生命周期 步骤 1] 构造函数调用：实例化 (Instantiation)");
    }

    public void setUserName(String userName) {
        this.userName = userName;
        System.out.println("[生命周期 步骤 2] 属性赋值 (Populate Bean): userName = " + userName);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("[生命周期 步骤 3-1] BeanNameAware 回调：感知到当前 BeanName = " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("[生命周期 步骤 3-2] BeanFactoryAware 回调：感知到持有当前容器工厂");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("[生命周期 步骤 4] @PostConstruct 注解方法执行（初始化前置处理阶段）");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[生命周期 步骤 5] InitializingBean.afterPropertiesSet() 执行：核心属性校验完成");
    }

    public void customInit() {
        System.out.println("[生命周期 步骤 6] 自定义 init-method 执行：业务定制初始化完毕");
    }

    public void doWork() {
        System.out.println("[生命周期 步骤 7] Bean 处于完全就绪状态，正在承接业务调用...");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("[生命周期 步骤 8-1] @PreDestroy 注解方法执行：准备销毁");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("[生命周期 步骤 8-2] DisposableBean.destroy() 执行：释放底层资源");
    }

    public void customDestroy() {
        System.out.println("[生命周期 步骤 8-3] 自定义 destroy-method 执行：最终销毁完毕");
    }
}
