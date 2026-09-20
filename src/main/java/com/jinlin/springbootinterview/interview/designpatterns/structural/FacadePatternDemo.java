package com.jinlin.springbootinterview.interview.designpatterns.structural;

/**
 * ============================================================================
 * 11. 外观模式 / 门面模式 (Facade Pattern)【建议学，封装复杂子系统利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    为子系统中一组复杂的接口提供一个一致的高层外观（Facade）接口。
 *    门面模式定义了一个高层入口，使子系统更加易于使用，降低客户端与子系统之间的耦合度。
 * 
 * 2. 现实生活的生动比喻：
 *    - 智能家居“一键离家模式”：
 *      没有门面时，你需要分别走去关灯、关空调、关电视、关窗帘、开启安防，极其繁琐；
 *      有了门面控制器，按下一个按钮，门面类自动协调各个子系统逐一关闭。
 * 
 * 3. 经典工业级应用：
 *    - SLF4J (Simple Logging Facade for Java)：著名的日志门面框架，
 *      为底层具体的 Logback、Log4j、JUL 提供了一致的高层调用接口。
 *    - Tomcat 的 RequestFacade / ResponseFacade：对内部复杂的 Request 对象进行包装，
 *      屏蔽 Tomcat 内部核心属性，只对外暴露标准 ServletRequest 方法。
 * ============================================================================
 */
public class FacadePatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("外观模式：智能家居一键开启与一键离家模式演示");
        System.out.println("=================================================");

        SmartHomeFacade smartHome = new SmartHomeFacade();

        // 客户端只需调用一个统一的高层门面方法
        smartHome.movieMode();

        System.out.println();

        smartHome.leaveHomeMode();
    }
}

/**
 * 子系统1：智能灯光
 */
class LightSubsystem {
    public void on() {
        System.out.println("[灯光系统] 客厅吸顶大灯开启");
    }
    public void dim() {
        System.out.println("[灯光系统] 灯光亮度调暗至 10%，切换为温馨暖光");
    }
    public void off() {
        System.out.println("[灯光系统] 全屋所有照明设备已关闭");
    }
}

/**
 * 子系统2：激光投影与音响
 */
class ProjectorSubsystem {
    public void on() {
        System.out.println("[影音系统] 投影仪开启，100英寸抗光巨幕自动降下，环绕立体声音响就绪");
    }
    public void off() {
        System.out.println("[影音系统] 投影仪关机，幕布自动收起归位");
    }
}

/**
 * 子系统3：中央空调
 */
class AirConditionerSubsystem {
    public void setComfortTemp() {
        System.out.println("[空调系统] 开启静音制冷模式，恒温设定为 25 摄氏度");
    }
    public void off() {
        System.out.println("[空调系统] 中央空调已关机");
    }
}

/**
 * 智能家居外观门面（Facade）：聚合各大复杂子系统
 */
class SmartHomeFacade {

    private final LightSubsystem light = new LightSubsystem();
    private final ProjectorSubsystem projector = new ProjectorSubsystem();
    private final AirConditionerSubsystem ac = new AirConditionerSubsystem();

    /**
     * 一键观影模式：统筹协调灯光、音响、空调
     */
    public void movieMode() {
        System.out.println(">>> 门面模式：启动【一键观影模式】 <<<");
        light.dim();
        projector.on();
        ac.setComfortTemp();
    }

    /**
     * 一键离家模式：快速安全切断电源
     */
    public void leaveHomeMode() {
        System.out.println(">>> 门面模式：启动【一键离家模式】 <<<");
        light.off();
        projector.off();
        ac.off();
    }
}
