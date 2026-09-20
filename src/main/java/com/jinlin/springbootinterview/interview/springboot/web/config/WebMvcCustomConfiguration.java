package com.jinlin.springbootinterview.interview.springboot.web.config;

import com.jinlin.springbootinterview.interview.springboot.web.interceptor.DemoAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringBoot Web MVC 定制配置类
 * 
 * 教学解析：
 * 1. 核心接口：WebMvcConfigurer
 *    - 在 SpringBoot 2.x+ 中，官方推荐实现 WebMvcConfigurer 接口来定制 MVC 配置
 *      （替代了早期已废弃的 WebMvcConfigurerAdapter 抽象类）。
 *    - 注意避坑：千万不要随便继承 WebMvcConfigurationSupport，
 *      否则会导致 SpringBoot 对 Spring MVC 的全部默认自动装配（静态资源映射、日期格式化等）全盘失效！
 * 
 * 2. 拦截器注册机制：
 *    - 通过实现 addInterceptors 方法，将自定义的 DemoAuthInterceptor 注册进 Spring MVC 拦截器流水线。
 *    - 支持 addPathPatterns 指定拦截路由模式，excludePathPatterns 排除白名单路由（如登录、静态资源）。
 */
@Configuration
public class WebMvcCustomConfiguration implements WebMvcConfigurer {

    private final DemoAuthInterceptor authInterceptor;

    public WebMvcCustomConfiguration(DemoAuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") // 拦截 /api 前缀的所有请求
                .excludePathPatterns("/api/login", "/api/public/**"); // 排除免认证路由
        System.out.println("[WebMvcConfigurer 配置] DemoAuthInterceptor 拦截器已成功挂载到路由规则中");
    }
}
