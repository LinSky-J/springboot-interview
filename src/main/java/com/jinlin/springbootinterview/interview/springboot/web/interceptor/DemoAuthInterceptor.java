package com.jinlin.springbootinterview.interview.springboot.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC 拦截器规范实现（HandlerInterceptor）
 * 
 * 教学解析：
 * 1. 核心三大方法：
 *    - preHandle(): 在进入 Controller 目标方法之前执行。
 *      返回 true：放行继续执行；返回 false：中断请求流程，可直接通过 response 输出拦截信息。
 *    - postHandle(): 在 Controller 目标方法正常执行完成、返回 ModelAndView 之后执行。
 *      注意：如果 Controller 抛出未捕获异常，或者使用了 @ResponseBody，此方法不会执行或无视图可改。
 *    - afterCompletion(): 无论请求是正常返回还是抛出异常，都会在 finally 块中绝对执行。
 *      常用于清除当前线程绑定的 ThreadLocal 变量（如当前登录用户信息），防止线程池复用导致的内存泄漏或数据串号！
 * 
 * 2. 独有优势：
 *    - Object handler 形参可以强转为 HandlerMethod，
 *      从而反射获取方法上标注的自定义业务注解（如 @RequireLogin, @RateLimiter）。
 */
@Component
public class DemoAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[Spring 拦截器 preHandle] 正在校验请求权限: " + request.getRequestURI());

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            System.out.println("  -> 目标 Controller 类: " + handlerMethod.getBeanType().getSimpleName());
            System.out.println("  -> 目标方法名: " + handlerMethod.getMethod().getName());
        }

        // 模拟放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("[Spring 拦截器 postHandle] Controller 执行完成，后置逻辑触发");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("[Spring 拦截器 afterCompletion] 请求完整处理收尾，清理 ThreadLocal 资源");
        if (ex != null) {
            System.out.println("  -> 记录请求过程中捕获的未处理异常: " + ex.getMessage());
        }
    }
}
