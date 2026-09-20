package com.jinlin.springbootinterview.interview.spring.ioc.service;

import com.jinlin.springbootinterview.interview.spring.ioc.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单业务逻辑服务类
 * 
 * 教学解析：
 * 1. 控制反转：OrderService 不负责 new OrderDaoImpl()，完全交给容器。
 * 2. 依赖注入：声明了依赖属性 orderDao，由容器在初始化阶段反射注入。
 */
@Service
public class OrderService {

    private OrderDao orderDao;

    public OrderService() {
    }

    /**
     * 构造器注入（Spring 官方最推荐方式）
     */
    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void createOrder(String orderId) {
        System.out.println("[业务逻辑层] 正在处理创建订单业务，调用持久层接口...");
        if (orderDao == null) {
            throw new IllegalStateException("依赖注入失败: orderDao 为 null");
        }
        orderDao.insert(orderId);
        System.out.println("[业务逻辑层] 订单创建处理完成。");
    }
}
