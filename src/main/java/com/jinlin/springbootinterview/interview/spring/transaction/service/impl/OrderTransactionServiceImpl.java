package com.jinlin.springbootinterview.interview.spring.transaction.service.impl;

import com.jinlin.springbootinterview.interview.spring.transaction.service.OrderTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单事务服务实现类
 * 
 * 教学解析：
 * 重点演示：同一个类中 this 调用导致的事务失效问题，以及如何通过注入自身代理对象解决。
 */
@Service
public class OrderTransactionServiceImpl implements OrderTransactionService {

    @Override
    public void createOrderFlow() {
        System.out.println("[业务执行] 进入 createOrderFlow 主业务流程，准备调用 deductInventoryAndPoints...");
        
        // 错误示范：使用 this 自调用，彻底绕过了 AOP 代理，事务注解不会生效！
        this.deductInventoryAndPoints();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deductInventoryAndPoints() {
        System.out.println("[业务执行] 执行扣减库存与扣减积分核心 SQL。");
    }
}
