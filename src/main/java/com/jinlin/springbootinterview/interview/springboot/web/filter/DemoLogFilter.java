package com.jinlin.springbootinterview.interview.springboot.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Servlet 过滤器规范实现（Filter）
 * 
 * 教学解析：过滤器 (Filter) 与 拦截器 (Interceptor) 的本质区别（高频必考题）
 * 
 * 1. 规范来源与依赖：
 *    - Filter (过滤器)：基于 Java EE (Jakarta EE) Servlet 规范，属于 Servlet 容器级别（如 Tomcat/Undertow）。
 *    - Interceptor (拦截器)：基于 Spring 框架体系，属于 Spring MVC 框架级别。
 * 
 * 2. 触发时机与执行层级：
 *    - 请求流转顺序：
 *      客户端请求 -> 过滤器 (Filter) -> DispatcherServlet -> 拦截器 (Interceptor) -> Controller 业务方法。
 *    - 过滤器位于最外层，先于 DispatcherServlet 触发；
 *    - 拦截器位于内层，由 DispatcherServlet 驱动执行。
 * 
 * 3. 容器感知能力与精度：
 *    - Filter 只能拿到底层的 ServletRequest / ServletResponse，无法感知当前请求究竟会被
 *      哪一个具体的 Controller 类的哪一个 Method 处理。
 *    - Interceptor 可以直接拿到 HandlerMethod 对象，能反射读取方法上的自定义业务注解，精度极高。
 * 
 * 4. 常见应用场景划分：
 *    - Filter 适合：全局字符编码转换（CharacterEncodingFilter）、CORS 跨域头设置、
 *      敏感词粗粒度过滤、请求内容包装（ContentCachingRequestWrapper）。
 *    - Interceptor 适合：登录状态拦截与 Token 校验、接口方法维度的权限拦截、
 *      接口耗时埋点统计、防重复提交校验。
 */
@Component
public class DemoLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[Servlet 过滤器] DemoLogFilter 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();
        System.out.println("[Servlet 过滤器-前置处理] 捕获到原始 HTTP 请求 URI: " + httpRequest.getRequestURI());

        // 放行请求，流向下一个 Filter 或 DispatcherServlet
        chain.doFilter(request, response);

        long cost = System.currentTimeMillis() - startTime;
        System.out.println("[Servlet 过滤器-后置处理] 请求 URI: " + httpRequest.getRequestURI() + " 处理结束，耗时: " + cost + "ms");
    }

    @Override
    public void destroy() {
        System.out.println("[Servlet 过滤器] DemoLogFilter 销毁");
    }
}
