package com.jinlin.springbootinterview.interview.springmvc.dispatcher;

import com.jinlin.springbootinterview.interview.springmvc.handler.MiniHandlerAdapter;
import com.jinlin.springbootinterview.interview.springmvc.handler.MiniHandlerMapping;

import java.util.Map;

/**
 * Spring MVC 核心中枢：前端控制器 DispatcherServlet 调度机制模拟
 * 
 * 教学解析：
 * 1. 架构定位：
 *    - DispatcherServlet 是 Spring MVC 的“中央总调度器”，采用经典前端控制器模式（Front Controller）。
 *    - 它自身不处理任何具体的业务逻辑，而是统一负责各个核心组件（HandlerMapping、HandlerAdapter、
 *      ViewResolver、ExceptionResolver 等）之间的调度与流转。
 * 
 * 2. 核心调度方法：doDispatch(HttpServletRequest request, HttpServletResponse response)
 *    源码级的经典执行主线全流程在这里一览无余。
 */
public class MiniDispatcherServlet {

    private final MiniHandlerMapping handlerMapping;
    private final MiniHandlerAdapter handlerAdapter;

    public MiniDispatcherServlet(MiniHandlerMapping handlerMapping, MiniHandlerAdapter handlerAdapter) {
        this.handlerMapping = handlerMapping;
        this.handlerAdapter = handlerAdapter;
    }

    /**
     * 核心调度主线：模拟 DispatcherServlet.doDispatch 完整处理流程
     */
    public void doDispatch(String requestUrl, Map<String, Object> requestParams) {
        System.out.println("=================================================================");
        System.out.println("[步骤 1: 接收请求] 前端控制器 DispatcherServlet 接收到 HTTP 请求: " + requestUrl);

        try {
            // 步骤 2 & 3: 调用 HandlerMapping 获取处理器执行链
            System.out.println("[步骤 2 & 3: 路由寻址] 调用 HandlerMapping 查找处理器执行链...");
            MiniHandlerMapping.HandlerExecutionChain executionChain = handlerMapping.getHandler(requestUrl);
            if (executionChain == null) {
                System.out.println("[404 Not Found] 未找到匹配的处理器映射，返回 404");
                return;
            }

            // 步骤 4: 依次执行所有拦截器的 preHandle 前置拦截
            System.out.println("[步骤 4: 拦截器前置处理] 正在按序执行拦截器链的 preHandle()...");
            for (String interceptor : executionChain.getInterceptors()) {
                System.out.println("  -> 拦截器 [" + interceptor + "].preHandle() 放行通过");
            }

            // 步骤 5: 寻找能处理当前 Handler 的 HandlerAdapter
            System.out.println("[步骤 5: 寻找适配器] 寻找匹配的 HandlerAdapter 处理器适配器...");
            Object handler = executionChain.getHandler();
            if (!handlerAdapter.supports(handler)) {
                throw new IllegalStateException("未找到支持该处理器的 HandlerAdapter: " + handler);
            }

            // 步骤 6 & 7: 由适配器执行目标 Controller 方法
            System.out.println("[步骤 6 & 7: 适配执行] HandlerAdapter 执行 Controller 目标方法...");
            Object responseData = handlerAdapter.handle(handler, requestParams);

            // 步骤 8: 依次逆序执行拦截器的 postHandle
            System.out.println("[步骤 8: 拦截器后置处理] 正在逆序执行拦截器链的 postHandle()...");

            // 步骤 9, 10 & 11: 渲染视图或 JSON 响应写回客户端
            System.out.println("[步骤 9-11: 响应输出] 检测到 @ResponseBody，跳过 ViewResolver 渲染，");
            System.out.println("           直接由 HttpMessageConverter 将结果写入 HTTP 响应体: " + responseData);

        } catch (Exception e) {
            System.out.println("[异常处理] 触发 HandlerExceptionResolver 统一异常处理: " + e.getMessage());
        } finally {
            // 步骤 12: 触发拦截器的 afterCompletion 资源清理（在 finally 块中绝对保证执行）
            System.out.println("[步骤 12: 请求收尾] 执行拦截器的 afterCompletion()，释放资源，请求处理完毕。");
            System.out.println("=================================================================\n");
        }
    }
}
