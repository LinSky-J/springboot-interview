package com.jinlin.springbootinterview.interview.springboot.autoconfigure.service;

import com.jinlin.springbootinterview.interview.springboot.autoconfigure.config.MyServiceProperties;

/**
 * 自定义 Starter 对外提供的核心业务能力对象
 * 
 * 教学解析：
 * 1. 在编写自定义 Starter 时，这是由外部组件库统一提供的封装类（如 RedisTemplate、RabbitTemplate）。
 * 2. 它持有配置类 MyServiceProperties，基于开发人员在 application.yml 中配置的参数执行实际操作。
 */
public class MyStarterService {

    private final MyServiceProperties properties;

    public MyStarterService(MyServiceProperties properties) {
        this.properties = properties;
    }

    /**
     * 对外提供的核心业务方法
     * @param payload 传入数据
     * @return 执行响应结果
     */
    public String executeBusiness(String payload) {
        if (!properties.isEnabled()) {
            return "[Starter 服务已停用] 当前服务未开启，请检查配置 my.service.enabled=true";
        }
        System.out.println("[Starter 核心业务执行] 使用密钥: " + properties.getApiKey() + "，超时时间: " + properties.getTimeoutMs() + "ms");
        System.out.println("[Starter 核心业务执行] 正在处理数据载荷: " + payload);
        return "数据 [" + payload + "] 处理成功！";
    }

    public MyServiceProperties getProperties() {
        return properties;
    }
}
