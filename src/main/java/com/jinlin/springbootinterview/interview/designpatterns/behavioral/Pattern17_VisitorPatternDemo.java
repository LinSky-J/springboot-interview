package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * 17. 访问者模式 (Visitor Pattern)【数据结构与操作解耦与双分派利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    封装一些作用于某种数据结构中的各元素的操作。
 *    它可以在不改变各元素的类的前提下定义作用于这些元素的新操作。
 * 
 * 2. 核心机制：双分派 (Double Dispatch)
 *    - Java 是单分派语言（在编译期静态重载绑定参数类型，运行时动态分派调用者对象类型）。
 *    - 访问者模式巧妙实现了双分派：
 *      第 1 次分派：element.accept(visitor) —— 运行时确定具体元素 (ConcreteElement)；
 *      第 2 次分派：visitor.visit(this) —— 在元素内部把具体类型的 this 传递给访问者，
 *                  让访问者在编译期精确命中对应的 visit(具体元素类型) 重载方法！
 * 
 * 3. 优缺点与适用场景：
 *    - 优点：新增操作极度方便（只需新增一个 ConcreteVisitor，无需修改任何元素类，符合开闭原则）。
 *    - 缺点：新增元素极其困难（一旦在结构中增加一个新元素类型，所有访问者接口和实现类都必须强制改动）。
 *    - 适用条件：元素的数据结构非常稳定，但作用于该结构上的业务操作频繁变动或需要自由扩充。
 * 
 * 4. 经典框架与底层应用：
 *    - Java 编译器 AST 语法树解析：Javac 中的 TreeVisitor
 *    - 字节码操作类库 ASM：ClassVisitor、MethodVisitor、FieldVisitor
 *    - Spring 框架：BeanDefinitionVisitor（用于遍历和解析 BeanDefinition 属性占位符）
 * ============================================================================
 */
public class Pattern17_VisitorPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("访问者模式：企业员工结构（稳定数据）由不同角色（HR / 财务）执行不同操作");
        System.out.println("=================================================");

        // 1. 构建稳定的对象数据结构
        EmployeeRoster roster = new EmployeeRoster();
        roster.addEmployee(new FullTimeEmployee("张工(全职高级架构师)", 30000, 180));
        roster.addEmployee(new FullTimeEmployee("李工(全职资深研发)", 20000, 160));
        roster.addEmployee(new ContractorEmployee("王工(外包技术顾问)", 120, 200));

        // 2. HR 访问者访问该数据结构：统计绩效工时与考勤
        System.out.println("\n--- 访问者 1: HR 部门执行绩效考勤核算 ---");
        DepartmentVisitor hrVisitor = new HrPerformanceVisitor();
        roster.accept(hrVisitor);

        // 3. 财务访问者访问该数据结构：核算个税与薪酬发放
        System.out.println("\n--- 访问者 2: 财务部门执行月度薪资税费发放核算 ---");
        DepartmentVisitor financeVisitor = new FinancePayrollVisitor();
        roster.accept(financeVisitor);
    }
}

/**
 * 抽象访问者角色 (Visitor)：为每一种具体元素声明一个 visit 访问方法
 */
interface DepartmentVisitor {
    void visit(FullTimeEmployee fullTimeEmployee);
    void visit(ContractorEmployee contractorEmployee);
}

/**
 * 抽象元素角色 (Element)：声明 accept 接收访问者的方法
 */
interface EmployeeElement {
    void accept(DepartmentVisitor visitor);
}

/**
 * 具体元素1：全职员工（数据结构固定：固定月薪、月工作工时）
 */
class FullTimeEmployee implements EmployeeElement {
    private final String name;
    private final double monthlySalary;
    private final int workHours;

    public FullTimeEmployee(String name, double monthlySalary, int workHours) {
        this.name = name;
        this.monthlySalary = monthlySalary;
        this.workHours = workHours;
    }

    public String getName() {
        return name;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public int getWorkHours() {
        return workHours;
    }

    @Override
    public void accept(DepartmentVisitor visitor) {
        // 关键点：双分派的核心 —— 把精确类型的 this 传递给访问者
        visitor.visit(this);
    }
}

/**
 * 具体元素2：外包兼职员工（数据结构固定：时薪、工作工时）
 */
class ContractorEmployee implements EmployeeElement {
    private final String name;
    private final double hourlyRate;
    private final int workHours;

    public ContractorEmployee(String name, double hourlyRate, int workHours) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
    }

    public String getName() {
        return name;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getWorkHours() {
        return workHours;
    }

    @Override
    public void accept(DepartmentVisitor visitor) {
        // 双分派：精准重载调用 visit(ContractorEmployee)
        visitor.visit(this);
    }
}

/**
 * 具体访问者1：人力资源部访问者（关注工时和考勤表现）
 */
class HrPerformanceVisitor implements DepartmentVisitor {

    @Override
    public void visit(FullTimeEmployee employee) {
        int standardHours = 176;
        int diff = employee.getWorkHours() - standardHours;
        System.out.println("[HR 审查 - 全职] 员工: " + employee.getName() 
                + ", 本月出勤: " + employee.getWorkHours() + " 小时, 加班时长: " + Math.max(diff, 0) + " 小时");
    }

    @Override
    public void visit(ContractorEmployee employee) {
        System.out.println("[HR 审查 - 外包] 顾问: " + employee.getName() 
                + ", 实际交付核定工时: " + employee.getWorkHours() + " 小时");
    }
}

/**
 * 具体访问者2：财务部访问者（关注薪酬成本与应纳税额）
 */
class FinancePayrollVisitor implements DepartmentVisitor {

    @Override
    public void visit(FullTimeEmployee employee) {
        double salary = employee.getMonthlySalary();
        double tax = salary * 0.1; // 假设税率
        double netSalary = salary - tax;
        System.out.println("[财务核算 - 全职] 员工: " + employee.getName() 
                + ", 应发工资: ￥" + salary + ", 扣除税额: ￥" + tax + ", 实发工资: ￥" + netSalary);
    }

    @Override
    public void visit(ContractorEmployee employee) {
        double pay = employee.getHourlyRate() * employee.getWorkHours();
        double tax = pay * 0.2; // 劳务报酬税
        System.out.println("[财务核算 - 外包] 顾问: " + employee.getName() 
                + ", 劳务总酬劳: ￥" + pay + ", 代扣税金: ￥" + tax + ", 净结算打款: ￥" + (pay - tax));
    }
}

/**
 * 结构对象角色 (ObjectStructure)：管理元素集合，并提供高层遍历方法供访问者访问
 */
class EmployeeRoster {
    private final List<EmployeeElement> employees = new ArrayList<>();

    public void addEmployee(EmployeeElement employee) {
        employees.add(employee);
    }

    public void accept(DepartmentVisitor visitor) {
        for (EmployeeElement employee : employees) {
            employee.accept(visitor);
        }
    }
}
