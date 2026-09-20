package com.jinlin.springbootinterview.interview.designpatterns;

import com.jinlin.springbootinterview.interview.designpatterns.behavioral.*;
import com.jinlin.springbootinterview.interview.designpatterns.creational.*;
import com.jinlin.springbootinterview.interview.designpatterns.structural.*;

/**
 * ============================================================================
 * 23种经典设计模式（GoF）全景分类总览与交互式引导
 * ============================================================================
 * 
 * 本项目为全套设计模式实战精讲，涵盖高频面试与架构设计的20种核心模式（另含解释器、原型等），
 * 并按【必学】、【建议学】、【拓展模式】三大梯度科学分级，各模式独立封装于子包中：
 * 
 * 一、【必学核心模式（5种）】
 * 1. 单例模式 (Singleton Pattern)        -> creational.SingletonPatternDemo
 * 2. 工厂方法模式 (Factory Method)      -> creational.FactoryMethodPatternDemo
 * 3. 代理模式 (Proxy Pattern)            -> structural.ProxyPatternDemo
 * 4. 策略模式 (Strategy Pattern)         -> behavioral.StrategyPatternDemo
 * 5. 观察者模式 (Observer Pattern)       -> behavioral.ObserverPatternDemo
 * 
 * 二、【建议掌握模式（7种）】
 * 6. 建造者模式 (Builder Pattern)        -> creational.BuilderPatternDemo
 * 7. 模板方法模式 (Template Method)      -> behavioral.TemplateMethodPatternDemo
 * 8. 责任链模式 (Chain of Responsibility)-> behavioral.ChainOfResponsibilityPatternDemo
 * 9. 抽象工厂模式 (Abstract Factory)    -> creational.AbstractFactoryPatternDemo
 * 10. 适配器模式 (Adapter Pattern)       -> structural.AdapterPatternDemo
 * 11. 外观模式 (Facade Pattern)          -> structural.FacadePatternDemo
 * 12. 迭代器模式 (Iterator Pattern)      -> behavioral.IteratorPatternDemo
 * 
 * 三、【架构进阶模式（8种）】
 * 13. 桥接模式 (Bridge Pattern)          -> structural.BridgePatternDemo
 * 14. 组合模式 (Composite Pattern)       -> structural.CompositePatternDemo
 * 15. 装饰器模式 (Decorator Pattern)     -> structural.DecoratorPatternDemo
 * 16. 状态模式 (State Pattern)           -> behavioral.StatePatternDemo
 * 17. 访问者模式 (Visitor Pattern)       -> behavioral.VisitorPatternDemo
 * 18. 中介者模式 (Mediator Pattern)      -> behavioral.MediatorPatternDemo
 * 19. 命令模式 (Command Pattern)         -> behavioral.CommandPatternDemo
 * 20. 备忘录模式 (Memento Pattern)       -> behavioral.MementoPatternDemo
 * ============================================================================
 */
public class DesignPatternsMasterApplication {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("Java 经典设计模式 (Design Patterns) 体系化全景演示总入口");
        System.out.println("==========================================================================");
        System.out.println("分支名称: design-patterns");
        System.out.println("包含模块:");
        System.out.println("1. 创建型 (Creational): 单例、工厂方法、抽象工厂、建造者");
        System.out.println("2. 结构型 (Structural): 代理、适配器、外观、桥接、组合、装饰器");
        System.out.println("3. 行为型 (Behavioral): 策略、观察者、模板方法、责任链、迭代器、状态、访问者、中介者、命令、备忘录");
        System.out.println("==========================================================================");
        System.out.println("请进入各具体模式 Demo 类中独立运行其 main() 方法查看全链路交互执行日志。");
    }
}
