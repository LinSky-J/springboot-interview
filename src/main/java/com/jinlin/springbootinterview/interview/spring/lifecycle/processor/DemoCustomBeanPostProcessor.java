package com.jinlin.springbootinterview.interview.spring.lifecycle.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 自定义 Bean 后置处理器
 * 
 * 教学解析：
 * 1. 作用：拦截所有 Bean 的初始化方法前后。
 * 2. postProcessBeforeInitialization：在 @PostConstruct / InitializingBean 之前执行。
 * 3. postProcessAfterInitialization：在初始化完成后执行，Spring AOP 动态代理正是在此阶段生成并替换原对象！
 */
@Component
public class DemoCustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.contains("demoLifecycleUserBean")) {
            System.out.println("[BeanPostProcessor 前置拦截] 正在初始化 Bean 前进行安全审计与增强: " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.contains("demoLifecycleUserBean")) {
            System.out.println("[BeanPostProcessor 后置拦截] 初始化已完成，AOP 代理若存在将在此处生成: " + beanName);
        }
        return bean;
    }
}
