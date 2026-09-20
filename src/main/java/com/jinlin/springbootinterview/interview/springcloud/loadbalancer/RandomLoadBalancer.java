package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 2. 经典随机负载均衡器 (Random)
 * 原理：利用 ThreadLocalRandom 高性能并发随机数发生器随机抽取下标。
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final List<ServiceInstance> instances;

    public RandomLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;
    }

    @Override
    public ServiceInstance select() {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(instances.size());
        return instances.get(index);
    }
}
