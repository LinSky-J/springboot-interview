package com.jinlin.springbootinterview.interview.spring.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式演示：事件广播调度中心
 * 
 * 教学解析：
 * 对应 Spring 中的 org.springframework.context.event.ApplicationEventMulticaster。
 * 维护所有的观察者（Listener）列表，负责在 publishEvent 时循环通知各个监听器。
 */
public class MiniEventMulticaster {

    private final List<MiniEventListener> listeners = new ArrayList<>();

    public void addListener(MiniEventListener listener) {
        listeners.add(listener);
    }

    public void publishEvent(MiniEvent event) {
        System.out.println("[事件中心] 正在广播系统事件: " + event.getSource());
        for (MiniEventListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }
}
