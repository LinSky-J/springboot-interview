package com.jinlin.springbootinterview.interview.springcloud.overview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================================
 * 一、Spring Cloud 核心理解、与 Spring Boot 的区别、微服务组件全景对比
 * ============================================================================
 * 
 * [面试核心高频问题]
 * 1. 了解 Spring Cloud 吗？它到底是什么？
 *    - Spring Cloud 不是某一个具体的框架，而是一整套分布式微服务治理架构的“全家桶生态规范”与“集成方案”。
 *    - 它基于 Spring Boot 的约定优于配置理念，将业界成熟的微服务组件（如 Netflix、Alibaba、HashiCorp 等）
 *      进行二次封装，提供统一的统一抽象接口（如 DiscoveryClient, LoadBalancer, CircuitBreaker）。
 * 
 * 2. Spring Cloud 和 Spring Boot 的本质区别是什么？
 *    +------------------+------------------------------------+------------------------------------+
 *    | 比较维度         | Spring Boot                        | Spring Cloud                       |
 *    +------------------+------------------------------------+------------------------------------+
 *    | 定位目标         | 快速构建单一独立的微服务应用个件   | 全局分布式微服务集群的治理与协同   |
 *    | 解决痛点         | 简化 Spring 繁杂配置、起步依赖开箱即用| 解决分布式网络通信、服务发现、熔断 |
 *    |                  | 内嵌 Web 容器，专注于单体或微服务个体| 配置中心、网关路由等跨进程集群问题 |
 *    | 依赖关系         | 底层基础设施，可以单独脱离 Cloud 使用| 强依赖于 Spring Boot，必须基于 Boot|
 *    | 部署范围         | 关注目标是“单个进程”内部的开发效率 | 关注目标是“成百上千个微服务”的治理 |
 *    | 版本命名规则     | 语义化数字版本（如 3.2.0, 2.7.18） | 伦敦地铁站字母序/年份发布列车      |
 *    |                  |                                    | （如 Greenwich, Hoxton, 2023.0.x） |
 *    +------------------+------------------------------------+------------------------------------+
 * 
 * 3. 生产中用过哪些微服务组件？（三大流派演进路线）
 *    - 【第一代：Spring Cloud Netflix 经典体系（多数已停止维护/闭源进入维护期）】
 *      * 注册中心：Eureka
 *      * 客户端负载均衡：Ribbon
 *      * 声明式 HTTP 调用：Feign / OpenFeign
 *      * 服务熔断限流：Hystrix
 *      * 微服务网关：Zuul 1.x
 *      * 集中配置中心：Spring Cloud Config + Spring Cloud Bus
 *    - 【第二代：Spring Cloud Alibaba 体系（国内主流，性能强劲，拥抱云原生）】
 *      * 注册中心 + 配置中心：Nacos（双合一，支持 AP/CP 模式动态切换，支持实时推送）
 *      * 服务熔断与流量防卫：Sentinel（支持丰富流控规则、系统自适应保护、实时监控大盘）
 *      * 远程 RPC / HTTP 调用：Dubbo Spring Cloud 或 OpenFeign
 *      * 微服务网关：Spring Cloud Gateway
 *      * 分布式事务：Seata（AT 模式无侵入、TCC 模式、SAGA 模式）
 *    - 【第三代：Spring Cloud 官方原生替换生态】
 *      * 注册/配置中心：Spring Cloud Consul / Spring Cloud Kubernetes
 *      * 客户端负载均衡：Spring Cloud LoadBalancer（替代已下线的 Ribbon）
 *      * 服务熔断降级：Spring Cloud CircuitBreaker（底层适配 Resilience4j，替代 Hystrix）
 *      * 微服务网关：Spring Cloud Gateway（基于 Netty + WebFlux 响应式非阻塞架构，替代 Zuul）
 * ============================================================================
 */
public class SpringCloudVsSpringBootExplanation {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("Spring Boot vs Spring Cloud 本质定位与微服务组件生态全景解析");
        System.out.println("==========================================================================");

        // 1. 打印对比总结
        System.out.println("\n[一句话总结二者关系]");
        System.out.println("Spring Boot 是微服务大厦中的每一块坚固砖石（个体）；");
        System.out.println("Spring Cloud 是把所有砖石连接起来的钢筋水泥与水电管道（集群治理网络）。");

        // 2. 展示主流三代组件对照矩阵
        System.out.println("\n[微服务核心五大治理能力与技术选型对照表]");
        List<ComponentRow> matrix = buildComponentMatrix();
        System.out.printf("%-14s | %-20s | %-24s | %-24s%n", 
                "治理维度", "第一代(Netflix)", "第二代(Alibaba 主流)", "第三代(Spring 官方推荐)");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (ComponentRow row : matrix) {
            System.out.printf("%-14s | %-20s | %-24s | %-24s%n",
                    row.dimension, row.netflix, row.alibaba, row.springOfficial);
        }
    }

    private static List<ComponentRow> buildComponentMatrix() {
        List<ComponentRow> list = new ArrayList<>();
        list.add(new ComponentRow("服务注册与发现", "Eureka (停更)", "Nacos (强推, 注册+配置)", "Consul / K8s DNS"));
        list.add(new ComponentRow("服务配置中心", "Spring Cloud Config", "Nacos (动态热刷新秒级生效)", "Consul KV / K8s ConfigMap"));
        list.add(new ComponentRow("客户端负载均衡", "Ribbon (停更)", "Spring Cloud LoadBalancer", "Spring Cloud LoadBalancer"));
        list.add(new ComponentRow("声明式服务调用", "Feign (停更)", "OpenFeign / Dubbo", "OpenFeign / HTTP Interface"));
        list.add(new ComponentRow("服务熔断与限流", "Hystrix (停更)", "Sentinel (阿里流量防卫兵)", "Resilience4j"));
        list.add(new ComponentRow("微服务网关", "Zuul 1.x (阻塞式)", "Spring Cloud Gateway", "Spring Cloud Gateway (响应式)"));
        list.add(new ComponentRow("分布式事务", "LCN (老旧方案)", "Seata (阿里开源主流)", "Seata"));
        return Collections.unmodifiableList(list);
    }

    static class ComponentRow {
        String dimension;
        String netflix;
        String alibaba;
        String springOfficial;

        public ComponentRow(String dimension, String netflix, String alibaba, String springOfficial) {
            this.dimension = dimension;
            this.netflix = netflix;
            this.alibaba = alibaba;
            this.springOfficial = springOfficial;
        }
    }
}
