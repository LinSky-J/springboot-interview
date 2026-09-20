package com.jinlin.springbootinterview.interview.mybatis.plus;

/**
 * ============================================================================
 * MyBatis-Plus 与 原生 MyBatis 核心区别深度对比
 * ============================================================================
 * 
 * 本类涵盖的核心面试问题如下：
 * 1. MyBatis-Plus 和原生 MyBatis 有什么区别？
 * 2. MyBatis-Plus 的核心设计理念是什么？
 * 3. MyBatis-Plus 相比原生 MyBatis 提供了哪些颠覆性的效率提升？
 * 4. 在实际企业开发中，应该如何选型？
 * 
 * ============================================================================
 * [面试核心考点深度教学]
 * 
 * 一、架构定位与核心设计理念
 * 1. 官方定位：
 *    - MyBatis-Plus (简称 MP) 是一个 MyBatis 的增强工具，在 MyBatis 的基础上“只做增强不做改变”，
 *      为简化开发、提高效率而生。
 * 2. 兼容性：
 *    - 引入 MyBatis-Plus 不会对现有工程的 MyBatis 产生任何破坏，原有的 XML 映射、自定义 SQL 依然 100% 兼容运行。
 * 
 * ----------------------------------------------------------------------------
 * 二、MyBatis-Plus 与原生 MyBatis 的六大维度核心差异对比
 * 
 * 1. 单表 CRUD 效率（最大的生产力解放）：
 *    - 原生 MyBatis：每增加一张表，都必须手写 Mapper 接口、XML 文件以及基础的 insert、deleteById、updateById、selectById 等繁琐模板 SQL。
 *    - MyBatis-Plus：只需让 Mapper 接口继承 BaseMapper<T>，Service 接口继承 IService<T>，
 *      无需编写任何 XML，立刻开箱即用拥有通用的单表 CRUD 全套 API！
 * 
 * 2. 动态查询条件构造方式：
 *    - 原生 MyBatis：必须在 XML 中使用长篇的 <where>、<if test="name != null"> 进行拼接，容易拼错且重构困难。
 *    - MyBatis-Plus：内置强大的条件构造器（Wrapper 体系），尤其是【LambdaQueryWrapper】！
 *      支持通过 Java 8 方法引用（如 eq(User::getUsername, "张三")），完全杜绝了数据库列名的魔法值硬编码，
 *      在实体字段重构重命名时，编译器会自动报错提示，极大提升了重构安全性！
 * 
 * 3. 物理分页机制：
 *    - 原生 MyBatis：通常需要引入外部插件 PageHelper（通过拦截器修改 SQL 拼接 LIMIT）或手动写 count 和 limit。
 *    - MyBatis-Plus：官方原生内置开箱即用的分页插件（PaginationInnerInterceptor），直接调用 mapper.selectPage(page, wrapper) 即可完成自动统计与物理分页。
 * 
 * 4. 自动化工程级特性支持：
 *    - 自动填充字段（MetaObjectHandler）：通过注解 @TableField(fill = FieldFill.INSERT_UPDATE)，
 *      在新增或修改时自动注入 create_time、update_time、operator_id 等审计字段，无需业务代码手动 set。
 *    - 逻辑删除（@TableLogic）：打上该注解后，所有的 delete 操作会自动转为 UPDATE 状态字段为 1，所有的 select 自动过滤已删除数据。
 *    - 乐观锁插件（@Version）：自动在更新时比对并自增版本号，防止并发冲突。
 *    - 主键自增与分布式 ID：内置雪花算法（Snowflake / ASSIGN_ID），开箱即用生成全局唯一的分布式主键。
 * 
 * 5. 代码生成器（Auto Generator）：
 *    - MyBatis-Plus 提供强大的代码生成器，只需配置好数据库连接，即可一键自动化生成对应的
 *      Entity、Mapper、XML、Service、ServiceImpl、Controller 全套标准骨架代码。
 * 
 * ----------------------------------------------------------------------------
 * 三、企业级生产选型最佳实践
 * 1. 单表维度的基础业务：【强烈推荐全面使用 MyBatis-Plus】。能节省 70% 以上的样板代码编写时间。
 * 2. 复杂多表关联、海量数据统计大报表、超核心高并发慢 SQL 优化：
 *    【推荐手写原生 MyBatis XML】。因为多表联合查询用 Wrapper 表达往往可读性极差且难以利用复合索引，
 *    在 XML 中手写清晰规范的原生 SQL，更有利于 DBA 进行针对性 Explain 分析与索引调优。
 * ============================================================================
 */
public class MyBatisPlusVsMyBatisComparison {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("展示 MyBatis-Plus 条件构造器相比原生 MyBatis 的现代化编程范式");
        System.out.println("=================================================================");

        // 模拟展示 LambdaQueryWrapper 带来的代码优雅度提升
        System.out.println("原生 MyBatis：必须在 XML 中编写冗长的 <where><if test=...>");
        System.out.println("MyBatis-Plus：通过 Lambda 链式调用，类型安全且无列名硬编码魔法值：");
        System.out.println("  LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();");
        System.out.println("  wrapper.eq(User::getStatus, 1)");
        System.out.println("         .ge(User::getAge, 18)");
        System.out.println("         .like(User::getUsername, \"admin\");");
        System.out.println("  userMapper.selectList(wrapper);");
    }
}
