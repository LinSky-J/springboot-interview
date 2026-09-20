package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * 19. 命令模式 (Command Pattern)【请求封装为对象与撤销/重做/异步排队利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将一个请求封装为一个对象，从而使你可用不同的请求对客户进行参数化；
 *    对请求排队或记录请求日志，以及支持可撤销的操作。
 * 
 * 2. 核心价值：请求发送者 (Invoker) 与接收执行者 (Receiver) 彻底解耦
 *    - 传统直接调用：按钮直接 new Light() 并调用 light.on()，按钮与具体电器紧密耦合。
 *    - 命令模式抽象：按钮只关联 Command 接口，按下按钮只执行 command.execute()。
 *      具体是开灯、关灯、拉开窗帘还是放音乐，由具体 Command 子类封装，运行时灵活注入。
 * 
 * 3. 衍生的重大工程能力：
 *    - 操作可撤销 (Undo) 与重做 (Redo)：命令对象保存执行前现场，实现 execute() 与 undo()。
 *    - 异步排队与宏命令批处理：命令对象可放入阻塞队列中供工作线程池逐一异步消费。
 *    - 操作审计与事务恢复日志：将命令序列化持久化到磁盘日志中，崩溃时重放命令恢复现场。
 * 
 * 4. 经典框架与底层源码应用：
 *    - JUC 并发包体系：Runnable / Callable 就是最标准的命令对象，
 *      ThreadPoolExecutor（线程池）就是调用者（Invoker），Worker 线程充当执行者。
 *    - Spring 框架：JdbcTemplate 中的 StatementCallback / ConnectionCallback。
 *    - CQRS（命令查询职责分离）架构：写入与更新操作全部抽象为 Command 消息分发。
 * ============================================================================
 */
public class CommandPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("命令模式：智能家居遥控器（支持参数化指令与多级撤销 Undo）");
        System.out.println("=================================================");

        // 1. 创建具体底层硬件执行者（Receiver）
        SmartLight livingRoomLight = new SmartLight("客厅主灯");

        // 2. 创建具体命令对象，绑定执行者
        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);

        // 3. 创建遥控器调用者（Invoker）
        SmartRemoteControl remote = new SmartRemoteControl();

        // 4. 用户操作：按下开灯
        System.out.println("\n--- 用户点击[开灯]按钮 ---");
        remote.executeCommand(lightOn);

        // 5. 用户操作：按下关灯
        System.out.println("\n--- 用户点击[关灯]按钮 ---");
        remote.executeCommand(lightOff);

        // 6. 用户操作：撤销刚才的关灯操作（恢复开灯）
        System.out.println("\n--- 用户点击[撤销(Undo)]按钮 ---");
        remote.undoLastCommand();

        // 7. 用户操作：再次撤销（恢复最开始的关灯状态）
        System.out.println("\n--- 用户再次点击[撤销(Undo)]按钮 ---");
        remote.undoLastCommand();
    }
}

/**
 * 抽象命令角色 (Command)：声明执行操作与撤销操作的抽象接口
 */
interface Command {
    void execute();
    void undo();
}

/**
 * 接收者角色 (Receiver)：真正执行具体硬件操作的实体对象
 */
class SmartLight {
    private final String location;

    public SmartLight(String location) {
        this.location = location;
    }

    public void turnOn() {
        System.out.println("[硬件响应] " + location + " 已点亮，暖光色温 4000K。");
    }

    public void turnOff() {
        System.out.println("[硬件响应] " + location + " 已熄灭电源关闭。");
    }
}

/**
 * 具体命令角色1：开灯命令 (LightOnCommand)
 */
class LightOnCommand implements Command {

    // 聚合持有具体硬件接收者
    private final SmartLight light;

    public LightOnCommand(SmartLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }

    @Override
    public void undo() {
        // 撤销开灯即为关灯
        System.out.println("[执行撤销动作]");
        light.turnOff();
    }
}

/**
 * 具体命令角色2：关灯命令 (LightOffCommand)
 */
class LightOffCommand implements Command {

    private final SmartLight light;

    public LightOffCommand(SmartLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }

    @Override
    public void undo() {
        // 撤销关灯即为开灯
        System.out.println("[执行撤销动作]");
        light.turnOn();
    }
}

/**
 * 请求者/调用者角色 (Invoker)：智能遥控器，维护命令历史栈以支持多级 Undo 撤销
 */
class SmartRemoteControl {

    // 历史命令栈（用于撤销操作记录）
    private final Deque<Command> historyStack = new ArrayDeque<>();

    public void executeCommand(Command command) {
        // 执行命令
        command.execute();
        // 压入历史栈中
        historyStack.push(command);
    }

    public void undoLastCommand() {
        if (historyStack.isEmpty()) {
            System.out.println("[提示] 当前没有可撤销的历史操作！");
            return;
        }
        // 弹出栈顶最近执行的命令
        Command lastCommand = historyStack.pop();
        // 触发其 undo 反向操作
        lastCommand.undo();
    }
}
