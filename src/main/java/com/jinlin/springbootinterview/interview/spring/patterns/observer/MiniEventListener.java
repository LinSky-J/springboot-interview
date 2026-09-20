package com.jinlin.springbootinterview.interview.spring.patterns.observer;

/**
 * 观察者模式演示：事件监听者标准接口
 * 
 * 教学解析：
 * 对应 Spring 中的 org.springframework.context.ApplicationListener。
 * 观察者订阅感兴趣的事件，当广播器发布事件时接收回调。
 */
@FunctionalInterface
public interface MiniEventListener {

    /**
     * 响应事件回调
     * @param event 广播的事件对象
     */
    void onApplicationEvent(MiniEvent event);
}
