package com.jinlin.springbootinterview.interview.designpatterns.creational;

/**
 * ============================================================================
 * 1. 单例模式 (Singleton Pattern)【必学，高频必考题】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    保证一个类在整个 JVM 进程中仅有一个实例，并提供一个全局唯一的访问点。
 * 
 * 2. 饿汉式 vs 懒汉式的本质区别与选型：
 *    +------------------+------------------------------------+------------------------------------+
 *    | 比较维度         | 饿汉式 (Eager Initialization)      | 懒汉式 (Lazy Initialization)       |
 *    +------------------+------------------------------------+------------------------------------+
 *    | 实例化时机       | 类加载 (Class Loading) 阶段立即初始化| 首次主动调用 getInstance() 时初始化|
 *    | 延迟加载 (Lazy)  | 不支持（急切加载，无论用不用都占内存）| 支持（按需加载，节省系统初始内存） |
 *    | 线程安全性       | 天生线程安全（利用 JVM 类加载机制） | 需手动保证（推荐 DCL + volatile）  |
 *    | 性能开销         | getInstance() 无锁竞争，执行极快   | DCL 仅在初次并发创建时需要锁竞争   |
 *    | 适用场景         | 单例对象占用内存小、启动后必定使用的组件| 单例对象占用内存大、不确定何时使用的组件|
 *    +------------------+------------------------------------+------------------------------------+
 * 
 * 3. 单例实现的五种经典演进形式：
 *    (1) 静态常量饿汉式：最简单直观，类加载即初始化，线程安全。
 *    (2) 静态代码块饿汉式：适合在初始化前需要执行复杂参数计算或读取配置文件的场景。
 *    (3) 基础简单懒汉式：有严重线程安全隐患（多线程并发时导致单例被多次创建）。
 *    (4) 双重检查锁定 (DCL) 懒汉式：兼具延迟加载与高性能并发读，必须配合 volatile 禁止指令重排。
 *    (5) 静态内部类 (IoDH)：利用 JVM 类加载特性，兼具饿汉式的无锁安全与懒汉式的延迟加载。
 *    (6) 枚举单例：Effective Java 作者推荐，天然防反射与反序列化破坏的最佳单例。
 * 
 * 4. 面试核心深水区：DCL 为什么要加 volatile？
 *    - Java 创建对象分为三步字节码指令：
 *      a. allocate 分配堆内存空间；
 *      b. ctorInstance 调用构造器初始化对象；
 *      c. 将 instance 引用指向已分配的堆内存地址。
 *    - 缺少 volatile 时，JVM/CPU 可能会发生【指令重排序】变为 a -> c -> b；
 *    - 线程 A 执行完 c 尚未执行 b 时，线程 B 判断 instance != null 直接拿到“半成品未初始化对象”，
 *      访问成员属性时引发空指针或业务数据混乱！
 *    - volatile 通过插入底层内存屏障，强行【禁止指令重排序】，保证安全发布。
 * ============================================================================
 */
public class Pattern01_SingletonPatternDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("单例模式全景演进：饿汉式 (两种) 与 懒汉式 (DCL/静态内部类) 及枚举对比");
        System.out.println("==========================================================================");

        // 1. 验证饿汉式单例（静态常量方式）
        System.out.println("\n--- 1. 验证饿汉式单例 (静态常量方式) ---");
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        System.out.println("饿汉式静态常量两次获取是否相同: " + (eager1 == eager2));
        eager1.printMessage();

        // 2. 验证饿汉式单例（静态代码块方式）
        System.out.println("\n--- 2. 验证饿汉式单例 (静态代码块方式) ---");
        StaticBlockEagerSingleton blockEager1 = StaticBlockEagerSingleton.getInstance();
        StaticBlockEagerSingleton blockEager2 = StaticBlockEagerSingleton.getInstance();
        System.out.println("饿汉式静态代码块两次获取是否相同: " + (blockEager1 == blockEager2));

        // 3. 验证懒汉式单例（双重检查锁定 DCL + volatile）
        System.out.println("\n--- 3. 验证懒汉式单例 (DCL + volatile) ---");
        DclSingleton dcl1 = DclSingleton.getInstance();
        DclSingleton dcl2 = DclSingleton.getInstance();
        System.out.println("DCL 懒汉式两次获取是否相同: " + (dcl1 == dcl2));

        // 4. 验证静态内部类单例 (IoDH)
        System.out.println("\n--- 4. 验证静态内部类单例 (兼具饿汉安全与懒汉延迟) ---");
        StaticInnerSingleton inner1 = StaticInnerSingleton.getInstance();
        StaticInnerSingleton inner2 = StaticInnerSingleton.getInstance();
        System.out.println("静态内部类两次获取是否相同: " + (inner1 == inner2));

        // 5. 验证枚举单例 (防反射与反序列化)
        System.out.println("\n--- 5. 验证枚举单例 (Effective Java 推荐最佳实现) ---");
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        System.out.println("枚举单例两次获取是否相同: " + (enum1 == enum2));
        enum1.doBusiness();
    }
}

