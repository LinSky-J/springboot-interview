package com.jinlin.springbootinterview.interview.mybatis.sql;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * MyBatis 中 #{} 与 ${} 的本质区别与安全防范深度解析
 * ============================================================================
 * 
 * 本类涵盖的核心面试问题如下：
 * 1. MyBatis 里的 #{} 和 ${} 有什么区别？
 * 2. 为什么 #{} 能够防止 SQL 注入，而 ${} 存在严重的 SQL 注入风险？
 * 3. 在什么业务场景下必须使用 ${}？如何确保 ${} 使用时的安全性？
 * 
 * ============================================================================
 * [面试核心考点深度教学]
 * 
 * 一、#{} 与 ${} 的本质差异对照表
 * 
 * 1. 底层解析机制：
 *    - #{}: 采用【预编译占位符 (PreparedStatement)】机制。
 *      MyBatis 解析 SQL 时，会将 #{param} 替换为 JDBC 占位符问号 "?"，
 *      并在后续通过 PreparedStatement.setXxx() 统一为参数赋值并自动转义。
 *    - ${}: 采用【直接字符串拼接 (Statement)】机制。
 *      MyBatis 解析 SQL 时，直接把变量的原始值原封不动拼接到 SQL 语句字符串中，
 *      最后交给底层的 Statement 执行。
 * 
 * 2. 数据类型处理与引号添加：
 *    - #{}: 会自动根据传入参数的 Java 类型添加对应的单引号。
 *      例如：传入字符串 "admin"，SQL 被解析为: WHERE username = ?，参数传入后变为 'admin'。
 *    - ${}: 不会自动添加任何单引号。
 *      例如：传入字符串 "admin"，SQL 被解析为: WHERE username = admin（会导致语法报错：找不到列 admin）。
 * 
 * 3. 安全性对比（防 SQL 注入能力）：
 *    - #{}: 【绝对安全】，能从语法层面彻底免疫 SQL 注入攻击！
 *    - ${}: 【极度危险】，恶意用户传入精心构造的特殊字符（如 ' OR '1'='1），
 *      会直接篡改原有 SQL 的逻辑结构，导致认证绕过、拖库泄露甚至删库！
 * 
 * ----------------------------------------------------------------------------
 * 二、典型 SQL 注入场景深度还原
 * 假设登录验证 SQL：
 * 
 * 1. 错误使用 ${}：
 *    SELECT * FROM t_user WHERE username = '${username}' AND password = '${password}'
 *    - 黑客传入用户名：admin' -- 
 *    - 拼接生成的最终 SQL：
 *      SELECT * FROM t_user WHERE username = 'admin' -- ' AND password = 'xxx'
 *    - 分析：双中划线 "--" 在 SQL 中是注释符，直接把后面的密码校验截断注释掉了！黑客无需密码直接登录管理员账户！
 * 
 * 2. 正确使用 #{}：
 *    SELECT * FROM t_user WHERE username = #{username} AND password = #{password}
 *    - 编译生成的 SQL 模板：
 *      SELECT * FROM t_user WHERE username = ? AND password = ?
 *    - 数据库在执行前已经完成了 SQL 的词法与语法树编译，传入的 "admin' -- " 只会被当作
 *      纯粹的字符串字面量传递给参数槽，绝对不会被当作 SQL 语法关键字解析，攻击完全失效！
 * 
 * ----------------------------------------------------------------------------
 * 三、什么时候必须使用 ${}？如何防范注入？
 * 1. 必须使用 ${} 的三大典型场景：
 *    - 场景 A：动态分表 / 动态表名（例如按月份分表：SELECT * FROM t_order_${month}）。
 *    - 场景 B：动态指定查询列名（例如：SELECT ${columnName} FROM t_user）。
 *    - 场景 C：动态排序字段与排序方向（例如：ORDER BY ${sortField} ${sortOrder}）。
 *    - 原因：数据库 JDBC 的 PreparedStatement 占位符 "?" 只能用于替代【列值（Value）】，
 *      绝对不能用于替代【表名、列名、ORDER BY 关键字】！如果在 ORDER BY ? 处使用问号，
 *      数据库会将其解析为对常量进行排序，导致排序完全失效。
 * 
 * 2. 使用 ${} 时的生产级安全防御指南：
 *    - 方案一：后端强制白名单校验（最为推荐！）。
 *      在 Java 代码层校验前端传入的排序字段是否在允许的字段集合中，非法参数直接抛出异常拒绝。
 *    - 方案二：正则合法性过滤（如仅允许字母、数字和下划线，过滤单引号、空格、分号等危险字符）。
 * ============================================================================
 */
public class HashAndDollarDifferenceExplanation {

    // 允许排序的合法列名白名单集合
    private static final List<String> ALLOWED_SORT_COLUMNS = Arrays.asList("id", "create_time", "price", "age");
    private static final List<String> ALLOWED_SORT_ORDERS = Arrays.asList("ASC", "DESC");

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("演示 #{} 的预编译安全机制 vs ${} 动态排序的白名单防护");
        System.out.println("=================================================================");

        // 模拟正常安全的排序请求
        String safeSql = buildSafeOrderBySql("t_order", "create_time", "DESC");
        System.out.println("[合法请求拼接的 SQL] " + safeSql);

        // 模拟黑客带有 SQL 注入攻击的恶意排序参数
        try {
            buildSafeOrderBySql("t_order", "id; DROP TABLE t_order; --", "ASC");
        } catch (IllegalArgumentException e) {
            System.out.println("[成功拦截 SQL 注入攻击] " + e.getMessage());
        }
    }

    /**
     * 在必须使用 ${} 拼接 ORDER BY 时，进行严格的后端白名单安全校验
     */
    public static String buildSafeOrderBySql(String tableName, String sortColumn, String sortOrder) {
        // 校验排序字段白名单
        if (!ALLOWED_SORT_COLUMNS.contains(sortColumn.toLowerCase())) {
            throw new IllegalArgumentException("非法排序字段，疑似 SQL 注入攻击: " + sortColumn);
        }

        // 校验排序方向白名单
        if (!ALLOWED_SORT_ORDERS.contains(sortOrder.toUpperCase())) {
            throw new IllegalArgumentException("非法排序方式: " + sortOrder);
        }

        // 白名单通过后，方可安全进行 ${} 级别的文本拼接
        return "SELECT * FROM " + tableName + " ORDER BY " + sortColumn + " " + sortOrder;
    }
}
