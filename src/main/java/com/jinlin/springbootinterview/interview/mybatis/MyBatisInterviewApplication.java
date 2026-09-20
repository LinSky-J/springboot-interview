package com.jinlin.springbootinterview.interview.mybatis;

/**
 * ============================================================================
 * MyBatis 核心面试题大纲与常见设计模式全景题解中心
 * ============================================================================
 * 
 * 本模块深度解答以下全部核心面试题目：
 * 1. 与传统的 JDBC 相比，MyBatis 有哪些优点？
 * 2. MyBatis 觉得在哪方面做得比较好？
 * 3. 还记得 JDBC 连接数据库的完整六大步骤吗？
 * 4. 如果项目中要用到原生的 MyBatis 去查询，该怎样写？
 * 5. MyBatis 里的 #{} 和 ${} 的区别？
 * 6. MyBatis-Plus 和 MyBatis 的区别与选型？
 * 7. MyBatis 源码中运用了哪些常见的设计模式？
 * 
 * ============================================================================
 * [面试核心考点深度教学：MyBatis 八大经典设计模式剖析]
 * 
 * 1. 构建者模式 (Builder Pattern)：
 *    - 核心源码类：SqlSessionFactoryBuilder、XMLConfigBuilder、XMLMapperBuilder。
 *    - 解决痛点：Configuration 全局配置对象的初始化参数极其庞大（含环境、数据源、事务工厂、
 *      映射器、插件等），通过构建者分步解析 XML 节点组装对象，隐藏复杂的构建细节。
 * 
 * 2. 工厂模式 (Factory Pattern)：
 *    - 核心源码类：SqlSessionFactory（工厂方法模式，提供 openSession() 方法生产 SqlSession）、
 *      ObjectFactory（负责实例化结果集对象）、MapperProxyFactory（生产 Mapper 接口代理）。
 *    - 解决痛点：解耦对象的创建逻辑与业务使用逻辑。
 * 
 * 3. 代理模式 (Proxy Pattern)：
 *    - 核心源码类：MapperProxy（JDK 动态代理实现，为 Mapper 接口生成代理实例，将方法调用转为 SQL 执行）；
 *      ProxyFactory（延迟加载代理，使用 CGLIB 或 Javassist 代理实体类，在访问 getter 时按需触发懒加载查询）。
 * 
 * 4. 装饰器模式 (Decorator Pattern)：
 *    - 核心源码类：org.apache.ibatis.cache.Cache 接口及其实现族。
 *    - 结构原理解析：
 *      * PerpetualCache：最基础的核心缓存实现（底层就是一个简单的 HashMap）。
 *      * 装饰器类：LruCache（最近最少使用淘汰）、FifoCache（先进先出淘汰）、
 *        SynchronizedCache（线程安全同步控制）、LoggingCache（缓存命中率日志统计）、
 *        TransactionalCache（二级缓存事务提交支持）。
 *      * 优势：通过组合代替继承，可按需任意叠加缓存特性（如：一个既带 LRU 淘汰又带线程同步和日志统计的缓存）。
 * 
 * 5. 模板方法模式 (Template Method Pattern)：
 *    - 核心源码类：BaseExecutor（抽象基类）与 SimpleExecutor / ReuseExecutor / BatchExecutor。
 *    - 结构原理解析：
 *      * BaseExecutor 实现了通用的 query/update 模板方法，统一处理一级缓存查询、二级缓存暂存、事务回滚等通用逻辑。
 *      * 将具体真正与数据库交互的方法 doQuery() / doUpdate() 抽象为抽象方法，
 *        留给子类按特定策略（如 BatchExecutor 批处理）实现。
 * 
 * 6. 责任链模式 (Chain of Responsibility Pattern)：
 *    - 核心源码类：InterceptorChain 插件拦截器链。
 *    - 结构原理解析：
 *      * MyBatis 允许通过自定义 Interceptor 拦截四大核心组件：
 *        Executor（调度器）、StatementHandler（SQL处理器）、ParameterHandler（参数处理器）、ResultSetHandler（结果处理器）。
 *      * 多个插件层层包裹生成代理，形成类似洋葱模型的执行责任链。
 * 
 * 7. 适配器模式 (Adapter Pattern)：
 *    - 核心源码类：org.apache.ibatis.logging.Log 接口体系。
 *    - 结构原理解析：
 *      * 外部存在的日志库各不相同（Slf4j, Log4j, Log4j2, Commons-Logging, JDK Logging）。
 *      * MyBatis 定义了统一的 Log 接口，并为每一种具体的日志库提供了适配器类
 *        （如 Slf4jImpl, Log4j2Impl, Jdk14LoggingImpl），抹平了不同外部日志框架的 API 差异。
 * 
 * 8. 组合模式 (Composite Pattern)：
 *    - 核心源码类：SqlNode 接口及其子类（IfSqlNode, TrimSqlNode, WhereSqlNode, ForEachSqlNode, MixedSqlNode）。
 *    - 结构原理解析：
 *      * 动态 SQL 标签被解析为一棵树形语法树，树枝节点与叶子节点统一实现 SqlNode 接口的 apply(DynamicContext context) 方法。
 *      * 只需调用根节点的 apply 方法，即可递归完成整个动态 SQL 树的遍历与字符串拼接。
 * ============================================================================
 */
public class MyBatisInterviewApplication {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("MyBatis 面试核心知识库已建立完毕");
        System.out.println("=================================================================");
        System.out.println("1. 传统 JDBC vs MyBatis 优势已归档在: jdbc/TraditionalJdbcDemonstration.java");
        System.out.println("2. #{} 与 ${} 的底层原理与防注入已归档在: sql/HashAndDollarDifferenceExplanation.java");
        System.out.println("3. 原生 MyBatis 查询与动态代理机制已归档在: nativeflow/NativeMyBatisCoreFlow.java");
        System.out.println("4. MyBatis-Plus vs 原生 MyBatis 差异对比已归档在: plus/MyBatisPlusVsMyBatisComparison.java");
        System.out.println("5. 八大经典设计模式（Builder, Factory, Proxy, Decorator 等）已在类注释中透彻拆解！");
    }
}
