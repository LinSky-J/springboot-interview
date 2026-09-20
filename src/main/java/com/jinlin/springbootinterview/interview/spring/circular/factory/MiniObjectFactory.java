package com.jinlin.springbootinterview.interview.spring.circular.factory;

/**
 * 模拟 Spring 中的 ObjectFactory 函数式接口
 * 
 * 教学解析：
 * 对应 org.springframework.beans.factory.ObjectFactory。
 * 存放在三级缓存中，提供懒加载获取早期 Bean 引用的能力（支持提前触发 AOP 代理）。
 */
@FunctionalInterface
public interface MiniObjectFactory<T> {

    /**
     * 获取对象
     * @return 目标实例或提前生成的代理对象
     */
    T getObject();
}
