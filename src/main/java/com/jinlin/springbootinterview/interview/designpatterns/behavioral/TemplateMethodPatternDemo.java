package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

/**
 * ============================================================================
 * 7. 模板方法模式 (Template Method Pattern)【建议学，骨架复用利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    定义一个算法的标准化固定骨架，而将具体的某些差异化步骤延迟到子类中去实现。
 *    模板方法使得子类可以在不改变算法整体结构的前提下，灵活重定义该算法的某些特定步骤。
 * 
 * 2. 关键方法分类：
 *    - 模板方法（Template Method）：定义固定流程骨架，通常使用 final 修饰，防止子类恶意重写改变流程。
 *    - 抽象方法（Abstract Method）：必须由具体子类根据业务实现的差异步骤。
 *    - 钩子方法（Hook Method）：父类提供默认空实现或布尔判断，子类可通过重写它来反向控制模板的执行分支（如 shouldSendSms()）。
 * 
 * 3. 经典工业级应用：
 *    - Spring 容器启动主骨架：AbstractApplicationContext 中的 refresh() 方法。
 *    - Spring 各种数据模板：JdbcTemplate、RestTemplate、TransactionTemplate。
 *    - Servlet 体系：HttpServlet.service() 根据 GET/POST 分发调用 doGet/doPost。
 * ============================================================================
 */
public class TemplateMethodPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("模板方法模式：网络下单标准流水线骨架演示");
        System.out.println("=================================================");

        System.out.println("--- 1. 实体商品订单流程 ---");
        OrderProcessTemplate physicalOrder = new PhysicalGoodsOrder();
        physicalOrder.processOrder();

        System.out.println();
        System.out.println("--- 2. 虚拟充值订单流程（钩子方法跳过物流发货） ---");
        OrderProcessTemplate virtualOrder = new VirtualGoodsOrder();
        virtualOrder.processOrder();
    }
}

/**
 * 抽象订单处理模板基类
 */
abstract class OrderProcessTemplate {

    /**
     * 核心模板方法：使用 final 锁定执行骨架流程，子类不可篡改执行顺序
     */
    public final void processOrder() {
        step1SelectItems();
        step2MakePayment();
        // 根据钩子方法返回值决定是否执行物流配送步骤
        if (isPhysicalGoodsHook()) {
            step3ShipDelivery();
        } else {
            System.out.println("[步骤 3: 虚拟发货] 无需物理物流配送，系统自动将充值直冲到账！");
        }
        step4SendReceipt();
        System.out.println("[订单完成] 交易闭环结束。");
    }

    private void step1SelectItems() {
        System.out.println("[步骤 1: 选购校验] 用户选择商品并锁定库存");
    }

    private void step2MakePayment() {
        System.out.println("[步骤 2: 支付结算] 扣减用户账户资金或通过微信/支付宝支付");
    }

    // 抽象方法：由具体商品子类实现的物流配送
    protected abstract void step3ShipDelivery();

    private void step4SendReceipt() {
        System.out.println("[步骤 4: 发送小票] 向用户手机推送电子发票与订单明细");
    }

    /**
     * 钩子方法 (Hook Method)：子类可重写，默认返回 true
     */
    protected boolean isPhysicalGoodsHook() {
        return true;
    }
}

/**
 * 具体子类1：实体物理商品（衣服、数码）
 */
class PhysicalGoodsOrder extends OrderProcessTemplate {
    @Override
    protected void step3ShipDelivery() {
        System.out.println("[步骤 3: 顺丰速递] 仓库打印面单打包，顺丰快递揽收发货");
    }
}

/**
 * 具体子类2：虚拟商品（游戏点卡、VIP充值）
 */
class VirtualGoodsOrder extends OrderProcessTemplate {
    @Override
    protected void step3ShipDelivery() {
        // 虚拟商品无需物理发货
    }

    @Override
    protected boolean isPhysicalGoodsHook() {
        // 重写钩子，通知模板跳过快递配送环节
        return false;
    }
}
