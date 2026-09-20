package com.jinlin.springbootinterview.interview.springcloud.loadbalancer;

/**
 * 微服务集群节点定义
 */
public class ServiceInstance {

    private final String ip;
    private final int port;
    private final int weight; // 权重

    public ServiceInstance(String ip, int port, int weight) {
        this.ip = ip;
        this.port = port;
        this.weight = weight;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getWeight() {
        return weight;
    }

    public String getHostAndPort() {
        return ip + ":" + port + " (权重=" + weight + ")";
    }

    public String getIdentifier() {
        return ip + ":" + port;
    }
}
