package com.jinlin.springbootinterview.interview.mybatis.nativeflow;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ============================================================================
 * 原生 MyBatis 查询编程范式与核心对象生命周期深度剖析
 * ============================================================================
 * 
 * 本类涵盖的核心面试问题如下：
 * 1. 如果项目中要使用原生的 MyBatis 去执行查询，代码该怎样写？
 * 2. 原生 MyBatis 核心四大对象的职责与生命周期分别是什么？
 * 3. 为什么通过 Mapper 接口没有写任何实现类就能直接调用方法？底层机制是什么？
 * 
 * ============================================================================
 * [面试核心考点深度教学]
 * 
 * 一、原生 MyBatis 执行查询的标准编程范式
 * 
 * // 1. 读取 MyBatis 核心全局配置文件 mybatis-config.xml
 * String resource = "mybatis-config.xml";
 * InputStream inputStream = Resources.getResourceAsStream(resource);
 * 
 * // 2. 创建 SqlSessionFactoryBuilder 构建者对象，并解析配置构建 SqlSessionFactory
 * SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
 * 
 * // 3. 从工厂中获取 SqlSession 会话（使用 try-with-resources 保证安全关闭）
 * try (SqlSession session = sqlSessionFactory.openSession()) {
 *     // 方式 A（古老传统方式，存在硬编码字符串风险）：
 *     User user = session.selectOne("com.jinlin.mapper.UserMapper.selectById", 1001L);
 * 
 *     // 方式 B（主流推荐：通过类型安全的 Mapper 接口代理）：
 *     UserMapper mapper = session.getMapper(UserMapper.class);
 *     User user = mapper.selectById(1001L);
 * }
 * 
 * ----------------------------------------------------------------------------
 * 二、原生 MyBatis 核心四大对象的生命周期与作用域
 * 
 * 1. SqlSessionFactoryBuilder（构建者）：
 *    - 职责：负责解析 XML 文件、构建全局 Configuration 对象并产出 SqlSessionFactory。
 *    - 生命周期：【方法作用域（局部变量）】。一旦创建了 SqlSessionFactory，它的使命就完成了，应该立即被垃圾回收释放。
 * 
 * 2. SqlSessionFactory（会话工厂）：
 *    - 职责：根据 Configuration 配置信息生产 SqlSession 实例。
 *    - 生命周期：【应用作用域（全局单例 Application Scope）】。
 *      在整个应用的运行生命周期中应该只有一个单例工厂，避免重复解析 XML 消耗性能。
 * 
 * 3. SqlSession（会话对象）：
 *    - 职责：代表一次与数据库的连接会话，内部持有真实的 Connection 和事务状态，提供 CRUD API。
 *    - 生命周期：【请求 / 线程作用域（Request / Thread Scope）】。
 *      【注意：SqlSession 是绝对线程不安全的！】不能在多线程间共享，每次用完必须立即显式关闭（session.close()）。
 * 
 * 4. Mapper 接口代理对象（MapperProxy）：
 *    - 职责：绑定接口方法与 XML 中的 MappedStatement 映射语句。
 *    - 生命周期：【方法调用作用域】。由 SqlSession.getMapper() 动态生成，其生命周期最大不应超过创建它的 SqlSession。
 * 
 * ----------------------------------------------------------------------------
 * 三、为什么 Mapper 只有接口没有实现类就能调用？（底层原理：JDK 动态代理）
 * - 当我们调用 sqlSession.getMapper(UserMapper.class) 时，MyBatis 并没有为我们生成真正的 Java 文件，
 *   而是利用 MapperProxyFactory 借助 JDK 的 Proxy.newProxyInstance(...) 动态生成了一个代理实例。
 * - 该代理实例持有着 MapperProxy（实现了 InvocationHandler 接口）。
 * - 当调用 mapper.selectById(1001L) 时，触发 MapperProxy.invoke()，
 *   通过全类名 + 方法名（com.jinlin.mapper.UserMapper.selectById）定位到 XML 中对应的
 *   <select id="selectById"> 节点，最终委托给 sqlSession.selectOne() 驱动底层 JDBC 执行！
 * ============================================================================
 */
public class NativeMyBatisCoreFlow {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("模拟原生 MyBatis 的 Mapper 动态代理执行全流程");
        System.out.println("=================================================================");

        // 模拟 SqlSession 生产 Mapper 代理对象
        UserDemoMapper mapper = getMockMapper(UserDemoMapper.class);

        // 调用接口方法，验证动态代理拦截与 SQL 路由执行
        UserDemoDTO user = mapper.selectUserById(8888L);
        System.out.println("[调用成功] 获取到查询结果: " + user);
    }

    /**
     * 业务演示 Mapper 接口
     */
    public interface UserDemoMapper {
        UserDemoDTO selectUserById(Long id);
    }

    /**
     * 业务演示 DTO
     */
    public static class UserDemoDTO {
        private Long id;
        private String username;

        public UserDemoDTO(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        @Override
        public String toString() {
            return "UserDemoDTO{id=" + id + ", username='" + username + "'}";
        }
    }

    /**
     * 模拟 MyBatis 核心底层：MapperProxy 动态代理工厂
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMockMapper(Class<T> mapperInterface) {
        return (T) Proxy.newProxyInstance(
                mapperInterface.getClassLoader(),
                new Class<?>[]{mapperInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 1. 获取接口全限定名与方法名组成的 StatementID
                        String statementId = method.getDeclaringClass().getName() + "." + method.getName();
                        System.out.println("[MyBatis MapperProxy 代理拦截] 检测到方法调用: " + statementId);

                        // 2. 模拟从 XML 映射表中定位 MappedStatement
                        System.out.println("[MappedStatement 匹配] 成功定位到 XML 中的 SQL: SELECT id, username FROM t_user WHERE id = ?");

                        // 3. 模拟底层 SqlSession.selectOne 委托执行
                        Long queryId = (Long) args[0];
                        System.out.println("[SqlSession 底层执行] 传入参数: " + queryId + "，通过 PreparedStatement 执行并自动映射实体");

                        // 返回模拟结果
                        return new UserDemoDTO(queryId, "原生MyBatis用户_张三");
                    }
                }
        );
    }
}
