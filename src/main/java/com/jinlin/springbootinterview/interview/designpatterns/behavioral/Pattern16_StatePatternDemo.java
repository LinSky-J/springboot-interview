package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

/**
 * ============================================================================
 * 16. 状态模式 (State Pattern)【消除复杂状态条件分支与状态机利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    允许一个对象在其内部状态改变时改变它的行为。对象看起来似乎修改了它的类。
 * 
 * 2. 核心痛点与消除 if-else：
 *    - 痛点：业务中经常遇到复杂的生命周期状态流转（如订单：待支付 -> 已支付 -> 已发货 -> 已完成 / 已取消）。
 *      传统做法：在每个操作方法中充斥着庞大的 switch-case 或 if (status == ...) 判断，
 *      一旦增加或修改状态，所有方法都要修改，极难维护且易漏改引发 Bug。
 *    - 解决：将每个状态的具体行为封装到独立的状态类中，环境类（Context）只负责委派当前状态去处理，
 *      状态流转在各具体状态内部完成（或统一由状态机驱动），各状态职责高度内聚。
 * 
 * 3. 状态模式 vs 策略模式：
 *    - 策略模式：强调“同一种目的的算法替换”（如支付宝、微信支付都是支付算法），客户端主动决定使用哪种策略，策略间通常是平级的，无状态流转。
 *    - 状态模式：强调“对象生命周期与行为随状态驱动改变”，状态之间存在前后继流转关系，状态的切换通常是内部自动发生的，客户端只负责触发动作。
 * 
 * 4. 经典框架应用：
 *    - Spring StateMachine（Spring 官方状态机组件）
 *    - 工作流引擎（Flowable、Camunda、Activiti）
 *    - TCP 协议连接管理（CLOSED, LISTEN, SYN_SENT, ESTABLISHED）
 * ============================================================================
 */
public class Pattern16_StatePatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("状态模式：电商订单完整生命周期状态流转");
        System.out.println("=================================================");

        // 1. 初始化新创建的订单（初始状态：待支付）
        OrderContext order = new OrderContext();

        // 2. 正常履约流程推进
        System.out.println("\n--- 步骤 1: 尝试发货（非正常流程测试） ---");
        order.shipOrder(); // 待支付状态下不能直接发货

        System.out.println("\n--- 步骤 2: 用户付款 ---");
        order.payOrder(); // 待支付 -> 已支付

        System.out.println("\n--- 步骤 3: 仓库发货 ---");
        order.shipOrder(); // 已支付 -> 已发货

        System.out.println("\n--- 步骤 4: 确认收货 ---");
        order.receiveOrder(); // 已发货 -> 已完成

        System.out.println("\n--- 步骤 5: 订单完成后尝试取消 ---");
        order.cancelOrder(); // 已完成状态无法取消
    }
}

/**
 * 抽象状态角色 (State)：定义订单在各个状态下支持的动作
 */
interface OrderState {
    void pay(OrderContext context);
    void ship(OrderContext context);
    void receive(OrderContext context);
    void cancel(OrderContext context);
    String getStateName();
}

/**
 * 环境角色 (Context)：维护当前状态，并提供对外业务接口
 */
class OrderContext {

    // 持有当前状态引用
    private OrderState currentState;

    public OrderContext() {
        // 初始状态为待支付状态
        this.currentState = new PendingPaymentState();
        System.out.println("[订单初始化] 订单创建成功，当前状态: " + currentState.getStateName());
    }

    public void setState(OrderState state) {
        this.currentState = state;
        System.out.println("[状态流转] 订单状态变更为: " + state.getStateName());
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    // 对外暴露的业务操作，全部委托给当前状态实例执行
    public void payOrder() {
        currentState.pay(this);
    }

    public void shipOrder() {
        currentState.ship(this);
    }

    public void receiveOrder() {
        currentState.receive(this);
    }

    public void cancelOrder() {
        currentState.cancel(this);
    }
}

/**
 * 具体状态1：待支付状态 (PendingPaymentState)
 */
class PendingPaymentState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        System.out.println("[支付成功] 订单款项已到账。");
        // 状态流转到：已支付
        context.setState(new PaidState());
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("[操作被拦截] 订单尚未付款，禁止发货！");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("[操作被拦截] 订单尚未付款，无法收货！");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("[取消成功] 未付款订单已取消关闭。");
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "待支付 (PENDING_PAYMENT)";
    }
}

/**
 * 具体状态2：已支付状态 (PaidState)
 */
class PaidState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        System.out.println("[重复操作] 订单已经支付成功，无需重复付款！");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("[出库发货] 仓库已打包完毕，顺丰快递已揽件发出。");
        // 状态流转到：已发货
        context.setState(new ShippedState());
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("[操作被拦截] 商品尚未发货，无法确认收货！");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("[发起退款] 订单全额原路退回，订单关闭。");
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "已支付 (PAID)";
    }
}

/**
 * 具体状态3：已发货状态 (ShippedState)
 */
class ShippedState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        System.out.println("[重复操作] 订单已在运输中，请勿重复支付！");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("[重复操作] 商品已发货，请在物流详情查看轨迹。");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("[确认签收] 买家已验货签收，交易达成。");
        // 状态流转到：已完成
        context.setState(new FinishedState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("[拦截退货] 商品已在途，如需取消请拒收或申请售后退货流程。");
    }

    @Override
    public String getStateName() {
        return "已发货 (SHIPPED)";
    }
}

/**
 * 具体状态4：已完成状态 (FinishedState)
 */
class FinishedState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        System.out.println("[操作无效] 订单已完成。");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("[操作无效] 订单已完成。");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("[重复操作] 订单已确认签收，感谢惠顾！");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("[操作被拦截] 订单已履约完结，不能直接取消。");
    }

    @Override
    public String getStateName() {
        return "已完成 (FINISHED)";
    }
}

/**
 * 具体状态5：已取消状态 (CancelledState)
 */
class CancelledState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        System.out.println("[操作被拦截] 订单已取消关闭，无法支付！");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("[操作被拦截] 订单已取消，禁止发货！");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("[操作被拦截] 订单已失效。");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("[重复操作] 订单已处于取消状态。");
    }

    @Override
    public String getStateName() {
        return "已取消 (CANCELLED)";
    }
}
