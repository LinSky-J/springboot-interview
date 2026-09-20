package com.jinlin.springbootinterview.interview.springcloud;

import com.jinlin.springbootinterview.interview.springcloud.circuitbreaker.CircuitBreakerAndFallbackDemo;
import com.jinlin.springbootinterview.interview.springcloud.loadbalancer.LoadBalancerAlgorithmsDemo;
import com.jinlin.springbootinterview.interview.springcloud.overview.SpringCloudVsSpringBootExplanation;

/**
 * ============================================================================
 * Spring Cloud 微服务高频核心面试题系统化解析与运行总入口
 * ============================================================================
 * 
 * [面试核心问题索引与落地类映射]
 * 1. 了解 Spring Cloud 吗？说一下它和 Spring Boot 的区别？
 *    -> overview.SpringCloudVsSpringBootExplanation
 *    - 核心区别：Boot 是单体应用快速开发脚手架，Cloud 是分布式集群全局治理全家桶。
 * 
 * 2. 生产中用过哪些微服务组件？
 *    -> overview.SpringCloudVsSpringBootExplanation
 *    - 对比三大体系：Netflix(第一代 Eureka/Ribbon/Hystrix/Zuul),
 *      Alibaba(第二代主流 Nacos/Sentinel/OpenFeign/Gateway/Seata),
 *      Spring官方(第三代 Consul/LoadBalancer/Resilience4j/Gateway)。
 * 
 * 3. 负载均衡有哪些算法？
 *    -> loadbalancer.LoadBalancerAlgorithmsDemo
 *    - 轮询(Round Robin)、随机(Random)、加权轮询(Weighted Round Robin)、
 *      最小活跃数(Least Active)、一致性哈希(Consistent Hashing)。
 * 
 * 4. 如何实现一直均衡给一个用户？
 *    -> loadbalancer.LoadBalancerAlgorithmsDemo
 *    - 基于 UserId / IP 的一致性哈希 (Consistent Hashing) 算法；
 *    - 网关层 Cookie 粘性路由 (Sticky Session)；
 *    - Spring Cloud LoadBalancer 自定义 ReactorServiceInstanceLoadBalancer。
 * 
 * 5. 介绍一下服务熔断 (Circuit Breaker)？
 *    -> circuitbreaker.CircuitBreakerAndFallbackDemo
 *    - 防止雪崩效应的保险丝机制，三态状态机 CLOSED -> OPEN -> HALF_OPEN 跃迁流转。
 * 
 * 6. 介绍一下服务降级 (Fallback)？
 *    -> circuitbreaker.CircuitBreakerAndFallbackDemo
 *    - 有损但保可用的兜底措施，提供默认保底值、本地缓存或排队提示。
 * ============================================================================
 */
public class SpringCloudInterviewApplication {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("Spring Cloud 微服务核心面试实战与算法全景演示总入口");
        System.out.println("==========================================================================");

        // 1. 运行微服务概念与 Boot vs Cloud 对比
        System.out.println("\n>>> [模块 1: Spring Boot vs Spring Cloud 与微服务组件选型] <<<");
        SpringCloudVsSpringBootExplanation.main(args);

        // 2. 运行负载均衡算法与同一用户粘性路由演示
        System.out.println("\n\n>>> [模块 2: 负载均衡算法 与 同一用户粘性路由实战] <<<");
        LoadBalancerAlgorithmsDemo.main(args);

        // 3. 运行服务熔断状态机与降级兜底实战
        System.out.println("\n\n>>> [模块 3: 服务熔断状态机与服务降级实战] <<<");
        CircuitBreakerAndFallbackDemo.main(args);

        System.out.println("\n==========================================================================");
        System.out.println("所有 Spring Cloud 微服务高频考点已全部执行验证完毕！");
        System.out.println("==========================================================================");
    }
}
