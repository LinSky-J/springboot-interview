package com.jinlin.springbootinterview.interview.springcloud.circuitbreaker;

/**
 * 熔断器状态机三态枚举
 */
public enum CircuitState {

    /**
     * 闭合正常状态：请求正常穿透发往下游，内部滑动窗口统计失败率与慢调用比例
     */
    CLOSED,

    /**
     * 开启熔断状态：熔断器拉闸切断通路，所有请求直接被阻断并触发 Fallback 降级，开启冷静期倒计时
     */
    OPEN,

    /**
     * 半开探测状态：冷静期结束后放行极少量探针请求探测下游健康度，成功则自愈为 CLOSED，失败重新打回 OPEN
     */
    HALF_OPEN
}
