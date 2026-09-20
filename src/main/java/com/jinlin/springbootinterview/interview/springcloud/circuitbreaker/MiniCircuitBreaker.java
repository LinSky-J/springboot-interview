package com.jinlin.springbootinterview.interview.springcloud.circuitbreaker;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 生产级概念轻量级熔断器实现 (MiniCircuitBreaker)
 * 完整模拟 CLOSED -> OPEN -> HALF_OPEN -> CLOSED 状态机流转与降级触发
 */
public class MiniCircuitBreaker {

    private CircuitState state = CircuitState.CLOSED;
    private final int failureThreshold; // 连续失败阈值
    private final long sleepWindowMs;   // 熔断后冷却窗口时长（毫秒）

    private final AtomicInteger continuousFailureCount = new AtomicInteger(0);
    private long lastOpenTimestamp = 0;

    public MiniCircuitBreaker(int failureThreshold, long sleepWindowMs) {
        this.failureThreshold = failureThreshold;
        this.sleepWindowMs = sleepWindowMs;
    }

    /**
     * 核心熔断执行包装方法（结合函数式接口与降级 Fallback）
     */
    public synchronized String execute(Supplier<String> normalAction, Function<Throwable, String> fallback) {
        long now = System.currentTimeMillis();

        // 1. 检查是否达到冷却时间，从而从 OPEN 跃迁为 HALF_OPEN
        if (state == CircuitState.OPEN) {
            if (now - lastOpenTimestamp >= sleepWindowMs) {
                state = CircuitState.HALF_OPEN;
                System.out.println("[熔断状态机变迁] 冷却时间已到，状态由 OPEN 跃迁为 -> HALF_OPEN (半开探测态)！");
            } else {
                // 仍在冷却窗口期内，直接拦截不发网络请求，走降级
                return fallback.apply(new RuntimeException("熔断器打开 (OPEN)，系统正在紧急冷却隔离保护中"));
            }
        }

        // 2. 执行真正的远程调用
        try {
            String result = normalAction.get();
            // 调用成功：处理成功后状态转移
            onSuccess();
            return result;
        } catch (Throwable ex) {
            // 调用失败：处理失败并可能触发拉闸
            onFailure(ex);
            // 触发降级保底
            return fallback.apply(ex);
        }
    }

    private void onSuccess() {
        if (state == CircuitState.HALF_OPEN) {
            System.out.println("[熔断状态机变迁] 半开探针请求成功！下游已自愈，状态由 HALF_OPEN 恢复为 -> CLOSED (闭合正常)！");
            state = CircuitState.CLOSED;
            continuousFailureCount.set(0);
        } else if (state == CircuitState.CLOSED) {
            continuousFailureCount.set(0);
        }
    }

    private void onFailure(Throwable ex) {
        int failures = continuousFailureCount.incrementAndGet();
        System.out.println("[异常上报] 远程服务调用失败: " + ex.getMessage() + " (连续失败次数: " + failures + ")");

        if (state == CircuitState.HALF_OPEN) {
            // 半开态只要探测失败，立即重新打回 OPEN 状态
            state = CircuitState.OPEN;
            lastOpenTimestamp = System.currentTimeMillis();
            System.out.println("[熔断状态机变迁] 半开探针请求再次失败，下游尚未恢复，状态重新打回 -> OPEN (熔断开启)！");
        } else if (state == CircuitState.CLOSED && failures >= failureThreshold) {
            // 闭合态达到连续失败阈值，立即跳闸拉闸
            state = CircuitState.OPEN;
            lastOpenTimestamp = System.currentTimeMillis();
            System.out.println("[熔断状态机变迁] 连续失败达到阈值 " + failureThreshold + " 次！紧急拉闸，状态跃迁为 -> OPEN (熔断开启)！");
        }
    }

    public CircuitState getState() {
        return state;
    }
}
