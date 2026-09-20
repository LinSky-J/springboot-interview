package com.jinlin.springbootinterview.interview.spring.transaction.service;

/**
 * 订单事务服务标准接口
 */
public interface OrderTransactionService {

    /**
     * 外部调用的入口方法
     */
    void createOrderFlow();

    /**
     * 内部关键扣库存方法（带有声明式事务注解）
     */
    void deductInventoryAndPoints();
}
