package com.jinlin.springbootinterview.interview.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Spring AOP 注解切面演示类
 * 
 * 教学解析：
 * 1. 引入了 spring-boot-starter-aop 依赖后，可直接使用 AspectJ 规范的标准注解。
 * 2. 核心注解体系：
 *    - @Aspect: 声明此类为切面。
 *    - @Pointcut: 切入点表达式，指定具体拦截哪些目标方法。
 *    - @Around: 环绕通知，最强大的通知类型，掌控方法执行前、执行后以及异常处理全过程。
 *    - @Before: 前置通知。
 *    - @AfterReturning: 返回通知，在方法成功 return 之后执行。
 *    - @AfterThrowing: 异常通知，在方法抛出异常后执行。
 *    - @After: 最终后置通知，在 finally 代码块中执行。
 */

//声明这个类为切面类
@Aspect
@Component
public class LoggingAspect {

    /**
     * 切入点：匹配 userService 包下所有类的所有方法
     */
    @Pointcut("execution(* com.jinlin.springbootinterview.interview.spring.aop.service..*.*(..))")
    public void userServicePointcut() {
    }

    @Around("userServicePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[Spring AOP @Around] 环绕前置处理：开始计时");
        Object result = joinPoint.proceed();
        System.out.println("[Spring AOP @Around] 环绕后置处理：结束计时");
        return result;
    }

    @Before("userServicePointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("[Spring AOP @Before] 前置通知：准备调用方法 -> " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "userServicePointcut()", returning = "retVal")
    public void afterReturningAdvice(JoinPoint joinPoint, Object retVal) {
        System.out.println("[Spring AOP @AfterReturning] 返回通知：方法正常返回");
    }

    @AfterThrowing(pointcut = "userServicePointcut()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        System.out.println("[Spring AOP @AfterThrowing] 异常通知：捕获到异常 -> " + ex.getMessage());
    }

    @After("userServicePointcut()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("[Spring AOP @After] 最终通知：资源清理释放");
    }
}
