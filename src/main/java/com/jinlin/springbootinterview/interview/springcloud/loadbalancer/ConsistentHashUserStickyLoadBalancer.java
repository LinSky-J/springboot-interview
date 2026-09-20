package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 4. 基于用户 ID 的一致性哈希粘性负载均衡器 (Consistent Hash Sticky Load Balancer)
 * 解决“如何实现一直均衡给同一个用户”的核心利器！
 * 
 * 核心原理：
 * 1. 虚拟节点环：将每个真实节点映射为 N 个虚拟节点均匀散列在 2^32 环形哈希空间，彻底杜绝数据倾斜。
 * 2. 顺时针寻径：对用户的唯一身份键（如 userId、IP、token）计算 Hash 值，沿环顺时针寻找首个节点。
 * 3. 容灾最小化：当某节点宕机时，仅影响该节点附近的小范围用户，其他用户依然精准绑定原实例。
 */
public class ConsistentHashUserStickyLoadBalancer {

    // 虚拟节点环（红黑树有序映射：Hash值 -> 实际物理节点）
    private final SortedMap<Integer, ServiceInstance> hashRing = new TreeMap<>();
    private final int numberOfVirtualNodes;

    public ConsistentHashUserStickyLoadBalancer(List<ServiceInstance> instances, int numberOfVirtualNodes) {
        this.numberOfVirtualNodes = numberOfVirtualNodes;
        if (instances != null) {
            for (ServiceInstance instance : instances) {
                addServerNode(instance);
            }
        }
    }

    // 将物理服务节点扩展为若干虚拟节点，均匀散列到 2^32 环形空间中，避免数据倾斜
    public void addServerNode(ServiceInstance instance) {
        for (int i = 0; i < numberOfVirtualNodes; i++) {
            String virtualNodeKey = instance.getIdentifier() + "&&VN_" + i;
            int hash = hash(virtualNodeKey);
            hashRing.put(hash, instance);
        }
    }

    /**
     * 核心算法：根据用户的唯一身份键（如 userId、IP、token）在环上顺时针寻径
     */
    public ServiceInstance selectByUser(String userId) {
        if (hashRing.isEmpty() || userId == null) {
            return null;
        }
        int userHash = hash(userId);
        // 如果没有直接命中，取顺时针方向的第一个虚拟节点
        SortedMap<Integer, ServiceInstance> tailMap = hashRing.tailMap(userHash);
        int targetHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
        return hashRing.get(targetHash);
    }

    /**
     * FNV1_32_HASH 经典 32 位哈希算法（分布均匀，碰撞率极低）
     */
    private int hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }
}
