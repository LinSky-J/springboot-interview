package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

/**
 * 负载均衡策略顶层通用接口
 */
public interface LoadBalancer {

    /**
     * 从服务可用实例列表中挑选出一个目标实例
     *
     * @return 目标微服务实例
     */
    ServiceInstance select();
}
