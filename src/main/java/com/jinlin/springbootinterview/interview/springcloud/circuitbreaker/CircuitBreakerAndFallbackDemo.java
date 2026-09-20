package com.jinlin.springbootinterview.interview.springcloud.circuitbreaker;

/**
 * ============================================================================
 * 三、服务熔断 (Circuit Breaker) 与 服务降级 (Fallback) 深度剖析与状态机实战
 * ============================================================================
 * 
 * [面试核心高频问题]
 * 1. 什么是微服务雪崩效应 (Cascading Failures)？
 *    - 在微服务链路中（A -> B -> C -> D），若底层基础服务 D 发生响应超时或宕机，
 *      上游服务 C、B、A 的工作线程池或连接池会被持续占用并堆积，最终导致整条链路全线瘫痪。
 * 
 * 2. 介绍一下服务熔断 (Circuit Breaker)：
 *    - 本质比喻：就像家庭强电箱里的“空气开关/保险丝”。当电路过载或短路时，保险丝瞬间拉闸断电，
 *      避免烧毁整个屋子的电器设备甚至引发火灾。
 *    - 核心三态状态机：
 *      * CLOSED（闭合/正常状态）：
 *        微服务请求正常通过。熔断器在内部滑动窗口中统计请求成功率、异常比例与慢调用比例。
 *        当失败率超过阈值（如连续 3 次失败或失败率 > 50%），状态机瞬间跃迁为 OPEN。
 *      * OPEN（开启/熔断状态）：
 *        熔断器彻底切断通路！后续所有进来的请求“根本不再发往远程网络下游”，
 *        而是立即被阻断（Fast-Fail），直接触发执行本地 Fallback 降级逻辑。
 *        同时开启冷静倒计时（如 3 秒），给下游留出自我修复/重启重启时间。
 *      * HALF_OPEN（半开探测状态）：
 *        冷静倒计时结束后，熔断器进入半开状态，小心翼翼地放行极少量探针请求（Probe Request）发往下游。
 *        - 若探针请求成功：说明下游故障已排除自愈，熔断器自动重置恢复为 CLOSED 闭合状态！
 *        - 若探针请求依然失败：说明下游依然不可用，熔断器重新跳回 OPEN 状态，重置冷却时间。
 * 
 * 3. 介绍一下服务降级 (Fallback)：
 *    - 定义：在服务不可用、熔断、网络超时、并发限流或业务异常时，系统为了保障核心流程运转，
 *      采取的一种“退而求其次”、“有损但保证可用”的兜底保障机制。
 *    - 经典降级实现方案：
 *      * 返回本地默认兜底安全数据（如商品详情查询失败，返回保底静态默认数据）
 *      * 读取本地二级缓存数据（读取 Redis 或 Caffeine 本地快照缓存）
 *      * 异步暂存 / 发送至死信队列（先给用户提示“操作已提交”，后台通过 MQ 异步重试兜底）
 *      * 人工主动开关降级（双十一零点高峰期，人工主动关闭退款、评价等非核心功能，释放资源保下单）
 * 
 * 4. 熔断 vs 降级的异同与协同：
 *    +------------------+------------------------------------+------------------------------------+
 *    | 对比维度         | 服务熔断 (Circuit Breaker)         | 服务降级 (Fallback)                |
 *    +------------------+------------------------------------+------------------------------------+
 *    | 核心出发点       | 站在架构防护角度，防止系统级联崩溃 | 站在用户体验角度，提供保底可用方案 |
 *    | 触发条件         | 必须达到设定的统计阈值才跳闸拉闸   | 任何单次异常、超时、限流或熔断均可 |
 *    | 是否发网络请求   | OPEN 状态下直接拦截，根本不发网络  | 超时等降级仍可能先发网络，熔断则不发|
 *    | 相互关系         | 熔断通常会伴随降级（熔断后走兜底） | 降级不一定因为熔断（单个异常即可降）|
 *    +------------------+------------------------------------+------------------------------------+
 * ============================================================================
 */
public class CircuitBreakerAndFallbackDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("服务熔断状态机（CLOSED -> OPEN -> HALF_OPEN -> CLOSED）全链路流转实战");
        System.out.println("==========================================================================");

        // 阈值：连续失败 3 次拉闸熔断，熔断后冷却 1500 毫秒（1.5秒）进入半开探测
        MiniCircuitBreaker circuitBreaker = new MiniCircuitBreaker(3, 1500);

        // 模拟不可稳定的远程微服务（前 3 次调用必崩，第 4 次及以后恢复健康）
        RemoteOrderService mockRemoteService = new RemoteOrderService();

        System.out.println("\n--- 阶段一：初始正常状态 (CLOSED)，开始连续发起调用 ---");
        for (int i = 1; i <= 4; i++) {
            final int requestId = i;
            String result = circuitBreaker.execute(
                    // 业务远程调用
                    () -> mockRemoteService.queryOrder("ORDER_100" + requestId),
                    // 降级兜底逻辑 (Fallback)
                    (ex) -> "[Fallback 降级响应] 下游服务异常或触发熔断，返回保底缓存订单数据 (原因: " + ex.getMessage() + ")"
            );
            System.out.println("请求 " + requestId + " 响应: " + result);
        }

        System.out.println("\n--- 阶段二：熔断器已处于 OPEN 状态，此时所有请求不再发往远程网络，直接快速失败并降级 ---");
        String fastFailResult = circuitBreaker.execute(
                () -> mockRemoteService.queryOrder("ORDER_10099"),
                (ex) -> "[Fallback 降级响应] 熔断器处于开启状态，直接触发降级，保护系统不受连带崩溃！"
        );
        System.out.println("高频请求直接阻断响应: " + fastFailResult);

        System.out.println("\n--- 阶段三：等待 1.6 秒冷却期，等待熔断器自动由 OPEN 跃迁为 HALF_OPEN (半开探测态) ---");
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- 阶段四：发起半开探针请求（此时远程服务已修复恢复正常） ---");
        String probeResult = circuitBreaker.execute(
                () -> mockRemoteService.queryOrder("ORDER_10088"),
                (ex) -> "[Fallback 降级响应] 探测失败"
        );
        System.out.println("探针请求执行响应: " + probeResult);

        System.out.println("\n--- 阶段五：探针探测成功，熔断器自动重置自愈为 CLOSED 状态 ---");
        String finalResult = circuitBreaker.execute(
                () -> mockRemoteService.queryOrder("ORDER_10066"),
                (ex) -> "[Fallback 降级响应] 兜底"
        );
        System.out.println("后续常规请求响应: " + finalResult);
        System.out.println("\n[全链路验证通过] 熔断器完成 CLOSED -> OPEN -> HALF_OPEN -> CLOSED 完整闭环！");
    }
}
