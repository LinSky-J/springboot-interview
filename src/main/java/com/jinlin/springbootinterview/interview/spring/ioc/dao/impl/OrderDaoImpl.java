package com.jinlin.springbootinterview.interview.spring.ioc.dao.impl;

import com.jinlin.springbootinterview.interview.spring.ioc.dao.OrderDao;
import org.springframework.stereotype.Repository;

/**
 * 订单数据访问层具体实现
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    @Override
    public void insert(String orderId) {
        System.out.println("[数据持久层] 成功将订单落盘持久化至 MySQL: " + orderId);
    }
}
