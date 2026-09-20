package com.jinlin.springbootinterview.interview.mybatis.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ============================================================================
 * 传统 JDBC 标准操作规范与 MyBatis 优势深度对比
 * ============================================================================
 * 
 * 本类涵盖的核心面试问题如下：
 * 1. 还记得原生 JDBC 连接与操作数据库的完整步骤吗？
 * 2. 与传统的 JDBC 相比，MyBatis 有哪些显著优点？
 * 3. 你觉得 MyBatis 在哪些方面做得最好？
 * 
 * ============================================================================
 * [面试核心考点深度教学]
 * 
 * 一、原生 JDBC 访问数据库的完整六大步骤
 * 1. 第一步：加载数据库驱动
 *    - Class.forName("com.mysql.cj.jdbc.Driver");
 *    - 作用：触发 Driver 类中的静态代码块，向 DriverManager 注册驱动。
 * 2. 第二步：获取数据库连接
 *    - Connection conn = DriverManager.getConnection(url, username, password);
 *    - 建立与 MySQL 服务端的底层 TCP 网络套接字连接。
 * 3. 第三步：创建预编译 SQL 语句对象
 *    - PreparedStatement ps = conn.prepareStatement("SELECT * FROM t_user WHERE id = ?");
 *    - 服务端对 SQL 模板进行语法解析与预编译优化。
 * 4. 第四步：设置 SQL 占位符参数
 *    - ps.setLong(1, 1001L);
 *    - 逐个参数手动按索引绑定传参。
 * 5. 第五步：执行 SQL 并遍历处理结果集
 *    - ResultSet rs = ps.executeQuery();
 *    - while (rs.next()) { ...手动读取列数据并逐字段 new 实体赋值... }
 * 6. 第六步：在 finally 块中逆序关闭释放资源
 *    - 依次关闭 ResultSet -> PreparedStatement -> Connection，防止连接泄漏。
 * 
 * ----------------------------------------------------------------------------
 * 二、原生 JDBC 的核心痛点 vs MyBatis 的降维打击优势
 * 
 * 1. 痛点一：硬编码繁重，样板代码极多
 *    - JDBC：每次查询都需要写长达几十行的获取连接、创建 Statement、遍历结果、手动关闭资源等重复代码。
 *    - MyBatis：完全封装了底层 Connection 管理与资源关闭，通过声明式映射将关注点聚焦在 SQL 本身。
 * 
 * 2. 痛点二：SQL 与 Java 代码严重纠缠，难以维护
 *    - JDBC：SQL 语句作为字符串硬编码写死在 Java 代码中，修改 SQL 必须重新编译整个项目，DBA 无法直接审查。
 *    - MyBatis：将 SQL 集中抽离到独立规范的 Mapper XML 文件中，实现 SQL 与 Java 源码的彻底解耦。
 * 
 * 3. 痛点三：参数手动设置极其繁琐（输入映射问题）
 *    - JDBC：对于复杂查询，必须根据 "?" 顺序手动调用 ps.setXxx(1, ...)，参数错位极易引发运行时异常。
 *    - MyBatis：提供强大的输入参数映射，自动将 JavaBean 属性、Map 键值映射到 SQL 的 #{属性名} 占位符中。
 * 
 * 4. 痛点四：结果集手动遍历封装极为痛苦（输出映射问题）
 *    - JDBC：每次都要手动 rs.getString("user_name")，实体字段多了之后极其枯燥易错。
 *    - MyBatis：提供极致的自动化 ORM 结果映射，支持驼峰命名自动转换（mapUnderscoreToCamelCase）、
 *      高级复杂关联映射（association 一对一、collection 一对多）。
 * 
 * ----------------------------------------------------------------------------
 * 三、你觉得 MyBatis 在哪些方面做得最好？（大厂深度回答维度）
 * 1. “半自动 ORM”的黄金平衡点：
 *    - 相比全自动 ORM（如 Hibernate），MyBatis 不盲目接管 SQL 的生成。它把 SQL 的完全控制权留给开发者，
 *      使得针对高并发海量数据的慢 SQL 优化、复合索引调优、SQL 调优诊断变得极为容易。
 * 2. 强大的动态 SQL 标签体系：
 *    - 内置 <if>、<choose>、<where>、<trim>、<foreach> 等标签，
 *      彻底终结了原生 JDBC 时代恶心易错的字符串拼接和 "WHERE 1=1" 丑陋写法。
 * 3. 极强的灵活性与扩展性：
 *    - 支持自定义插件（Interceptor 责任链机制拦截四大对象），
 *      轻松扩展出分页插件（PageHelper/MyBatis-Plus 分页）、性能分析慢查询插件、数据脱敏插件等。
 * ============================================================================
 */
public class TraditionalJdbcDemonstration {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("展示原生 JDBC 的样板代码流程与传统数据库访问方式");
        System.out.println("=================================================================");

        // 模拟执行原生 JDBC 的完整六大标准步骤
        simulateJdbcExecution("jdbc:mysql://localhost:3306/interview_db", "root", "123456", 1001L);
    }

    /**
     * 原生 JDBC 完整标准代码流程示例
     */
    public static void simulateJdbcExecution(String url, String username, String password, Long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            System.out.println("[步骤 1] 加载数据库驱动: com.mysql.cj.jdbc.Driver");
            // Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("[步骤 2] 建立数据库物理网络连接: " + url);
            // conn = DriverManager.getConnection(url, username, password);

            System.out.println("[步骤 3] 预编译 SQL 语句模板: SELECT id, username, age FROM t_user WHERE id = ?");
            // ps = conn.prepareStatement("SELECT id, username, age FROM t_user WHERE id = ?");

            System.out.println("[步骤 4] 手动设置第 1 个占位符参数: id = " + userId);
            // ps.setLong(1, userId);

            System.out.println("[步骤 5] 执行查询并获取结果集游标，手动逐字段提取并注入 Java 实体对象...");
            // rs = ps.executeQuery();
            // while (rs.next()) {
            //     User user = new User();
            //     user.setId(rs.getLong("id"));
            //     user.setUsername(rs.getString("username"));
            //     user.setAge(rs.getInt("age"));
            // }

            System.out.println("[数据处理完成] 原生 JDBC 手动映射耗时耗力，MyBatis 可实现一键自动化 ORM 转换！");

        } catch (Exception e) {
            System.out.println("[异常处理] 处理数据库访问异常: " + e.getMessage());
        } finally {
            System.out.println("[步骤 6] 逆序手动关闭数据库资源: 关闭 ResultSet -> PreparedStatement -> Connection");
            // if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            // if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
            // if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
        }
    }
}
