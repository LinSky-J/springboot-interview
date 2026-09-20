package com.jinlin.springbootinterview.interview.springmvc;

import com.jinlin.springbootinterview.interview.springmvc.controller.UserController;
import com.jinlin.springbootinterview.interview.springmvc.dispatcher.MiniDispatcherServlet;
import com.jinlin.springbootinterview.interview.springmvc.handler.MiniHandlerAdapter;
import com.jinlin.springbootinterview.interview.springmvc.handler.MiniHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Spring MVC 核心知识点全景题解与运行验证中心
 * ============================================================================
 * 
 * 本模块涵盖的核心面试问题如下：
 * 1. MVC 分层架构介绍一下？
 * 2. 详细聊聊你对 Spring MVC 请求核心处理流程的理解？
 * 3. 核心组件 HandlerMapping 和 HandlerAdapter 有了解吗？分别起什么作用？
 * 
 * ============================================================================
 * [面试核心考点深度教学]
 * 
 * 一、MVC 分层架构深度解析
 * 1. 概念定义：
 *    MVC (Model-View-Controller) 是一种经典的软件设计典范，用一种业务逻辑、数据、界面显示分离的方法组织代码。
 * 2. 核心三层职责边界：
 *    - Model (模型层)：
 *      负责封装应用状态与业务数据。包含 Entity（数据库实体）、DTO（数据传输对象）、VO（视图显示对象）。
 *      在实际项目中，业务逻辑层（Service）和数据访问层（DAO）共同构成了广义的模型层。
 *    - View (视图层)：
 *      负责向用户展示界面与数据。在传统 JSP/Thymeleaf 时代，View 负责将 Model 数据渲染进 HTML 模板；
 *      在现代前后端分离时代，View 层已移交前端（Vue/React），后端返回的 JSON 数据即为视图数据源。
 *    - Controller (控制器层)：
 *      充当调度中枢。接收用户发起的 HTTP 请求，提取解析请求参数，调用下层业务逻辑服务（Service），
 *      最后决定把数据交给哪个视图或直接将数据以 JSON 格式返回给前端。
 * 3. 核心优势：
 *    高重用性、低耦合、职责单一、便于多人并行开发与后期维护。
 * 
 * 二、Spring MVC 核心请求处理流程（高频必考题，11 步标准推导）
 * 1. 客户端发送 HTTP 请求至前端控制器 DispatcherServlet。
 * 2. DispatcherServlet 收到请求后，调用 HandlerMapping（处理器映射器）。
 * 3. HandlerMapping 根据请求的 URL、Method、Header 等条件查找匹配的目标处理器，
 *    并将处理器与拦截器组合成一个 HandlerExecutionChain（处理器执行链）返回给 DispatcherServlet。
 * 4. DispatcherServlet 依次遍历执行 HandlerExecutionChain 中所有拦截器的 preHandle() 方法（权限、鉴权拦截）。
 * 5. DispatcherServlet 根据 Handler 类型找到对应的 HandlerAdapter（处理器适配器）。
 * 6. HandlerAdapter 内部通过 HandlerMethodArgumentResolver（参数解析器）将 HTTP 请求参数
 *    绑定到形参上，随后反射调用真正的目标 Controller 方法。
 * 7. Controller 执行完毕后返回结果。
 * 8. 如果是传统页面开发，Controller 返回 ModelAndView；
 *    如果是现代前后端分离开发（带 @ResponseBody / @RestController），
 *    HandlerMethodReturnValueHandler 激活 HttpMessageConverter（如 Jackson），
 *    直接将对象序列化为 JSON 字符串写入 HttpServletResponse 输出流，跳过后续视图解析阶段。
 * 9. DispatcherServlet 依次逆序执行所有拦截器的 postHandle() 方法。
 * 10. 如果返回了视图名称，DispatcherServlet 调用 ViewResolver（视图解析器）将逻辑视图名解析为真实 View 对象。
 * 11. View 负责将 Model 中的数据渲染到模板中，生成 HTML 返回给客户端。
 * 12. 最后在 finally 块中绝对执行拦截器的 afterCompletion() 方法，用于请求耗时统计与 ThreadLocal 资源清理。
 * 
 * 三、HandlerMapping 与 HandlerAdapter 的深度原理解析
 * 1. 为什么不能由 DispatcherServlet 直接根据 URL 调用 Controller 方法，而必须多加一层 HandlerAdapter？
 *    - 核心原因在于【适配器设计模式】的运用。
 *    - 在 Spring MVC 中，Handler（处理器）的类型极其丰富多样：
 *      * @RequestMapping 注解的方法（底层为 HandlerMethod 对象）。
 *      * 实现了 HttpRequestHandler 接口的类（例如静态资源处理器 ResourceHttpRequestHandler）。
 *      * 实现了 Controller 接口的类（早期旧接口）。
 *      * 标准的原生 Servlet。
 *    - 不同的 Handler，其调用方式和方法签名完全不同！
 *    - 如果让 DispatcherServlet 直接调用，就必须写大量硬编码的 if-else 强转逻辑，违反开闭原则。
 *    - 引入 HandlerAdapter 后：
 *      每个适配器通过 supports(handler) 判断能否处理该类型的处理器，
 *      通过统一的 handle(...) 方法抹平了各类处理器的调用差异，极大增强了框架的灵活性与可扩展性！
 * 
 * 2. 常用核心实现类：
 *    - RequestMappingHandlerMapping: 负责扫描解析 @Controller/@RequestMapping 注解建立路由表。
 *    - RequestMappingHandlerAdapter: 负责执行 @RequestMapping 标记的方法，内置参数解析与返回值处理。
 * ============================================================================
 */
public class SpringMvcInterviewApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("=================================================================");
        System.out.println("Spring MVC 核心流程调度模拟验证");
        System.out.println("=================================================================\n");

        // 1. 初始化核心组件
        MiniHandlerMapping handlerMapping = new MiniHandlerMapping();
        MiniHandlerAdapter handlerAdapter = new MiniHandlerAdapter();
        MiniDispatcherServlet dispatcherServlet = new MiniDispatcherServlet(handlerMapping, handlerAdapter);

        // 2. 模拟 Spring 容器启动时，RequestMappingHandlerMapping 扫描注册 Controller 方法
        UserController userController = new UserController();
        Method getUserMethod = UserController.class.getMethod("getUserById", Long.class);
        handlerMapping.registerHandler("/api/users/1001", userController, getUserMethod);

        // 3. 模拟接收前端客户端请求并驱动 doDispatch 核心执行主线
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", 1001L);

        // 触发调度
        dispatcherServlet.doDispatch("/api/users/1001", requestParams);
    }
}
