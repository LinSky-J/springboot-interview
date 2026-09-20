package com.jinlin.springbootinterview.interview.spring.aop.proxy;

import com.jinlin.springbootinterview.interview.spring.aop.service.UserService;

/**
 * 静态代理演示类
 * 
 * 教学解析：
 * 1. 静态代理的特征：在编译期就手动创建好代理类，代理类必须与目标类实现相同的接口。
 * 2. 缺点分析：
 *    - 冗余度高：接口中只要新增一个方法，目标类和所有代理类都必须修改。
 *    - 复用性差：静态代理只能专门为 UserService 服务，无法为 OrderService 等其他服务复用相同的增强逻辑。
 */
public class UserStaticProxy implements UserService {

    private final UserService target;

    public UserStaticProxy(UserService target) {
        this.target = target;
    }

    @Override
    public void addUser(String userName) {
        System.out.println("[静态代理-前置增强] 开启声明式事务...");
        target.addUser(userName);
        System.out.println("[静态代理-后置增强] 提交声明式事务...");
    }

    @Override
    public void deleteUser(String userId) {
        System.out.println("[静态代理-前置增强] 校验操作员安全权限...");
        target.deleteUser(userId);
        System.out.println("[静态代理-后置增强] 记录系统审计日志...");
    }
}
