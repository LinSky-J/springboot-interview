package com.jinlin.springbootinterview.interview.spring.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 动态代理通用调用处理器（InvocationHandler）
 * 
 * 教学解析：
 * 1. 动态代理的核心思想：横切逻辑只写一份，在运行期动态织入。
 * 2. invoke 方法参数：
 *    - proxy: JVM 动态生成的代理类实例。
 *    - method: 当前被调用的接口方法反射对象。
 *    - args: 方法入参实参列表。
 * 3. 为什么能通用？
 *    通过 Object target 泛化持有任意目标对象，利用 method.invoke(target, args)
 *    可在不修改任何业务类的前提下拦截成千上万个业务方法。
 */
public class TransactionAndLoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public TransactionAndLoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("[JDK动态代理-切面拦截] >>> 调用方法: " + method.getName());
        System.out.println("[JDK动态代理-切面拦截] 模拟开启底层事务 Transaction.begin()");

        Object result = null;
        try {
            // 反射调用目标对象的真实核心业务逻辑
            result = method.invoke(target, args);

            System.out.println("[JDK动态代理-切面拦截] 业务执行成功，模拟提交事务 Transaction.commit()");
        } catch (Exception e) {
            System.out.println("[JDK动态代理-切面拦截] 业务异常，模拟执行回滚 Transaction.rollback()");
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            System.out.println("[JDK动态代理-切面拦截] <<< 方法 " + method.getName() + " 执行完毕，耗时: " + cost + "ms");
        }

        return result;
    }
}
