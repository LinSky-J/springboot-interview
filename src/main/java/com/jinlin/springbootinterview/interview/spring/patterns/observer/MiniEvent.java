package com.jinlin.springbootinterview.interview.spring.patterns.observer;

/**
 * 观察者模式演示：事件实体
 * 
 * 教学解析：
 * 对应 Spring 中的 org.springframework.context.ApplicationEvent。
 * 充当事件发布与监听交互过程中的数据载体。
 */
public class MiniEvent {

    private final Object source;

    public MiniEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
