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
 * 文件命名与问题编号严格按照考题顺序（Pattern01 ~ Pattern20）精确对齐：
 * 
 * 一、【必学核心模式（5种）】
 * 1. 单例模式 (Singleton Pattern)        -> creational.Pattern01_SingletonPatternDemo
 * 2. 工厂方法模式 (Factory Method)      -> creational.Pattern02_FactoryMethodPatternDemo
 * 3. 代理模式 (Proxy Pattern)            -> structural.Pattern03_ProxyPatternDemo
 * 4. 策略模式 (Strategy Pattern)         -> behavioral.Pattern04_StrategyPatternDemo
 * 5. 观察者模式 (Observer Pattern)       -> behavioral.Pattern05_ObserverPatternDemo
 * 
 * 二、【建议掌握模式（7种）】
 * 6. 建造者模式 (Builder Pattern)        -> creational.Pattern06_BuilderPatternDemo
 * 7. 模板方法模式 (Template Method)      -> behavioral.Pattern07_TemplateMethodPatternDemo
 * 8. 责任链模式 (Chain of Responsibility)-> behavioral.Pattern08_ChainOfResponsibilityPatternDemo
 * 9. 抽象工厂模式 (Abstract Factory)    -> creational.Pattern09_AbstractFactoryPatternDemo
 * 10. 适配器模式 (Adapter Pattern)       -> structural.Pattern10_AdapterPatternDemo
 * 11. 外观模式 (Facade Pattern)          -> structural.Pattern11_FacadePatternDemo
 * 12. 迭代器模式 (Iterator Pattern)      -> behavioral.Pattern12_IteratorPatternDemo
 * 
 * 三、【架构进阶模式（8种）】
 * 13. 桥接模式 (Bridge Pattern)          -> structural.Pattern13_BridgePatternDemo
 * 14. 组合模式 (Composite Pattern)       -> structural.Pattern14_CompositePatternDemo
 * 15. 装饰器模式 (Decorator Pattern)     -> structural.Pattern15_DecoratorPatternDemo
 * 16. 状态模式 (State Pattern)           -> behavioral.Pattern16_StatePatternDemo
 * 17. 访问者模式 (Visitor Pattern)       -> behavioral.Pattern17_VisitorPatternDemo
 * 18. 中介者模式 (Mediator Pattern)      -> behavioral.Pattern18_MediatorPatternDemo
 * 19. 命令模式 (Command Pattern)         -> behavioral.Pattern19_CommandPatternDemo
 * 20. 备忘录模式 (Memento Pattern)       -> behavioral.Pattern20_MementoPatternDemo
 * ============================================================================
 */
public class DesignPatternsMasterApplication {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("Java 经典设计模式 (Design Patterns) 体系化全景演示总入口 (顺序命名版)");
        System.out.println("==========================================================================");
        System.out.println("20 种设计模式文件名与问题编号严格对齐 (Pattern01 ~ Pattern20):");
        System.out.println("01. 单例模式:      creational/Pattern01_SingletonPatternDemo.java");
        System.out.println("02. 工厂方法模式:  creational/Pattern02_FactoryMethodPatternDemo.java");
        System.out.println("03. 代理模式:      structural/Pattern03_ProxyPatternDemo.java");
        System.out.println("04. 策略模式:      behavioral/Pattern04_StrategyPatternDemo.java");
        System.out.println("05. 观察者模式:    behavioral/Pattern05_ObserverPatternDemo.java");
        System.out.println("06. 建造者模式:    creational/Pattern06_BuilderPatternDemo.java");
        System.out.println("07. 模板方法模式:  behavioral/Pattern07_TemplateMethodPatternDemo.java");
        System.out.println("08. 责任链模式:    behavioral/Pattern08_ChainOfResponsibilityPatternDemo.java");
        System.out.println("09. 抽象工厂模式:  creational/Pattern09_AbstractFactoryPatternDemo.java");
        System.out.println("10. 适配器模式:    structural/Pattern10_AdapterPatternDemo.java");
        System.out.println("11. 外观模式:      structural/Pattern11_FacadePatternDemo.java");
        System.out.println("12. 迭代器模式:    behavioral/Pattern12_IteratorPatternDemo.java");
        System.out.println("13. 桥接模式:      structural/Pattern13_BridgePatternDemo.java");
        System.out.println("14. 组合模式:      structural/Pattern14_CompositePatternDemo.java");
        System.out.println("15. 装饰器模式:    structural/Pattern15_DecoratorPatternDemo.java");
        System.out.println("16. 状态模式:      behavioral/Pattern16_StatePatternDemo.java");
        System.out.println("17. 访问者模式:    behavioral/Pattern17_VisitorPatternDemo.java");
        System.out.println("18. 中介者模式:    behavioral/Pattern18_MediatorPatternDemo.java");
        System.out.println("19. 命令模式:      behavioral/Pattern19_CommandPatternDemo.java");
        System.out.println("20. 备忘录模式:    behavioral/Pattern20_MementoPatternDemo.java");
        System.out.println("==========================================================================");
        System.out.println("请进入各具体模式类中独立运行其 main() 方法查看全链路交互执行日志。");
    }
}
