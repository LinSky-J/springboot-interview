package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

/**
 * ============================================================================
 * 8. 责任链模式 (Chain of Responsibility Pattern)【建议学，管道流水线处理利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将多个处理节点连接成一条链式结构，当请求到达时，依次沿着链路传递。
 *    每个处理节点只需关注自己的职责：要么处理请求并决定是否放行，要么拦截中断请求。
 * 
 * 2. 解决的核心工程痛点：
 *    - 解耦请求的“发送者”与多个“处理者”。发送者无需知道究竟由谁处理、如何处理。
 *    - 极佳的可扩展性：各个处理器节点之间松散耦合，可根据业务需要任意动态调整节点顺序、增加或删除节点。
 * 
 * 3. 经典工业级应用：
 *    - Java EE Servlet 规范中的 FilterChain（过滤器链）。
 *    - Spring MVC 的 HandlerExecutionChain（拦截器链）。
 *    - Netty 中的 ChannelPipeline / ChannelHandler。
 *    - 办公 OA 系统请假多级审批流程（组长 -> 部门经理 -> 总经理）。
 * ============================================================================
 */
public class Pattern08_ChainOfResponsibilityPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("责任链模式：模拟 OA 员工请假多级审批审批链");
        System.out.println("=================================================");

        // 1. 构建责任链节点
        Approver groupLeader = new GroupLeader("张组长");
        Approver deptManager = new DepartmentManager("李经理");
        Approver generalManager = new GeneralManager("王总经理");

        // 2. 组装链条顺序：组长 -> 部门经理 -> 总经理
        groupLeader.setNext(deptManager).setNext(generalManager);

        // 3. 测试不同请假天数的流转
        System.out.println("--- 提交 2 天请假单 ---");
        groupLeader.processLeaveRequest(2);

        System.out.println();
        System.out.println("--- 提交 5 天请假单 ---");
        groupLeader.processLeaveRequest(5);

        System.out.println();
        System.out.println("--- 提交 15 天请假单 ---");
        groupLeader.processLeaveRequest(15);
    }
}

/**
 * 抽象审批处理节点
 */
abstract class Approver {

    protected final String name;
    protected Approver nextApprover;

    public Approver(String name) {
        this.name = name;
    }

    /**
     * 设置链条的下一个审批者，支持链式装配
     */
    public Approver setNext(Approver next) {
        this.nextApprover = next;
        return next;
    }

    /**
     * 核心审批流转方法
     */
    public abstract void processLeaveRequest(int days);
}

/**
 * 节点1：组长（权限：只能审批 <= 3 天的请假）
 */
class GroupLeader extends Approver {

    public GroupLeader(String name) {
        super(name);
    }

    @Override
    public void processLeaveRequest(int days) {
        if (days <= 3) {
            System.out.println("[组长审批通过] " + name + " 批准了 " + days + " 天请假。");
        } else if (nextApprover != null) {
            System.out.println("[组长权限不足] 超出 3 天权限，由 " + name + " 上报给上级领导...");
            nextApprover.processLeaveRequest(days);
        }
    }
}

/**
 * 节点2：部门经理（权限：只能审批 <= 7 天的请假）
 */
class DepartmentManager extends Approver {

    public DepartmentManager(String name) {
        super(name);
    }

    @Override
    public void processLeaveRequest(int days) {
        if (days <= 7) {
            System.out.println("[部门经理审批通过] " + name + " 批准了 " + days + " 天请假。");
        } else if (nextApprover != null) {
            System.out.println("[部门经理权限不足] 超出 7 天权限，由 " + name + " 上报给总经理...");
            nextApprover.processLeaveRequest(days);
        }
    }
}

/**
 * 节点3：总经理（权限：审批 > 7 天的请假）
 */
class GeneralManager extends Approver {

    public GeneralManager(String name) {
        super(name);
    }

    @Override
    public void processLeaveRequest(int days) {
        if (days <= 30) {
            System.out.println("[总经理终审通过] " + name + " 特别批准了 " + days + " 天超长假期！");
        } else {
            System.out.println("[总经理驳回] 请假天数超过 30 天，" + name + " 予以驳回！");
        }
    }
}
