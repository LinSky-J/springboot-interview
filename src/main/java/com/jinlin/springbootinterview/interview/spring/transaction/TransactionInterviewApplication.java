package com.jinlin.springbootinterview.interview.spring.transaction;

import com.jinlin.springbootinterview.interview.spring.transaction.proxy.OrderServiceTransactionProxy;
import com.jinlin.springbootinterview.interview.spring.transaction.service.OrderTransactionService;
import com.jinlin.springbootinterview.interview.spring.transaction.service.impl.OrderTransactionServiceImpl;

/**
 * ============================================================================
 * 专题五：Spring 声明式事务底层机制与失效场景问答与验证中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. 同一个类中 this 调用事务为什么失效？
 *    - Spring 声明式事务基于 AOP 动态代理。外部调用代理对象才能进入 TransactionInterceptor 拦截器。
 *    - 当在方法内部使用 this.xxx() 时，获取的是目标对象自身的直接引用，绕过了代理对象，导致事务拦截失效。
 *    - 解决方案：
 *      (1) 抽取到独立的 Service 类并注入调用（架构最佳实践）。
 *      (2) 注入自身代理对象（自注入 @Autowired private OrderTransactionService self）。
 *      (3) 使用 AopContext.currentProxy() 强制获取当前代理对象。
 *      (4) 使用 ApplicationContext.getBean 动态获取代理对象。
 * 
 * 2. 事务失效的经典八大场景：
 *    - 非 public 方法。
 *    - this 内部自调用。
 *    - 异常被内部 try-catch 吞掉未向外抛出。
 *    - 抛出受检异常（Checked Exception）未配置 rollbackFor = Exception.class。
 *    - 事务传播行为配置为非事务传播（如 NOT_SUPPORTED / NEVER）。
 *    - 数据库存储引擎不支持（如 MyISAM）。
 *    - 类没有被 Spring 容器管理。
 *    - 多线程并发操作（子线程无法共享父线程 ThreadLocal 中的数据库 Connection）。
 * ============================================================================
 */
public class TransactionInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("运行事务代理验证：直观呈现代理调用与 this 自调用行为差异");
        System.out.println("=================================================");

        OrderTransactionService rawService = new OrderTransactionServiceImpl();
        OrderTransactionService proxyService = new OrderServiceTransactionProxy(rawService);

        System.out.println("--- 步骤 1: 外部调用 createOrderFlow() ---");
        proxyService.createOrderFlow();

        System.out.println();
        System.out.println("--- 步骤 2: 外部直接调用 deductInventoryAndPoints() ---");
        proxyService.deductInventoryAndPoints();
    }
}
