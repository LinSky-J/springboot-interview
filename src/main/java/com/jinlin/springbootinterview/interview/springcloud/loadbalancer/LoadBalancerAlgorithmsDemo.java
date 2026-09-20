package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * 二、负载均衡核心算法详解与“如何一直均衡给同一个用户”实战实现
 * ============================================================================
 * 
 * [面试核心高频问题]
 * 1. 负载均衡有哪些经典算法？
 *    - 轮询 (Round Robin)：按顺序逐个分发给后端实例，适合机器硬件配置一致的集群。
 *    - 随机 (Random)：随机选择目标实例，请求量越大分布越趋于均匀。
 *    - 加权轮询 (Weighted Round Robin)：根据服务器硬件配置高低分配权重（如 8C16G 分配权重 5，2C4G 分配权重 1），
 *      权重高的服务器承担更多请求。
 *    - 最小活跃调用数 / 最小连接数 (Least Active / Least Connections)：
 *      挑选当前正在处理请求数最少的节点，将请求压向最空闲的服务器（如 Dubbo LeastActiveLoadBalance）。
 *    - 响应时间加权 (Weighted Response Time)：
 *      统计近期平均响应时间，越快响应的节点分配越高权重（如 Ribbon WeightedResponseTimeRule）。
 *    - 一致性哈希 (Consistent Hashing)：
 *      构建虚拟节点哈希环，根据请求关键特征（IP、用户ID、参数）映射，集群缩容扩容时对全局影响最小。
 * 
 * 2. 如何实现“一直均衡给一个用户”？（用户级粘性路由 / Sticky Session）
 *    - 方案一：一致性哈希算法（Consistent Hash by User ID）
 *      以用户的唯一身份标识（如 userId / token / 客户端 IP）计算 Hash 值投射到哈希环上，
 *      相同用户永远计算出相同的哈希槽位，精准路由到同一台服务实例。
 *      适用场景：本地内存缓存（一级缓存命中率最大化）、长连接会话保持。
 *    - 方案二：Cookie 植入粘性会话（Sticky Cookie）
 *      微服务网关（Spring Cloud Gateway / Nginx）在用户首次握手时，在 Response Header 中注入
 *      一个名为 SERVERID 或 ROUTE_ID 的加密 Cookie；
 *      客户端后续请求自动携带该 Cookie，网关解析该 Cookie 后直接定向发往对应的微服务实例。
 *    - 方案三：Spring Cloud LoadBalancer 定制化扩展
 *      实现 ReactorServiceInstanceLoadBalancer 接口，重写 choose(Request request) 方法；
 *      从 RequestDataContext 中拿到当前请求上下文（如请求头 X-User-Id），通过一致性算法锁定目标节点。
 *      当原目标节点宕机下线时，负载均衡器自动顺时针漂移到下一个健康节点，保证容灾高可用。
 * ============================================================================
 */
public class LoadBalancerAlgorithmsDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("负载均衡经典算法实现 与 同一用户粘性定向路由演示");
        System.out.println("==========================================================================");

        List<ServiceInstance> instances = Arrays.asList(
                new ServiceInstance("192.168.1.101", 8080, 1),
                new ServiceInstance("192.168.1.102", 8080, 2),
                new ServiceInstance("192.168.1.103", 8080, 3)
        );

        // 1. 测试基础轮询算法 (Round Robin)
        System.out.println("\n--- 1. 基础轮询算法 (Round Robin) 测试 (6 次连续请求) ---");
        LoadBalancer roundRobin = new RoundRobinLoadBalancer(instances);
        for (int i = 1; i <= 6; i++) {
            System.out.println("第 " + i + " 次请求路由到: " + roundRobin.select().getHostAndPort());
        }

        // 2. 测试随机算法 (Random)
        System.out.println("\n--- 2. 随机算法 (Random) 测试 (3 次请求) ---");
        LoadBalancer randomLb = new RandomLoadBalancer(instances);
        for (int i = 1; i <= 3; i++) {
            System.out.println("随机分发: " + randomLb.select().getHostAndPort());
        }

        // 3. 测试加权轮询算法 (Weighted Round Robin)
        System.out.println("\n--- 3. 加权轮询算法 (权重 1:2:3，总计分发 6 次) ---");
        LoadBalancer weightedLb = new WeightedRoundRobinLoadBalancer(instances);
        for (int i = 1; i <= 6; i++) {
            System.out.println("加权路由: " + weightedLb.select().getHostAndPort());
        }

        // 4. 核心面试问题解决演示：一致性哈希实现“一直均衡给一个用户”
        System.out.println("\n==========================================================================");
        System.out.println("核心突破：如何实现一直均衡给同一个用户？（基于 UserId 的一致性哈希路由）");
        System.out.println("==========================================================================");

        ConsistentHashUserStickyLoadBalancer stickyLb = new ConsistentHashUserStickyLoadBalancer(instances, 100);

        String userAlice = "user_alice_8888";
        String userBob = "user_bob_6666";
        String userCharlie = "user_charlie_9999";

        System.out.println("\n[测试 1: 模拟 Alice 多次连续请求]");
        for (int i = 1; i <= 4; i++) {
            ServiceInstance node = stickyLb.selectByUser(userAlice);
            System.out.println("Alice 第 " + i + " 次调用 -> 命中服务实例: " + node.getHostAndPort());
        }

        System.out.println("\n[测试 2: 模拟 Bob 多次连续请求]");
        for (int i = 1; i <= 4; i++) {
            ServiceInstance node = stickyLb.selectByUser(userBob);
            System.out.println("Bob 第 " + i + " 次调用 -> 命中服务实例: " + node.getHostAndPort());
        }

        System.out.println("\n[测试 3: 模拟 Charlie 多次连续请求]");
        for (int i = 1; i <= 4; i++) {
            ServiceInstance node = stickyLb.selectByUser(userCharlie);
            System.out.println("Charlie 第 " + i + " 次调用 -> 命中服务实例: " + node.getHostAndPort());
        }

        System.out.println("\n[验证结论] 无论请求多少次，Alice、Bob、Charlie 各自请求均牢牢绑定在专属的目标实例上！");
    }
}
