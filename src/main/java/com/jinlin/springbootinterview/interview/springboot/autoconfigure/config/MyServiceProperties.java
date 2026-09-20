package com.jinlin.springbootinterview.interview.springboot.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringBoot 配置属性映射模型（自定义 Starter 核心组件）
 * 
 * 教学解析：
 * 1. 核心注解：@ConfigurationProperties(prefix = "my.service")
 *    - 它是 SpringBoot 外部化配置的核心灵魂注解之一。
 *    - 核心作用：通过前缀（prefix）将 application.yml 或 application.properties 中
 *      以 "my.service" 开头的键值对，自动、类型安全（Type-Safe）地映射绑定到当前 JavaBean 属性中。
 * 
 * 2. 相比 @Value 注解的优势：
 *    - 支持松散绑定（Relaxed Binding）：支持驼峰（apiKey）、中划线（api-key）、下划线（api_key）自动匹配。
 *    - 支持复杂嵌套对象、集合（List/Map）的批量映射注入。
 *    - 支持 JSR-303 数据校验（配合 @Validated 注解在启动时校验端口、格式等）。
 */
@ConfigurationProperties(prefix = "my.service")
public class MyServiceProperties {

    /**
     * 是否启用该自定义服务，默认开启
     */
    private boolean enabled = true;

    /**
     * 服务密钥凭证，默认配置
     */
    private String apiKey = "DEFAULT_SECRET_KEY_8888";

    /**
     * 请求超时时间（单位：毫秒），默认 5000ms
     */
    private Integer timeoutMs = 5000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    @Override
    public String toString() {
        return "MyServiceProperties{" +
                "enabled=" + enabled +
                ", apiKey='" + apiKey + '\'' +
                ", timeoutMs=" + timeoutMs +
                '}';
    }
}
