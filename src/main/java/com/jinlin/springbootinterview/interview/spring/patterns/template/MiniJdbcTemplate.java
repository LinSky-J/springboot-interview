package com.jinlin.springbootinterview.interview.spring.patterns.template;

/**
 * 模板方法模式演示：模拟 Spring JdbcTemplate
 * 
 * 教学解析：
 * 模板方法模式定义了一个操作中的算法骨架，将某些步骤延迟到子类或回调中实现。
 * 框架统一负责 Connection 的获取、关闭、异常捕获，业务代码只需传入回调策略接口提取数据。
 */
public class MiniJdbcTemplate {

    @FunctionalInterface
    public interface ResultSetExtractor<T> {
        T extractData(String rawResult);
    }

    public <T> T execute(String sql, ResultSetExtractor<T> extractor) {
        System.out.println("[模板框架] 1. 从数据源连接池获取可用数据库连接 Connection");
        System.out.println("[模板框架] 2. 预编译 SQL: " + sql);
        System.out.println("[模板框架] 3. 执行查询并获得 ResultSet 数据游标流");

        String mockData = "raw_record_id_1001";
        T result = extractor.extractData(mockData);

        System.out.println("[模板框架] 4. 关闭 Statement 并将 Connection 安全放回连接池");
        return result;
    }
}
