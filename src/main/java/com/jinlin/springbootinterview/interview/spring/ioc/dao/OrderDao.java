package com.jinlin.springbootinterview.interview.spring.ioc.dao;

/**
 * 订单数据访问层标准接口
 */
public interface OrderDao {

    /**
     * 保存订单数据
     * @param orderId 订单编号
     */
    void insert(String orderId);
}
