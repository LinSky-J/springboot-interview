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
 * 2. 为什么需要单例？
 *    - 避免重量级对象重复创建与销毁（如数据库连接池、Spring IoC 容器、线程池、缓存配置）。
 *    - 节省堆内存空间，减少垃圾回收（GC）频率，保证全局状态的一致性。
 * 
 * 3. 面试高频考点：双重检查锁定 (DCL) 为什么要加 volatile？
 *    - 原因：Java 对象创建不是原子操作，底层分为三步指令：
 *      (1) memory = allocate(); // 分配对象内存空间
 *      (2) ctorInstance(memory); // 初始化对象（调用构造方法）
 *      (3) instance = memory;    // 将 instance 引用指向刚分配的内存地址
 *    - 在没有 volatile 时，JVM 和 CPU 可能会发生【指令重排序】，导致顺序变为 (1) -> (3) -> (2)。
 *    - 此时若线程 A 执行到了 (3)，instance 已经非 null，但对象还没初始化完成（半成品）；
 *      线程 B 此时执行第一层 if (instance == null)，发现不为 null 直接返回了半成品对象，
 *      后续线程 B 使用该对象访问成员属性时就会发生空指针或数据异常！
 *    - volatile 的核心作用：通过插入内存屏障，【禁止指令重排序】，保证安全发布！
 * 
 * 4. 防止反射与反序列化破坏单例：
 *    - 防止反射破坏：在私有构造方法中增加判断，若 instance != null 则直接抛出运行时异常。
 *    - 防止反序列化破坏：在类中声明 readResolve() 方法，返回已有的单例实例。
 *    - 最完美单例：枚举单例（天然防反射破坏，JVM 底层在 reflect.Constructor.newInstance 中硬编码禁止反射创建枚举）。
 * ============================================================================
 */
public class SingletonPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("单例模式：验证 DCL 懒汉式与枚举单例的唯一性");
        System.out.println("=================================================");

        DclSingleton s1 = DclSingleton.getInstance();
        DclSingleton s2 = DclSingleton.getInstance();
        System.out.println("DCL单例两次获取是否相同: " + (s1 == s2));

        EnumSingleton e1 = EnumSingleton.INSTANCE;
        EnumSingleton e2 = EnumSingleton.INSTANCE;
        System.out.println("枚举单例两次获取是否相同: " + (e1 == e2));
        e1.doBusiness();
    }
}

/**
 * 经典双重检查锁定 (Double-Checked Locking, DCL) 懒汉式单例
 */
class DclSingleton {

    // 关键点1：必须加 volatile 禁止指令重排序
    private static volatile DclSingleton instance;

    // 关键点2：私有化构造函数，防止外部 new
    private DclSingleton() {
        // 防反射破坏防御代码
        if (instance != null) {
            throw new RuntimeException("单例实例已存在，严禁通过反射重复创建！");
        }
    }

    public static DclSingleton getInstance() {
        // 第一层检查：避免每次调用都进入 synchronized 锁竞争，大幅提升读性能
        if (instance == null) {
            synchronized (DclSingleton.class) {
                // 第二层检查：防止多线程同时通过第一层检查后重复创建实例
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}

/**
 * 静态内部类单例（推荐：延迟加载且线程安全）
 */
class StaticInnerSingleton {
    private StaticInnerSingleton() {}

    // 利用 JVM 类加载机制保证线程安全，只有在调用 getInstance 时才会加载 Holder 类
    private static class Holder {
        private static final StaticInnerSingleton INSTANCE = new StaticInnerSingleton();
    }

    public static StaticInnerSingleton getInstance() {
        return Holder.INSTANCE;
    }
}

/**
 * 枚举单例（Effective Java 推荐最佳实现：防反射、防反序列化）
 */
enum EnumSingleton {
    INSTANCE;

    public void doBusiness() {
        System.out.println("[枚举单例] 执行核心业务逻辑，天然防反射与反序列化破坏！");
    }
}
