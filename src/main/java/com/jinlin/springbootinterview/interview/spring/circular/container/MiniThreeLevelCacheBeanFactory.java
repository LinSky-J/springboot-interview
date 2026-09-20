package com.jinlin.springbootinterview.interview.spring.circular.container;

import com.jinlin.springbootinterview.interview.spring.circular.bean.ClassA;
import com.jinlin.springbootinterview.interview.spring.circular.bean.ClassB;
import com.jinlin.springbootinterview.interview.spring.circular.factory.MiniObjectFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟 Spring 三级缓存核心解决循环依赖容器
 * 
 * 教学解析：
 * 1. 一级缓存 singletonObjects：存储最终成熟的单例 Bean。
 * 2. 二级缓存 earlySingletonObjects：存储提前暴露的半成品 Bean，保证早期引用的唯一性。
 * 3. 三级缓存 singletonFactories：存储工厂对象，实现 AOP 代理对象的延迟生成。
 */
public class MiniThreeLevelCacheBeanFactory {

    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();
    private final Map<String, MiniObjectFactory<?>> singletonFactories = new HashMap<>();

    public Object getBean(String beanName) {
        Object singleton = getSingleton(beanName);
        if (singleton != null) {
            return singleton;
        }
        return doCreateBean(beanName);
    }

    private Object getSingleton(String beanName) {
        // 1. 查一级缓存
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 2. 查二级缓存
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                // 3. 查三级缓存
                MiniObjectFactory<?> factory = singletonFactories.get(beanName);
                if (factory != null) {
                    singletonObject = factory.getObject();
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                    System.out.println("[三级缓存] " + beanName + " 早期引用从三级缓存升迁至二级缓存");
                }
            }
        }
        return singletonObject;
    }

    private Object doCreateBean(String beanName) {
        System.out.println("[实例化阶段] 反射创建原始对象: " + beanName);
        Object rawInstance;
        if ("classA".equals(beanName)) {
            rawInstance = new ClassA();
        } else {
            rawInstance = new ClassB();
        }

        final Object exposedInstance = rawInstance;
        singletonFactories.put(beanName, () -> {
            System.out.println("[三级缓存执行] 触发工厂创建早期对象引用: " + beanName);
            return exposedInstance;
        });

        System.out.println("[属性填充阶段] 正在注入依赖: " + beanName);
        if ("classA".equals(beanName)) {
            ClassA a = (ClassA) exposedInstance;
            a.setClassB((ClassB) getBean("classB"));
        } else if ("classB".equals(beanName)) {
            ClassB b = (ClassB) exposedInstance;
            b.setClassA((ClassA) getBean("classA"));
        }

        System.out.println("[初始化阶段完成] " + beanName + " 成熟就绪，存入一级缓存");
        Object finalBean = earlySingletonObjects.getOrDefault(beanName, exposedInstance);
        singletonObjects.put(beanName, finalBean);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);

        return finalBean;
    }
}
