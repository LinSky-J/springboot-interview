package com.jinlin.springbootinterview.interview.springmvc.controller;

import com.jinlin.springbootinterview.interview.springmvc.model.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MVC 架构中的控制层（Controller）
 * 
 * 教学解析：
 * 1. Controller 的职责边界：
 *    - 充当系统的“总指挥调度”，接收前端 HTTP 请求，校验基本入参。
 *    - 调用下层 Service 处理业务逻辑，获取返回数据。
 *    - 将数据封装为响应结果（JSON 或视图）返回给前端客户端。
 *    - 切记：Controller 层严禁编写繁重的业务计算或直接写 SQL，必须保持精简与清晰。
 * 
 * 2. 常用注解解析：
 *    - @RestController: 等价于 @Controller + @ResponseBody，表明该类所有方法的返回值
 *      都不会经过视图解析器（ViewResolver）渲染为 HTML 页面，而是直接序列化为 JSON 写入 HTTP 响应体。
 *    - @RequestMapping: 定义基础请求路由前缀。
 *    - @GetMapping / @PostMapping: 针对特定 HTTP 方法（GET/POST）的映射缩写。
 *    - @PathVariable: 从请求路径中提取动态变量（RESTful 风格）。
 *    - @RequestParam: 提取 URL query 参数或 application/x-www-form-urlencoded 表单参数。
 *    - @RequestBody: 利用 HttpMessageConverter 将 HTTP Body 中的 JSON 字符串反序列化为 Java 对象。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 根据用户 ID 查询用户信息
     * 请求示例：GET /api/users/1001
     */
    @GetMapping("/{id}")
    public UserVO getUserById(@PathVariable("id") Long id) {
        System.out.println("[Controller 控制层] 接收到查询请求，用户 ID: " + id);
        // 模拟调用底层业务逻辑
        return new UserVO(id, "张三", 25, "zhangsan@example.com");
    }

    /**
     * 根据关键字与分页参数搜索用户
     * 请求示例：GET /api/users/search?keyword=jinlin&page=1
     */
    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword,
                              @RequestParam(value = "page", defaultValue = "1") Integer page) {
        System.out.println("[Controller 控制层] 接收到搜索请求，关键字: " + keyword + "，页码: " + page);
        return "查询成功，关键字: " + keyword + "，当前页: " + page;
    }

    /**
     * 创建新增用户
     * 请求示例：POST /api/users，请求体带有 JSON
     */
    @PostMapping
    public String createUser(@RequestBody UserVO userVO) {
        System.out.println("[Controller 控制层] 接收到创建用户请求: " + userVO);
        return "创建用户成功，用户 ID 为: " + userVO.getId();
    }
}
