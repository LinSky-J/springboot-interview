package com.jinlin.springbootinterview.interview.designpatterns.structural;

/**
 * ============================================================================
 * 13. 桥接模式 (Bridge Pattern)【多维度独立变化解耦利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将抽象部分与它的实现部分分离，使它们都可以独立地变化。
 *    通过“对象组合（桥接）”代替“类继承”，解决由于多维度变化导致的子类数量爆炸问题。
 * 
 * 2. 经典生动比喻（毛笔 vs 蜡笔）：
 *    - 蜡笔体系（纯继承）：
 *      如果有 3 种粗细型号的大中小蜡笔，12 种颜色，必须生产 3 x 12 = 36 支独立的蜡笔！
 *      每增加一种颜色或型号，子类成倍暴增。
 *    - 毛笔体系（桥接模式）：
 *      只需要 3 支不同粗细的毛笔（抽象），配上 12 盒颜色墨水（实现）。
 *      毛笔与墨水通过“蘸墨”动作（组合桥接）结合，只需 3 + 12 = 15 个对象即可组合出 36 种效果！
 * 
 * 3. 经典工业级应用：
 *    - Java JDBC 驱动架构：
 *      java.sql.DriverManager、Connection、Statement 是官方抽象层；
 *      MySQL Driver、Oracle Driver 是各大数据库厂商的具体底层实现层，两者通过桥接组合解耦。
 * ============================================================================
 */
public class Pattern13_BridgePatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("桥接模式：消息类型（普通/加急）与发送渠道（短信/邮件）组合");
        System.out.println("=================================================");

        // 1. 发送普通短信
        MessageSender smsSender = new SmsMessageSender();
        AbstractNotification normalSms = new NormalNotification(smsSender);
        normalSms.notifyUser("您的验证码为 123456");

        System.out.println();

        // 2. 发送特急邮件（无需新增类，自由组合即可）
        MessageSender emailSender = new EmailMessageSender();
        AbstractNotification urgentEmail = new UrgentNotification(emailSender);
        urgentEmail.notifyUser("服务器 CPU 使用率超过 95%！");
    }
}

/**
 * 实现化角色：消息发送渠道接口
 */
interface MessageSender {
    void send(String message);
}

/**
 * 具体实现化1：短信发送渠道
 */
class SmsMessageSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("[短信网关] 正在通过运营商 SMS 通道发送: " + message);
    }
}

/**
 * 具体实现化2：邮件发送渠道
 */
class EmailMessageSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("[邮件服务器] 正在通过 SMTP 协议发送邮件: " + message);
    }
}

/**
 * 抽象化角色：通知层级抽象类（桥梁核心：聚合持有 MessageSender 接口）
 */
abstract class AbstractNotification {

    // 关键点：通过组合建立跨越维度的“桥梁”
    protected final MessageSender sender;

    public AbstractNotification(MessageSender sender) {
        this.sender = sender;
    }

    public abstract void notifyUser(String message);
}

/**
 * 扩展抽象化1：普通通知
 */
class NormalNotification extends AbstractNotification {

    public NormalNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notifyUser(String message) {
        System.out.println("[通知级别: 普通]");
        sender.send(message);
    }
}

/**
 * 扩展抽象化2：加急通知
 */
class UrgentNotification extends AbstractNotification {

    public UrgentNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notifyUser(String message) {
        System.out.println("[通知级别: 紧急警报！]");
        sender.send("【URGENT】" + message);
    }
}
