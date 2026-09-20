package com.jinlin.springbootinterview.interview.spring.transaction.proxy;

import com.jinlin.springbootinterview.interview.spring.transaction.service.OrderTransactionService;

/**
 * 模拟 Spring 声明式事务代理类
 * 
 * 教学解析：
 * 清楚揭示 Spring 事务的本质：只有外部调用走代理对象，才会触发事务拦截。
 * 如果方法内部通过 this 访问，完全走的是原始对象内存方法，根本不会经过这个代理类！
 */
public class OrderServiceTransactionProxy implements OrderTransactionService {

    private final OrderTransactionService target;

    public OrderServiceTransactionProxy(OrderTransactionService target) {
        this.target = target;
    }

    @Override
    public void createOrderFlow() {
        System.out.println("[事务代理拦截] >>> 检查到调用 createOrderFlow，开启数据库连接与事务");
        try {
            target.createOrderFlow();
            System.out.println("[事务代理拦截] <<< 业务执行完成，提交事务 Connection.commit()");
        } catch (Exception e) {
            System.out.println("[事务代理拦截] <<< 业务捕获到异常，回滚事务 Connection.rollback()");
            throw e;
        }
    }

    @Override
    public void deductInventoryAndPoints() {
        System.out.println("[事务代理拦截] >>> 检查到外部直接调用 deductInventoryAndPoints，开启独立事务");
        try {
            target.deductInventoryAndPoints();
            System.out.println("[事务代理拦截] <<< 提交事务 Connection.commit()");
        } catch (Exception e) {
            System.out.println("[事务代理拦截] <<< 回滚事务 Connection.rollback()");
            throw e;
        }
    }
}
