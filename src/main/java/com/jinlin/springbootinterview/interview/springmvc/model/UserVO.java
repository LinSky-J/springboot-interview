package com.jinlin.springbootinterview.interview.springmvc.model;

/**
 * MVC 架构中的模型层实体（Model / View Object）
 * 
 * 教学解析：
 * 1. 在传统 MVC 架构中：
 *    - Model (模型)：负责业务数据封装与业务状态承载。
 *    - 既包括持久层的 Entity，也包括传输层的 DTO/VO，为 View（视图）或前端接口提供数据。
 * 2. 在现代前后端分离架构中：
 *    - Model 对象通常直接被 HttpMessageConverter（如 Jackson 的 MappingJackson2HttpMessageConverter）
 *      序列化为 JSON 字符串，通过 HTTP 响应体直接传输给前端。
 */
public class UserVO {

    private Long id;
    private String username;
    private Integer age;
    private String email;

    public UserVO() {
    }

    public UserVO(Long id, String username, Integer age, String email) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
