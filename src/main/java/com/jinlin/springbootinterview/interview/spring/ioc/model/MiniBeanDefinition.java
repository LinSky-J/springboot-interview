package com.jinlin.springbootinterview.interview.spring.ioc.model;

/**
 * 模拟 Spring 中的 BeanDefinition（Bean 定义元数据）
 * 
 * 教学解析：
 * 容器启动时第一件事不是直接 new 对象，而是先扫描并解析类的元数据，
 * 封装成 BeanDefinition 保存在注册表（beanDefinitionMap）中。
 */
public class MiniBeanDefinition {

    private final Class<?> beanClass;
    private String scope = "singleton";
    private boolean lazyInit = false;

    public MiniBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}
