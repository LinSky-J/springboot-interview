package com.jinlin.springbootinterview.interview.designpatterns.creational;

/**
 * ============================================================================
 * 2. 工厂方法模式 (Factory Method Pattern)【必学】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    定义一个创建对象的抽象工厂接口，但由具体的子类工厂来决定实例化哪一个具体产品类。
 *    工厂方法把类的实例化逻辑延迟到了具体的工厂子类中。
 * 
 * 2. 为什么不用简单工厂（静态工厂）？
 *    - 简单工厂模式（非 GoF 23 种之一）：在一个单一工厂类中通过 switch-case 或 if-else 判断入参
 *      来 new 不同的产品。
 *    - 致命缺点：每增加一个新产品，都必须修改工厂类的原有代码，【严重违反了开闭原则 (OCP)】！
 *    - 工厂方法模式的解决之道：
 *      每增加一个新产品，只需扩展一个新的具体产品类和对应的具体工厂类，
 *      原有已有代码无需改动一行，【完全符合开闭原则】！
 * 
 * 3. 经典工业级应用：
 *    - Java 集合体系：Iterable 接口的 iterator() 方法就是典型的工厂方法，
 *      ArrayList 返回 ArrayListIterator，LinkedList 返回 ListItr。
 *    - Spring 体系：BeanFactory 顶层规范。
 * ============================================================================
 */
public class Pattern02_FactoryMethodPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("工厂方法模式：通过具体工厂创建对应支付渠道实例");
        System.out.println("=================================================");

        // 业务需要微信支付：使用微信工厂
        PaymentFactory wxFactory = new WechatPaymentFactory();
        Payment wxPayment = wxFactory.createPayment();
        wxPayment.pay(100.0);

        // 业务需要支付宝支付：使用支付宝工厂（扩展新渠道时无需修改微信工厂）
        PaymentFactory aliFactory = new AliPaymentFactory();
        Payment aliPayment = aliFactory.createPayment();
        aliPayment.pay(200.0);
    }
}

/**
 * 抽象产品接口：支付渠道
 */
interface Payment {
    void pay(double amount);
}

/**
 * 具体产品1：微信支付
 */
class WechatPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("[微信支付] 成功发起微信扫码支付，金额: ¥" + amount);
    }
}

/**
 * 具体产品2：支付宝支付
 */
class AliPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("[支付宝支付] 成功发起支付宝快捷支付，金额: ¥" + amount);
    }
}

/**
 * 抽象工厂接口
 */
interface PaymentFactory {
    Payment createPayment();
}

/**
 * 具体工厂1：专门生产微信支付对象
 */
class WechatPaymentFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new WechatPayment();
    }
}

/**
 * 具体工厂2：专门生产支付宝支付对象
 */
class AliPaymentFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new AliPayment();
    }
}
