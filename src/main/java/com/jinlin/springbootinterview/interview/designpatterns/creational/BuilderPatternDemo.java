package com.jinlin.springbootinterview.interview.designpatterns.creational;

/**
 * ============================================================================
 * 6. 建造者模式 (Builder Pattern)【建议学，链式构建复杂对象利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将一个复杂对象的构建与其表示分离，使得同样的构建过程可以创建不同的表示。
 * 
 * 2. 解决的核心工程痛点：
 *    - 当一个类的成员属性非常多时（如超过 5-10 个属性），如果使用多参构造函数，
 *      会出现“重叠构造函数（Telescoping Constructor）”噩梦：参数顺序极易传错，代码极其难读。
 *    - 如果使用普通的 setXxx() 方法，对象可能在 set 的中间状态被暴露，导致状态不一致，且无法保证对象不可变性。
 *    - 建造者模式通过静态内部类 Builder 提供流式链式调用（Fluent API），清晰优雅，一步到位 build() 出不可变对象！
 * 
 * 3. 经典工业级应用：
 *    - Lombok 的 @Builder 注解。
 *    - StringBuilder / StringBuffer。
 *    - Spring 中的 SpringApplicationBuilder、BeanDefinitionBuilder。
 *    - MyBatis 中的 SqlSessionFactoryBuilder。
 * ============================================================================
 */
public class BuilderPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("建造者模式：链式流式配置组装复杂 Computer 对象");
        System.out.println("=================================================");

        // 使用 Builder 链式构造对象，清晰直观，杜绝多参构造器传错参数
        Computer computer = new Computer.Builder("Intel i9-14900K", "64GB DDR5")
                .gpu("NVIDIA RTX 4090")
                .ssd("2TB NVMe M.2")
                .powerSupply("1000W 80PLUS Platinum")
                .waterCooling(true)
                .build();

        System.out.println("构建完成的计算机配置: " + computer);
    }
}

/**
 * 复杂产品类（属性较多）
 */
class Computer {

    // 必选配置
    private final String cpu;
    private final String ram;

    // 可选配置
    private final String gpu;
    private final String ssd;
    private final String powerSupply;
    private final boolean waterCooling;

    // 私有构造器，只能通过 Builder 进行实例化
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.gpu = builder.gpu;
        this.ssd = builder.ssd;
        this.powerSupply = builder.powerSupply;
        this.waterCooling = builder.waterCooling;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", gpu='" + gpu + '\'' +
                ", ssd='" + ssd + '\'' +
                ", powerSupply='" + powerSupply + '\'' +
                ", waterCooling=" + waterCooling +
                '}';
    }

    /**
     * 静态内部建造者类
     */
    public static class Builder {
        private final String cpu;
        private final String ram;
        private String gpu = "集成显卡";
        private String ssd = "512GB";
        private String powerSupply = "500W";
        private boolean waterCooling = false;

        // 构造器传入必选参数
        public Builder(String cpu, String ram) {
            this.cpu = cpu;
            this.ram = ram;
        }

        // 链式设置可选参数，返回 this
        public Builder gpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder ssd(String ssd) {
            this.ssd = ssd;
            return this;
        }

        public Builder powerSupply(String powerSupply) {
            this.powerSupply = powerSupply;
            return this;
        }

        public Builder waterCooling(boolean waterCooling) {
            this.waterCooling = waterCooling;
            return this;
        }

        // 最终校验并生成不可变的外部类对象
        public Computer build() {
            return new Computer(this);
        }
    }
}
