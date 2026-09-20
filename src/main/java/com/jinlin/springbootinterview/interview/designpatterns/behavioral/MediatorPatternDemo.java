package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * 18. 中介者模式 (Mediator Pattern)【网状错综复杂依赖转为星型调度利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    用一个中介对象来封装一系列的对象交互。中介者使各对象不需要显式地相互引用，
 *    从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * 
 * 2. 核心架构拓扑转变：网状拓扑 -> 星型拓扑
 *    - 传统网状结构：
 *      A 与 B、C、D 均有依赖，B 也与 A、C、D 依赖，整个系统形成庞杂的“蜘蛛网”，
 *      任何一个对象的变动都会牵一发而动全身（牵连修改）。
 *    - 中介者星型结构：
 *      所有同事类（Colleague）仅与中心中介者（Mediator）通信。
 *      网状调用的复杂度被收敛封印在中介者内部，使得各个同事对象高度解耦、职责单一。
 * 
 * 3. 经典生动比喻：
 *    - 机场塔台空中管制（Air Traffic Control）：
 *      各架飞机之间不需要互相呼叫协商“谁先降落、谁先起飞”，所有飞行员只需听从机场塔台调度指挥。
 *    - 在线聊天室：
 *      群聊成员无需互相建立私聊连接，全部通过聊天室服务器（中介者）进行分发广播。
 * 
 * 4. 经典框架与源码应用：
 *    - Spring MVC 的核心前端控制器 DispatcherServlet：
 *      作为最经典的中介者，负责协调 HandlerMapping（映射）、HandlerAdapter（执行适配）、
 *      HandlerExceptionResolver（异常解析）、ViewResolver（视图解析）等组件的流转协同。
 *    - 消息队列中间件（MQ Broker）：
 *      解耦庞大分布式微服务系统之间的直接 RPC 依赖。
 * ============================================================================
 */
public class MediatorPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("中介者模式：在线多人协同聊天室（解耦成员间错综复杂的通信）");
        System.out.println("=================================================");

        // 1. 创建中心中介者（协同中枢）
        ChatMediator chatRoom = new TeamChatRoom();

        // 2. 创建同事成员并注册加入中介者体系
        User colleagueAlice = new ConcreteUser(chatRoom, "Alice(前端组长)");
        User colleagueBob = new ConcreteUser(chatRoom, "Bob(后端主程)");
        User colleagueCharlie = new ConcreteUser(chatRoom, "Charlie(测试主管)");

        chatRoom.registerUser(colleagueAlice);
        chatRoom.registerUser(colleagueBob);
        chatRoom.registerUser(colleagueCharlie);

        // 3. 成员通过中介者发送协同消息（无需关心其他成员都有谁、网络地址是什么）
        System.out.println("\n--- Alice 在聊天室广播通知 ---");
        colleagueAlice.send("接口文档已更新，联调测试请关注！");

        System.out.println("\n--- Bob 在聊天室广播回复 ---");
        colleagueBob.send("收到，后端本地接口已经部署完成！");
    }
}

/**
 * 抽象中介者接口 (Mediator)：定义同事对象通信的统一协作接口
 */
interface ChatMediator {
    void registerUser(User user);
    void sendMessage(String message, User sender);
}

/**
 * 抽象同事类 (Colleague)：持有中介者引用，所有与外部的交互均委托给中介者
 */
abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 发送消息
    public abstract void send(String message);

    // 接收消息
    public abstract void receive(String message, String fromUserName);
}

/**
 * 具体中介者 (ConcreteMediator)：维护所有同事的注册列表，协调各个同事间的交互逻辑
 */
class TeamChatRoom implements ChatMediator {

    private final List<User> userList = new ArrayList<>();

    @Override
    public void registerUser(User user) {
        userList.add(user);
        System.out.println("[聊天室通知] " + user.getName() + " 加入了项目协同群组。");
    }

    @Override
    public void sendMessage(String message, User sender) {
        // 核心协调：收到某个同事的消息后，精准转发给群内其他所有人（排除发送者本人）
        for (User user : userList) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}

/**
 * 具体同事类 (ConcreteColleague)：实现自身专有的业务行为
 */
class ConcreteUser extends User {

    public ConcreteUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    public void send(String message) {
        System.out.println("【" + this.name + "】发出消息: \"" + message + "\"");
        // 关键解耦点：不直接调用其他 User，而是全权委托给中介者调度
        mediator.sendMessage(message, this);
    }

    @Override
    public void receive(String message, String fromUserName) {
        System.out.println("  -> 【" + this.name + "】接收到来自【" + fromUserName + "】的消息: " + message);
    }
}
