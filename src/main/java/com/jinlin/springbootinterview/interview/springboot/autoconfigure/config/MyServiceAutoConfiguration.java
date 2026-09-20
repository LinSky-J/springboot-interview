package com.jinlin.springbootinterview.interview.springboot.autoconfigure.config;

import com.jinlin.springbootinterview.interview.springboot.autoconfigure.service.MyStarterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类（AutoConfiguration）—— 自动装配的核心实现载体
 * 
 * 教学解析：
 * 1. 核心面试题：SpringBoot 怎么做到“导入依赖就可以直接使用”的？
 *    答题三步法：
 *    第一步：【SPI 机制发现配置类】
 *      - 在 Starter 的 META-INF/spring.factories 文件中声明：
 *        org.springframework.boot.autoconfigure.EnableAutoConfiguration=...MyServiceAutoConfiguration
 *        （在 SpringBoot 2.7+ / 3.x 中改为 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports）。
 *      - SpringBoot 启动时通过 SpringFactoriesLoader（或 AutoConfigurationLoader）扫描所有 Jar 包中
 *        上述文件，将所有候选自动配置类加载到内存。
 *    
 *    第二步：【条件注解（@Conditional 系列）过滤决断】
 *      - @ConditionalOnClass: 检查当前工程的 Classpath 是否包含某个核心依赖类。
 *      - @ConditionalOnProperty: 检查 application.yml 中是否开启了某个开关属性。
 *      - @ConditionalOnMissingBean: 检查用户是否自己手动 @Bean 注入过该组件，
 *        如果用户自定义了，则自动装配退让；如果用户没有定义，则采用官方默认配置！
 * 
 *    第三步：【动态注入 Bean】
 *      - 满足所有条件后，执行带有 @Bean 的方法，自动将服务实例注入 Spring 容器单例池。
 *      - 业务开发人员只需在自己的 Controller/Service 中直接 @Autowired MyStarterService 即可使用！
 */
@Configuration
@EnableConfigurationProperties(MyServiceProperties.class)
@ConditionalOnClass(MyStarterService.class)
@ConditionalOnProperty(prefix = "my.service", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MyServiceAutoConfiguration {

    private final MyServiceProperties properties;

    public MyServiceAutoConfiguration(MyServiceProperties properties) {
        this.properties = properties;
    }

    /**
     * 当用户没有在 Spring 容器中显式声明 MyStarterService 时，
     * 自动装配机制自动创建并注入默认实例
     */
    @Bean
    @ConditionalOnMissingBean(MyStarterService.class)
    public MyStarterService myStarterService() {
        System.out.println("[SpringBoot 自动装配生效] 正在根据属性配置自动创建 MyStarterService Bean...");
        return new MyStarterService(properties);
    }
}
