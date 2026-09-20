package com.jinlin.springbootinterview.interview.spring.ioc.container;

import com.jinlin.springbootinterview.interview.spring.ioc.model.MiniBeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟 Spring 核心 IoC 容器（微型 ApplicationContext）
 * 
 * 教学解析：
 * 1. 注册表：beanDefinitionMap 维护所有的类元数据。
 * 2. 单例池：singletonObjects（一级缓存）维护创建完毕的单例对象。
 * 3. 运行原理：通过反射创建实例并进行属性注入（DI）。
 */
public class MiniApplicationContext {

    private final Map<String, MiniBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public void registerBeanDefinition(String beanName, MiniBeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void refresh() throws Exception {
        for (Map.Entry<String, MiniBeanDefinition> entry : beanDefinitionMap.entrySet()) {
            getBean(entry.getKey());
        }
    }

    public Object getBean(String beanName) throws Exception {
        Object instance = singletonObjects.get(beanName);
        if (instance != null) {
            return instance;
        }

        MiniBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("未找到对应的 Bean 定义: " + beanName);
        }

        Class<?> clazz = beanDefinition.getBeanClass();
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object rawBean = constructor.newInstance();

        // 反射属性注入
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            for (Map.Entry<String, MiniBeanDefinition> candidate : beanDefinitionMap.entrySet()) {
                if (fieldType.isAssignableFrom(candidate.getValue().getBeanClass()) && !candidate.getKey().equals(beanName)) {
                    Object dependency = getBean(candidate.getKey());
                    field.setAccessible(true);
                    field.set(rawBean, dependency);
                    System.out.println("[IoC 容器] 成功将 " + candidate.getKey() + " 注入到 " + beanName);
                }
            }
        }

        singletonObjects.put(beanName, rawBean);
        return rawBean;
    }
}
