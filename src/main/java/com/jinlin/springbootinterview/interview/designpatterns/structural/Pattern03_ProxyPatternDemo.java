package com.jinlin.springbootinterview.interview.designpatterns.structural;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ============================================================================
 * 3. 代理模式 (Proxy Pattern)【必学，面试极高频重点】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    为其他对象提供一种代理以控制对这个对象的访问，在不改变原有目标对象代码的前提下，
 *    通过代理对象对其进行功能增强（如增加安全鉴权、日志追踪、事务控制、性能统计等）。
 * 
 * 2. 静态代理 vs 动态代理（JDK 动态代理 vs CGLIB）：
 *    - 静态代理：由程序员在编译期手工编写代理类，代理类与目标类必须实现相同接口，
 *      一旦接口新增方法，所有实现类与代理类均需修改，代码冗余且严重耦合。
 *    - JDK 动态代理：Java 官方内置，基于反射包 java.lang.reflect.Proxy 在运行期动态生成字节码。
 *      【要求】：目标类必须实现至少一个接口（因为生成的代理类默认继承 Proxy 类，Java 单继承限制其无法再继承目标类）。
 *    - CGLIB 动态代理：基于开源 ASM 字节码生成框架，在运行期动态生成目标类的【子类】进行代理。
 *      【要求】：目标类不能是 final，被代理的方法也不能是 final / private。
 * 
 * 3. 经典工业级应用：
 *    - Spring AOP 面向切面编程与 @Transactional 声明式事务。
 *    - MyBatis Mapper 接口动态代理（MapperProxy）。
 *    - RPC 远程方法调用客户端桩对象（Dubbo / Feign）。
 * ============================================================================
 */
public class Pattern03_ProxyPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("代理模式：对比静态代理与 JDK 动态代理的增强执行");
        System.out.println("=================================================");

        OrderService realOrderService = new OrderServiceImpl();

        // 1. 静态代理测试
        OrderService staticProxy = new OrderServiceStaticProxy(realOrderService);
        staticProxy.createOrder("ORDER_10001");

        System.out.println();

        // 2. JDK 动态代理测试
        OrderService dynamicProxy = (OrderService) Proxy.newProxyInstance(
                realOrderService.getClass().getClassLoader(),
                realOrderService.getClass().getInterfaces(),
                new OrderDynamicProxyHandler(realOrderService)
        );
        dynamicProxy.createOrder("ORDER_10002");
        System.out.println("动态代理类真实运行时类名: " + dynamicProxy.getClass().getName());
    }
}

/**
 * 业务订单接口
 */
interface OrderService {
    void createOrder(String orderId);
}

/**
 * 真实目标业务实现类
 */
class OrderServiceImpl implements OrderService {
    @Override
    public void createOrder(String orderId) {
        System.out.println("[目标核心业务] 订单数据落盘持久化成功: " + orderId);
    }
}

/**
 * 静态代理类（需实现相同接口）
 */
class OrderServiceStaticProxy implements OrderService {
    private final OrderService target;

    public OrderServiceStaticProxy(OrderService target) {
        this.target = target;
    }

    @Override
    public void createOrder(String orderId) {
        System.out.println("[静态代理前置] 开启声明式事务 Transaction.begin()");
        target.createOrder(orderId);
        System.out.println("[静态代理后置] 提交声明式事务 Transaction.commit()");
    }
}

/**
 * JDK 动态代理调用处理器
 */
class OrderDynamicProxyHandler implements InvocationHandler {
    private final Object target;

    public OrderDynamicProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("[JDK 动态代理切面] 统一记录操作日志，方法: " + method.getName());

        Object result = method.invoke(target, args);

        long cost = System.currentTimeMillis() - start;
        System.out.println("[JDK 动态代理切面] 方法执行结束，执行耗时: " + cost + "ms");
        return result;
    }
}
