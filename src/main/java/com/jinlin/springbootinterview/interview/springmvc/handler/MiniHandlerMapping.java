package com.jinlin.springbootinterview.interview.springmvc.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring MVC 核心组件：HandlerMapping 机制模拟与原理解析
 * 
 * 教学解析：
 * 1. 什么是 HandlerMapping（处理器映射器）？
 *    - 它是请求路由的“导航仪”。
 *    - 核心职责：根据当前前端发来的 HTTP 请求（URL、请求方法 GET/POST、请求头等），
 *      在 Spring 容器预先建立的路由映射表中查找到最匹配的【目标处理器（Handler）】。
 * 
 * 2. 为什么返回的是 HandlerExecutionChain（处理器执行链）而不是单纯的一个类或方法？
 *    - 这是 Spring MVC 精妙的设计之一：
 *      HandlerExecutionChain = 真正的处理器 (Handler) + 该请求命中的所有拦截器列表 (List<HandlerInterceptor>)。
 *    - 这样设计可以让 DispatcherServlet 在执行真正的 Controller 方法前后，
 *      极其方便地依次遍历执行拦截器的 preHandle()、postHandle() 和 afterCompletion()。
 * 
 * 3. 常见实现类：
 *    - RequestMappingHandlerMapping（最常用）：负责解析 @RequestMapping、@GetMapping 等注解建立的映射。
 *    - BeanNameUrlHandlerMapping（较老）：将 Bean 的名称作为 URL 进行路由。
 */
public class MiniHandlerMapping {

    /**
     * 处理器执行链：包含目标处理器与拦截器
     */
    public static class HandlerExecutionChain {
        private final Object handler; // 通常为封装了 Controller 实例和 Method 的 HandlerMethod
        private final List<String> interceptors = new ArrayList<>();

        public HandlerExecutionChain(Object handler) {
            this.handler = handler;
        }

        public Object getHandler() {
            return handler;
        }

        public void addInterceptor(String interceptorName) {
            this.interceptors.add(interceptorName);
        }

        public List<String> getInterceptors() {
            return interceptors;
        }
    }

    /**
     * 封装目标类和目标方法的处理器元数据（对应 Spring 的 HandlerMethod）
     */
    public static class HandlerMethod {
        private final Object controllerBean;
        private final Method method;

        public HandlerMethod(Object controllerBean, Method method) {
            this.controllerBean = controllerBean;
            this.method = method;
        }

        public Object getControllerBean() {
            return controllerBean;
        }

        public Method getMethod() {
            return method;
        }
    }

    // 模拟内部注册表：URL -> HandlerMethod
    private final Map<String, HandlerMethod> urlRegistry = new HashMap<>();

    public void registerHandler(String url, Object controllerBean, Method method) {
        urlRegistry.put(url, new HandlerMethod(controllerBean, method));
    }

    /**
     * 查找请求对应的处理器执行链
     */
    public HandlerExecutionChain getHandler(String requestUrl) {
        HandlerMethod handlerMethod = urlRegistry.get(requestUrl);
        if (handlerMethod == null) {
            return null;
        }
        HandlerExecutionChain chain = new HandlerExecutionChain(handlerMethod);
        // 模拟装载全局通用拦截器
        chain.addInterceptor("SecurityAuthInterceptor");
        chain.addInterceptor("LoggingTraceInterceptor");
        return chain;
    }
}
