package com.jinlin.springbootinterview.interview.spring.circular.bean;

/**
 * 循环依赖模型中的 ClassB
 * 
 * 教学解析：
 * 依赖 ClassA，反向形成 A -> B -> A 循环依赖。
 */
public class ClassB {

    private ClassA classA;

    public ClassA getClassA() {
        return classA;
    }

    public void setClassA(ClassA classA) {
        this.classA = classA;
    }
}
