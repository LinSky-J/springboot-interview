package com.jinlin.springbootinterview.interview.springboot.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * SpringBoot 声明式事务开启机制与使用规范
 * 
 * 教学解析：SpringBoot 怎么开启事务？（高频踩坑与原理解答）
 * 
 * 1. 核心结论：在 SpringBoot 中，开启事务【默认无需显式声明 @EnableTransactionManagement 注解】！
 * 
 * 2. 深入底层自动装配机制：
 *    - 为什么不用加 @EnableTransactionManagement？
 *      因为只要引入了 spring-boot-starter-jdbc 或 mybatis-spring-boot-starter，
 *      SpringBoot 的 TransactionAutoConfiguration（事务自动配置类）就会自动生效！
 *      在 TransactionAutoConfiguration 内部，已经内置声明了带有 @EnableTransactionManagement 的内部静态配置类。
 *    - 只要开发者在 application.yml 中配置好数据源，SpringBoot 就会自动向容器中注入
 *      DataSourceTransactionManager 事务管理器 Bean。
 * 
 * 3. 实际开启与使用规范：
 *    - 在需要保证原子性、一致性的业务 Service 类或具体方法上，直接打上 @Transactional 注解即可！
 *    - 生产级避坑建议：
 *      务必显式声明 rollbackFor = Exception.class！
 *      因为 SpringBoot 事务默认只对 RuntimeException（运行时异常）和 Error 触发回滚。
 *      如果业务抛出受检异常（Checked Exception，如 IOException、SQLException），默认不会回滚！
 */
@Service
public class SpringBootTransactionMechanism {

    /**
     * 规范的标准声明式事务业务方法
     */
    @Transactional(
            propagation = Propagation.REQUIRED,         // 传播行为：默认 REQUIRED（有事务加入，无事务新建）
            isolation = Isolation.READ_COMMITTED,       // 隔离级别：读已提交（防止脏读）
            rollbackFor = Exception.class,              // 回滚规则：发生任何 Exception 均触发回滚
            timeout = 30                                // 超时时间：30秒防长事务锁表
    )
    public void executeAccountTransfer(String fromAccount, String toAccount, Double amount) {
        System.out.println("[事务控制] 检查到 @Transactional，Spring 代理对象通过 Connection.setAutoCommit(false) 开启事务");
        System.out.println("[转账业务] 从账户 " + fromAccount + " 扣减金额: " + amount);
        System.out.println("[转账业务] 向账户 " + toAccount + " 增加金额: " + amount);

        if (amount <= 0) {
            throw new IllegalArgumentException("转账金额必须大于 0，触发事务自动回滚！");
        }

        System.out.println("[事务控制] 业务正常执行完毕，Spring 代理对象调用 Connection.commit() 提交事务");
    }
}
