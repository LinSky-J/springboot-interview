package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 3. 经典加权轮询负载均衡器 (Weighted Round Robin)
 * 原理：根据实例权重比例构建权重调度池，机器性能高的节点分配更高权重，承接更多流量。
 */
public class WeightedRoundRobinLoadBalancer implements LoadBalancer {

    private final List<ServiceInstance> weightedPool = new ArrayList<>();
    private final AtomicInteger position = new AtomicInteger(0);

    public WeightedRoundRobinLoadBalancer(List<ServiceInstance> instances) {
        if (instances != null) {
            for (ServiceInstance instance : instances) {
                for (int i = 0; i < instance.getWeight(); i++) {
                    weightedPool.add(instance);
                }
            }
        }
    }

    @Override
    public ServiceInstance select() {
        if (weightedPool.isEmpty()) {
            return null;
        }
        int current = position.getAndIncrement();
        int index = (current & Integer.MAX_VALUE) % weightedPool.size();
        return weightedPool.get(index);
    }
}
