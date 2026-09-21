package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * 5. 观察者模式 (Observer Pattern)【必学，事件解耦发布-订阅基石】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    定义对象间的一种一对多的依赖关系，当一个对象（被观察者/主题 Subject）的状态发生改变时，
 *    所有依赖于它的对象（观察者 Observer）都会得到通知并自动完成更新。
 * 
 * 2. 为什么需要观察者模式？
 *    - 业务解耦：比如“用户注册成功后”，需要发送欢迎短信、发放优惠券、初始化积分账户、通知风控系统。
 *    - 如果写在一个方法里，代码将极其臃肿且严重违背单一职责；通过观察者模式（发布-订阅），
 *      主体只管 publishEvent，各个业务监听器自行订阅处理，高内聚、低耦合。
 * 
 * 3. 经典工业级应用：
 *    - Spring 事件驱动模型：ApplicationEvent、ApplicationListener、ApplicationEventPublisher。
 *    - GUI 事件监听：按钮点击监听 ActionListener。
 *    - 各种 MQ 消息中间件的主题订阅机制本质上也是广义的观察者模式。
 * ============================================================================
 */
public class Pattern05_ObserverPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("观察者模式：模拟用户注册成功事件的广播与多业务订阅响应");
        System.out.println("=================================================");

        // 1. 创建主题（被观察者）
        UserRegistrationSubject subject = new UserRegistrationSubject();

        // 2. 注册多个具体的观察者服务
        subject.registerObserver(new SmsNotificationObserver());
        subject.registerObserver(new CouponDistributionObserver());
        subject.registerObserver(new RiskControlAuditObserver());

        // 3. 触发业务事件，所有观察者自动收到通知
        subject.notifyAllObservers("用户【ID_888999】");
    }
}

/**
 * 抽象观察者接口
 */
interface OrderEventObserver {
    void onEvent(String message);
}

/**
 * 具体观察者1：短信通知服务
 */
class SmsNotificationObserver implements OrderEventObserver {
    @Override
    public void onEvent(String message) {
        System.out.println("[短信服务] 监听到事件 -> 正在发送注册成功欢迎短信至: " + message);
    }
}

/**
 * 具体观察者2：优惠券发放服务
 */
class CouponDistributionObserver implements OrderEventObserver {
    @Override
    public void onEvent(String message) {
        System.out.println("[营销服务] 监听到事件 -> 正在向新用户账户发放 ¥100 优惠券礼包: " + message);
    }
}

/**
 * 具体观察者3：安全风控审计服务
 */
class RiskControlAuditObserver implements OrderEventObserver {
    @Override
    public void onEvent(String message) {
        System.out.println("[风控系统] 监听到事件 -> 记录新用户环境指纹并建立安全画像: " + message);
    }
}

/**
 * 被观察者主体（Subject / 事件广播发布中心）
 */
class UserRegistrationSubject {

    private final List<OrderEventObserver> observers = new ArrayList<>();

    public void registerObserver(OrderEventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderEventObserver observer) {
        observers.remove(observer);
    }

    /**
     * 状态变更，循环通知所有注册的观察者
     */
    public void notifyAllObservers(String message) {
        System.out.println("[事件发布中心] 广播事件: " + message + " 注册成功！");
        for (OrderEventObserver observer : observers) {
            observer.onEvent(message);
        }
    }
}
