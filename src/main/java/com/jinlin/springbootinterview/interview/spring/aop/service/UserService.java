package com.jinlin.springbootinterview.interview.spring.aop.service;

/**
 * 业务用户服务标准接口
 * 
 * 教学解析：
 * 1. 在企业级规范中，接口必须独立声明为 public interface 并存放于专用的 service 包下。
 * 2. 面向接口编程能够降低业务耦合，同时为 JDK 动态代理提供基于接口代理的类型契约。
 */
public interface UserService {

    /**
     * 添加用户
     * @param userName 用户名称
     */
    void addUser(String userName);

    /**
     * 删除用户
     * @param userId 用户唯一标识
     */
    void deleteUser(String userId);
}
