package com.jinlin.springbootinterview.interview.springmvc.handler;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Spring MVC 核心组件：HandlerAdapter 机制模拟与原理解析
 * 
 * 教学解析：
 * 1. 什么是 HandlerAdapter（处理器适配器）？
 *    - 它是请求调用的“执行翻译官”，是【适配器设计模式 (Adapter Pattern)】的经典实现。
 * 
 * 2. 核心面试痛点：为什么不能由 DispatcherServlet 直接调用 Controller，而必须多此一举使用 HandlerAdapter？
 *    - 原因在于：在 Spring MVC 中，Handler（处理器）的形式是【多元化】的，不仅限于 @Controller！
 *      (1) @RequestMapping 标记的方法（HandlerMethod）。
 *      (2) 实现了 HttpRequestHandler 接口的类（如静态资源处理器 ResourceHttpRequestHandler）。
 *      (3) 实现了 Controller 接口的类（早期 Controller）。
 *      (4) 原生标准的 HttpServlet。
 *    - 如果没有 HandlerAdapter，DispatcherServlet 内部就必须充斥着大量的 if-else 判断强转类型，
 *      严重违反了“开闭原则 (OCP)”。
 *    - 引入 HandlerAdapter 后：
 *      每个适配器提供 supports(handler) 方法告诉 DispatcherServlet 自己能否处理该 Handler，
 *      并通过统一的 handle(...) 接口抹平不同 Handler 的执行差异！
 * 
 * 3. 内部核心工作细节（以最核心的 RequestMappingHandlerAdapter 为例）：
 *    (1) 参数解析器 (HandlerMethodArgumentResolver)：
 *        扫描方法形参，把 HTTP 原始字符串自动解析转换为强类型的 Java 参数对象
 *        （解析 @RequestParam, @PathVariable, @RequestBody, HttpServletRequest 等）。
 *    (2) 返回值处理器 (HandlerMethodReturnValueHandler)：
 *        根据方法返回值类型处理响应。如果带了 @ResponseBody，
 *        调用 HttpMessageConverter 转为 JSON 写回客户端；否则包装为 ModelAndView 返回给视图解析器。
 */
public class MiniHandlerAdapter {

    /**
     * 判断当前适配器是否支持处理传入的 handler
     */
    public boolean supports(Object handler) {
        return handler instanceof MiniHandlerMapping.HandlerMethod;
    }

    /**
     * 适配并反射执行目标方法
     * 内部模拟：参数解析、方法调用、返回值处理
     */
    public Object handle(Object handler, Map<String, Object> requestParams) throws Exception {
        MiniHandlerMapping.HandlerMethod handlerMethod = (MiniHandlerMapping.HandlerMethod) handler;
        Object controller = handlerMethod.getControllerBean();
        Method method = handlerMethod.getMethod();

        System.out.println("[HandlerAdapter 适配器] 匹配到 RequestMappingHandlerAdapter，开始参数解析与反射执行");

        // 简易模拟参数解析（实战中由 HandlerMethodArgumentResolver 负责）
        Object[] args = new Object[method.getParameterCount()];
        if (args.length > 0 && requestParams.containsKey("id")) {
            args[0] = requestParams.get("id");
            System.out.println("[参数解析器] 成功将请求参数 id=" + args[0] + " 解析绑定到方法形参");
        }

        // 反射执行 Controller 业务方法
        Object result = method.invoke(controller, args);

        System.out.println("[返回值处理器] 捕获到方法返回值: " + result + "，交由 HttpMessageConverter 序列化为 JSON");
        return result;
    }
}
