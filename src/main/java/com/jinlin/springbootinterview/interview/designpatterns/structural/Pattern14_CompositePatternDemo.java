package com.jinlin.springbootinterview.interview.designpatterns.structural;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * 14. 组合模式 (Composite Pattern)【树形结构与部分-整体统一处理利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    将对象组合成树形结构以表示“部分-整体”的层次结构。
 *    组合模式使得用户对单个对象（叶子节点）和组合对象（树枝容器节点）的使用具有一致性。
 * 
 * 2. 核心痛点与解决：
 *    - 痛点：如果文件夹和文件作为完全不同的类处理，客户端遍历文件树时需要频繁用 if-else / instanceof 区分：
 *      如果是文件夹就递归展开，如果是文件就直接输出，代码臃肿耦合。
 *    - 解决：让文件夹（Composite）与文件（Leaf）实现统一的抽象组件接口（Component）。
 *      调用者统一执行 print()，文件夹内部自动递归调用子节点的 print()，极大简化客户端逻辑。
 * 
 * 3. 典型应用场景：
 *    - 组织架构树（总公司、分公司、部门）
 *    - 目录文件系统（文件夹、具体文件）
 *    - UI 渲染组件树（Container 包含 Panel，Panel 包含 Button）
 *    - 源码与框架参考：
 *      * Spring MVC 中的 HandlerMethodArgumentResolverComposite（组合参数解析器）
 *      * Jackson 中的 JsonNode（ContainerNode / ObjectNode vs ValueNode / TextNode）
 *      * Java AWT 中的 Component 与 Container
 * ============================================================================
 */
public class Pattern14_CompositePatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("组合模式：企业组织架构树（统一处理总公司、分公司与部门）");
        System.out.println("=================================================");

        // 1. 创建总公司根节点
        CompositeOrganization rootCompany = new CompositeOrganization("集团北京总部");
        rootCompany.add(new LeafDepartment("集团总务行政部"));
        rootCompany.add(new LeafDepartment("集团核心技术研发中心"));

        // 2. 创建华东区分公司
        CompositeOrganization eastBranch = new CompositeOrganization("上海华东分公司");
        eastBranch.add(new LeafDepartment("华东大客户销售部"));
        eastBranch.add(new LeafDepartment("华东前端交付部"));

        // 3. 创建华南区分公司及办事处
        CompositeOrganization southBranch = new CompositeOrganization("深圳华南分公司");
        southBranch.add(new LeafDepartment("华南跨境电商研发组"));

        CompositeOrganization gzOffice = new CompositeOrganization("广州临时办事处");
        gzOffice.add(new LeafDepartment("本地地推运营组"));
        southBranch.add(gzOffice);

        // 4. 将分公司组装挂载到集团总部树中
        rootCompany.add(eastBranch);
        rootCompany.add(southBranch);

        // 5. 客户端统一展示组织树（一致性调用，调用者完全无需感知叶子与分支的差异）
        rootCompany.display(1);
    }
}

/**
 * 抽象组件角色 (Component)：定义组合中所有对象的公共接口和默认行为
 */
abstract class OrganizationComponent {
    protected String name;

    public OrganizationComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 针对容器节点提供的增删操作（叶子节点默认不支持）
    public void add(OrganizationComponent component) {
        throw new UnsupportedOperationException("叶子节点不支持添加子节点");
    }

    public void remove(OrganizationComponent component) {
        throw new UnsupportedOperationException("叶子节点不支持移除子节点");
    }

    // 业务统一抽象方法
    public abstract void display(int depth);
}

/**
 * 叶子节点 (Leaf)：没有子节点的末端个体对象（如具体的业务部门）
 */
class LeafDepartment extends OrganizationComponent {

    public LeafDepartment(String name) {
        super(name);
    }

    @Override
    public void display(int depth) {
        // 根据层级打印缩进线
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  |- ");
        }
        System.out.println(indent + "[部门] " + name);
    }
}

/**
 * 复合容器节点 (Composite)：包含子组件的容器对象（如包含下属部门与分支机构的总公司、分公司）
 */
class CompositeOrganization extends OrganizationComponent {

    // 聚合持有组件接口的集合（包含叶子或更低一级的组合节点）
    private final List<OrganizationComponent> children = new ArrayList<>();

    public CompositeOrganization(String name) {
        super(name);
    }

    @Override
    public void add(OrganizationComponent component) {
        children.add(component);
    }

    @Override
    public void remove(OrganizationComponent component) {
        children.remove(component);
    }

    @Override
    public void display(int depth) {
        // 打印自身信息
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  +-- ");
        }
        System.out.println(indent + "[机构单位] " + name);

        // 递归统一调用所有子节点的 display 方法
        for (OrganizationComponent child : children) {
            child.display(depth + 1);
        }
    }
}