/**
 * ============================================================================
 * 一、饿汉式单例（方式一：静态常量）
 * 特点：类加载阶段由 JVM 主动完成实例化，天然线程安全。
 * ============================================================================
 */
class EagerSingleton {

    // 1. 类加载时即直接初始化创建唯一单例对象
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    // 2. 私有化构造方法，禁止外部直接 new
    private EagerSingleton() {
        System.out.println("[饿汉式] 实例已被 JVM 类加载机制安全初始化完成。");
    }

    // 3. 全局唯一的公有静态访问入口
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }

    public void printMessage() {
        System.out.println("[饿汉式静态常量] 业务逻辑执行中...");
    }
}

/**
 * ============================================================================
 * 一、饿汉式单例（方式二：静态代码块）
 * 适合场景：单例对象创建前需要复杂的环境准备、加载本地文件或多步计算。
 * ============================================================================
 */
class StaticBlockEagerSingleton {

    private static final StaticBlockEagerSingleton INSTANCE;

    // 在静态代码块中完成实例化与依赖配置
    static {
        try {
            INSTANCE = new StaticBlockEagerSingleton();
        } catch (Exception e) {
            throw new RuntimeException("静态初始化单例异常", e);
        }
    }

    private StaticBlockEagerSingleton() {}

    public static StaticBlockEagerSingleton getInstance() {
        return INSTANCE;
    }
}

/**
 * ============================================================================
 * 二、懒汉式单例（方式一：双重检查锁定 DCL + volatile）
 * 特点：首次调用 getInstance() 时延迟加载，配合 volatile 防止指令重排半成品对象。
 * ============================================================================
 */
class DclSingleton {

    // 关键点1：必须加 volatile 禁止指令重排序
    private static volatile DclSingleton instance;

    // 关键点2：私有化构造函数，防止外部 new，并防御反射破坏
    private DclSingleton() {
        if (instance != null) {
            throw new RuntimeException("单例实例已存在，严禁通过反射重复创建！");
        }
    }

    public static DclSingleton getInstance() {
        // 第一层检查：过滤掉绝大多数已初始化后的读请求，无需进入 synchronized 锁竞争
        if (instance == null) {
            synchronized (DclSingleton.class) {
                // 第二层检查：防止多个线程同时跨过第一层检查后重复创建实例
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}

/**
 * ============================================================================
 * 二、懒汉式单例（方式二：静态内部类 IoDH）
 * 特点：利用 JVM 类加载特性，外部类加载时并不会加载内部类，仅在 getInstance 时触发。
 * ============================================================================
 */
class StaticInnerSingleton {

    private StaticInnerSingleton() {}

    // 静态内部类只有在被调用时才会由类加载器加载并初始化内部静态常量，实现线程安全 + 延迟加载
    private static class Holder {
        private static final StaticInnerSingleton INSTANCE = new StaticInnerSingleton();
    }

    public static StaticInnerSingleton getInstance() {
        return Holder.INSTANCE;
    }
}

/**
 * ============================================================================
 * 三、枚举单例（Effective Java 推荐最佳实现）
 * 特点：天然防反射（Constructor.newInstance 硬编码拦截）与防反序列化多次创建。
 * ============================================================================
 */
enum EnumSingleton {
    INSTANCE;

    public void doBusiness() {
        System.out.println("[枚举单例] 执行核心业务逻辑，天然防反射与反序列化破坏！");
    }
}
