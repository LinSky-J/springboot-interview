package com.jinlin.springbootinterview.interview.spring.circular.bean;

/**
 * 循环依赖模型中的 ClassA
 * 
 * 教学解析：
 * 依赖 ClassB，通过 Setter 注入，参与 Spring 三级缓存循环依赖闭环。
 */
public class ClassA {

    private ClassB classB;

    public ClassB getClassB() {
        return classB;
    }

    public void setClassB(ClassB classB) {
        this.classB = classB;
    }
}
