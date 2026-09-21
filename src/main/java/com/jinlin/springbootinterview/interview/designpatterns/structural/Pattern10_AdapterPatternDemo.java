package com.jinlin.springbootinterview.interview.designpatterns.structural;

/**
 * ============================================================================
 * 10. 适配器模式 (Adapter Pattern)【建议学，接口兼容利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将一个类的接口转换成客户希望的另外一个接口。
 *    适配器模式使得原本由于接口不兼容而不能协同工作的那些类可以一起工作。
 * 
 * 2. 现实生活的生动比喻：
 *    - 手机充电口是 Type-C，但充电线只有传统的 Micro-USB。
 *    - 买一个 Type-C 转接头（适配器），插在旧线上，手机就能正常充电了！
 * 
 * 3. 类适配器（基于继承）vs 对象适配器（基于组合，强烈推荐）：
 *    - 对象适配器遵循“组合优于继承”原则，在适配器内部持有被适配对象（Adaptee）的引用，
 *      灵活性极高，能够适配该类的所有子类对象。
 * 
 * 4. 经典工业级应用：
 *    - Spring MVC 的 HandlerAdapter（将各种不同的 Controller 接口适配为 DispatcherServlet 能调用的标准接口）。
 *    - MyBatis 的 Log 日志适配器（将第三方 Slf4j/Log4j 统一适配为 MyBatis 的 Log 接口）。
 * ============================================================================
 */
public class Pattern10_AdapterPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("适配器模式：使用 Type-C 适配器为旧充电线完成接口转接");
        System.out.println("=================================================");

        // 现有一根旧的 Micro-USB 充电线
        MicroUsbCable oldCable = new MicroUsbCable();

        // 手机只认 TypeC 接口：接入对象适配器
        TypeCTarget adapter = new TypeCAdapter(oldCable);

        // 手机通过标准 Type-C 接口成功充上电
        adapter.chargeWithTypeC();
    }
}

/**
 * 目标接口：客户端期望的标准接口（Target）
 */
interface TypeCTarget {
    void chargeWithTypeC();
}

/**
 * 被适配者：现有的不兼容旧类（Adaptee）
 */
class MicroUsbCable {
    public void chargeWithMicroUsb() {
        System.out.println("[旧版数据线] 正在使用 Micro-USB 梯形插头进行供电...");
    }
}

/**
 * 对象适配器（Adapter）：实现目标接口，并持有被适配对象的引用
 */
class TypeCAdapter implements TypeCTarget {

    private final MicroUsbCable adaptee;

    public TypeCAdapter(MicroUsbCable adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void chargeWithTypeC() {
        System.out.println("[Type-C 转换接头] 接入 Micro-USB 输入端口，动态转接为 Type-C 协议输出...");
        // 委托给底层被适配者真实执行
        adaptee.chargeWithMicroUsb();
        System.out.println("[Type-C 转换接头] 成功输出 5V/3A 快充电流！");
    }
}
