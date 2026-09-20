package com.jinlin.springbootinterview.interview.designpatterns.structural;

/**
 * ============================================================================
 * 15. 装饰器模式 (Decorator Pattern)【动态扩展对象职责与包装利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。
 *    又称为包装器模式 (Wrapper)。
 * 
 * 2. 装饰器模式 vs 继承 vs 代理模式：
 *    - 为什么不用继承扩展？
 *      继承是静态编译期绑定的，当扩展维度较多时（例如：奶茶 + 珍珠 + 椰果 + 布丁），
 *      子类排列组合会导致严重的类爆炸（继承脆弱性）。
 *    - 装饰器模式的优势：
 *      装饰器和被装饰者实现同一个接口，且装饰器内部组合持有该接口引用。
 *      可以像“套娃”一样在运行时任意自由嵌套叠加（按需组合增强）。
 *    - 装饰器 vs 代理模式区别：
 *      * 装饰器模式注重：给对象动态叠加功能与增强职责（透明增强，层层嵌套）。
 *      * 代理模式注重：控制对对象的访问（如权限校验、事务切面、远程代理等，通常单层代理）。
 * 
 * 3. 经典工业级与源码应用：
 *    - Java 标准 I/O 流体系：
 *      InputStream（抽象组件）
 *      FileInputStream（具体构件）
 *      FilterInputStream（抽象装饰器）
 *      BufferedInputStream / DataInputStream / GZIPInputStream（具体装饰器，典型套娃式链式包装）
 *    - MyBatis 缓存体系：
 *      Cache 接口由 PerpetualCache（基础基础缓存）实现，
 *      并通过 LruCache、FifoCache、SerializedCache、LoggingCache、SynchronizedCache 进行层层装饰。
 * ============================================================================
 */
public class DecoratorPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("装饰器模式：咖啡制作（基础黑咖啡叠加牛奶、焦糖糖浆）");
        System.out.println("=================================================");

        // 1. 点一杯基础纯黑咖啡
        Beverage basicCoffee = new Espresso();
        System.out.println("订单品类: " + basicCoffee.getDescription() + "，金额: ￥" + basicCoffee.cost());

        // 2. 动态套娃装饰 1：加入牛奶 (MilkDecorator)
        Beverage milkCoffee = new MilkDecorator(basicCoffee);
        System.out.println("订单品类: " + milkCoffee.getDescription() + "，金额: ￥" + milkCoffee.cost());

        // 3. 动态套娃装饰 2：继续再加入双份浓缩糖浆 (MochaDecorator)
        Beverage caramelMilkCoffee = new MochaDecorator(milkCoffee);
        System.out.println("订单品类: " + caramelMilkCoffee.getDescription() + "，金额: ￥" + caramelMilkCoffee.cost());

        System.out.println();
        System.out.println("[总结] 装饰器可以在运行时由客户按需任意组合拼装，完美符合开闭原则(OCP)！");
    }
}

/**
 * 抽象组件 (Component)：定义饮料的核心规范
 */
interface Beverage {
    String getDescription();
    double cost();
}

/**
 * 具体构件 (ConcreteComponent)：基础实现对象（纯意式黑咖啡）
 */
class Espresso implements Beverage {

    @Override
    public String getDescription() {
        return "意式浓缩黑咖啡";
    }

    @Override
    public double cost() {
        return 18.0; // 基础底价 18 元
    }
}

/**
 * 抽象装饰器 (Decorator)：实现组件接口，并组合持有底层组件引用
 */
abstract class BeverageDecorator implements Beverage {

    // 组合持有的目标组件（可以是被装饰的原对象，也可以是已经被上一层装饰包装过的对象）
    protected final Beverage delegate;

    public BeverageDecorator(Beverage delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public double cost() {
        return delegate.cost();
    }
}

/**
 * 具体装饰器1 (ConcreteDecorator)：增加牛奶配料
 */
class MilkDecorator extends BeverageDecorator {

    public MilkDecorator(Beverage delegate) {
        super(delegate);
    }

    @Override
    public String getDescription() {
        // 扩展功能：追加描述
        return delegate.getDescription() + " + 新西兰鲜牛奶";
    }

    @Override
    public double cost() {
        // 扩展功能：加收牛奶费用 5 元
        return delegate.cost() + 5.0;
    }
}

/**
 * 具体装饰器2 (ConcreteDecorator)：增加摩卡糖浆配料
 */
class MochaDecorator extends BeverageDecorator {

    public MochaDecorator(Beverage delegate) {
        super(delegate);
    }

    @Override
    public String getDescription() {
        return delegate.getDescription() + " + 浓情摩卡糖浆";
    }

    @Override
    public double cost() {
        // 扩展功能：加收糖浆费用 4 元
        return delegate.cost() + 4.0;
    }
}
