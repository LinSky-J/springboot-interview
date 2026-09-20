package com.jinlin.springbootinterview.interview.spring.circular;

import com.jinlin.springbootinterview.interview.spring.circular.bean.ClassA;
import com.jinlin.springbootinterview.interview.spring.circular.bean.ClassB;
import com.jinlin.springbootinterview.interview.spring.circular.container.MiniThreeLevelCacheBeanFactory;

/**
 * ============================================================================
 * 专题三：Spring 三级缓存与循环依赖问答与验证中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. 为什么用三级缓存？用二级缓存不行吗？
 *    - 如果没有 AOP，二级缓存完全足够。
 *    - 必须用三级缓存的本质原因：AOP 代理对象延迟生成的原则。
 *      Spring 规范要求正常情况下 AOP 代理必须在 Bean 初始化完成后（postProcessAfterInitialization）生成。
 *      三级缓存放入工厂对象，只有在发生循环依赖时才会提前执行 getEarlyBeanReference 生成代理对象。
 * 
 * 2. 哪些循环依赖无法解决？
 *    - 构造器注入循环依赖（实例化阻塞，无法暴露早期引用）。
 *    - prototype 原型作用域循环依赖（不缓存多例）。
 *    - @Async 异步注解引起的循环依赖（异步代理由独立的 BeanPostProcessor 生成，导致引用不一致）。
 * ============================================================================
 */
public class CircularDependencyInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("运行三级缓存模拟容器：验证 ClassA 与 ClassB 循环依赖解析");
        System.out.println("=================================================");

        MiniThreeLevelCacheBeanFactory factory = new MiniThreeLevelCacheBeanFactory();
        ClassA a = (ClassA) factory.getBean("classA");
        ClassB b = (ClassB) factory.getBean("classB");

        System.out.println("验证 A.getClassB() 是否不为 null: " + (a.getClassB() != null));
        System.out.println("验证 B.getClassA() 是否不为 null: " + (b.getClassA() != null));
        System.out.println("验证 B 注入的 A 与全局单例 A 是否是同一内存地址: " + (b.getClassA() == a));
    }
}
