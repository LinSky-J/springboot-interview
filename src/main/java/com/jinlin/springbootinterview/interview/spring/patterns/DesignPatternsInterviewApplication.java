package com.jinlin.springbootinterview.interview.spring.patterns;

import com.jinlin.springbootinterview.interview.spring.patterns.observer.MiniEvent;
import com.jinlin.springbootinterview.interview.spring.patterns.observer.MiniEventMulticaster;
import com.jinlin.springbootinterview.interview.spring.patterns.template.MiniJdbcTemplate;

/**
 * ============================================================================
 * 专题六：Spring 经典设计模式与核心注解问答与验证中心
 * ============================================================================
 * 
 * [涵盖核心面试题目与教学详解]
 * 
 * 1. Spring 框架中用到的经典设计模式全景剖析：
 *    - 单例模式：DefaultSingletonBeanRegistry 的 singletonObjects 保证单例 Bean。
 *    - 简单工厂 / 工厂方法：BeanFactory 及其各大实现子类。
 *    - 抽象工厂：ApplicationContext，整合环境配置、国际化、事件发布。
 *    - 专用工厂：FactoryBean，用于定制复杂第三方的 Bean 注入（如 MyBatis SqlSessionFactoryBean）。
 *    - 代理模式：Spring AOP（JdkDynamicAopProxy 与 CglibAopProxy）。
 *    - 模板方法模式：JdbcTemplate, RestTemplate, AbstractApplicationContext.refresh()。
 *    - 观察者模式：ApplicationEvent, ApplicationListener, ApplicationEventPublisher。
 *    - 适配器模式：Spring MVC 的 HandlerAdapter（适配各种 Controller/HttpRequestHandler/Servlet）。
 *    - 策略模式：InstantiationStrategy（选择反射还是 CGLIB 进行对象实例化）与 Resource 体系。
 *    - 责任链模式：HandlerExecutionChain 拦截器链与 AOP 的 ReflectiveMethodInvocation 拦截器链。
 * 
 * 2. Spring 常用注解体系归类（面试答题标准框架）：
 *    - 组件声明类：@Component, @Service, @Repository, @Controller, @Configuration, @Bean
 *    - 依赖注入类：@Autowired (默认byType), @Resource (默认byName), @Qualifier, @Value, @Primary
 *    - 生命周期与作用域：@Scope, @Lazy, @PostConstruct, @PreDestroy
 *    - 条件装配类：@Conditional, @ConditionalOnClass, @ConditionalOnMissingBean, @ConditionalOnProperty
 *    - AOP 切面类：@Aspect, @Pointcut, @Around, @Before, @After, @AfterReturning, @AfterThrowing
 *    - 事务控制类：@Transactional
 *    - Web MVC 类：@RestController, @RequestMapping, @RequestParam, @PathVariable, @RequestBody, @ResponseBody
 * ============================================================================
 */
public class DesignPatternsInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("1. 模板方法模式运行验证（JdbcTemplate 骨架与回调）");
        System.out.println("=================================================");
        MiniJdbcTemplate jdbcTemplate = new MiniJdbcTemplate();
        jdbcTemplate.execute("SELECT * FROM t_user WHERE id = 1001", raw -> {
            System.out.println("[业务回调逻辑] 成功将数据游标转为实体对象: " + raw);
            return raw;
        });

        System.out.println();
        System.out.println("=================================================");
        System.out.println("2. 观察者模式运行验证（事件发布与广播监听）");
        System.out.println("=================================================");
        MiniEventMulticaster multicaster = new MiniEventMulticaster();
        multicaster.addListener(e -> System.out.println("[邮件观察者服务] 监听到事件，向用户发送激活通知: " + e.getSource()));
        multicaster.addListener(e -> System.out.println("[风控观察者服务] 监听到事件，初始化安全风控策略: " + e.getSource()));

        multicaster.publishEvent(new MiniEvent("用户【USER_NEW_888】注册完毕"));
    }
}
