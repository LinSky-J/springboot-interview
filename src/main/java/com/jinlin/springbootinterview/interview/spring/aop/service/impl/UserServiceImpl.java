package com.jinlin.springbootinterview.interview.spring.aop.service.impl;

import com.jinlin.springbootinterview.interview.spring.aop.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 业务用户服务实现类（AOP 目标对象 Target）
 * 
 * 教学解析：
 * 1. 独立放在 service.impl 包下，符合单一职责与工程模块化分层规范。
 * 2. 这里的实现类仅关注核心业务逻辑（向数据库插入或删除），无需混杂事务、日志等横切代码。
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public void addUser(String userName) {
        System.out.println("[业务核心] 正在向数据库添加用户记录: " + userName);
    }

    @Override
    public void deleteUser(String userId) {
        System.out.println("[业务核心] 正在从数据库删除用户记录: " + userId);
    }
}
