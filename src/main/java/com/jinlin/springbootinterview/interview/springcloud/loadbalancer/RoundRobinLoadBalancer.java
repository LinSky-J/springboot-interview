package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 经典轮询负载均衡器 (Round Robin)
 * 原理：通过原子整数自增求模，保证多线程高并发下的均匀轮流分发。
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final List<ServiceInstance> instances;
    private final AtomicInteger position = new AtomicInteger(0);

    public RoundRobinLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;
    }

    @Override
    public ServiceInstance select() {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        // 原子自增，防溢出位运算处理后对实例数求模
        int current = position.getAndIncrement();
        int index = (current & Integer.MAX_VALUE) % instances.size();
        return instances.get(index);
    }
}
