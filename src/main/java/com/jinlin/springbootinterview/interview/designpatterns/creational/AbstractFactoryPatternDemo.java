package com.jinlin.springbootinterview.interview.designpatterns.creational;

/**
 * ============================================================================
 * 9. 抽象工厂模式 (Abstract Factory Pattern)【建议学，产品族维度创建】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    提供一个创建【一系列相关或相互依赖对象（即同一个产品族）】的抽象接口，
 *    而无需指定它们具体的类。
 * 
 * 2. 工厂方法模式 vs 抽象工厂模式的本质区别：
 *    - 工厂方法模式：针对【单一产品等级结构】（例如只有 Payment 接口，不同工厂生产不同 Payment）。
 *    - 抽象工厂模式：针对【多维度的产品族（Product Family）】！
 *      例如跨平台 UI 组件库：
 *      * Windows 产品族：包含 WindowsButton、WindowsTextBox 等一系列成套组件。
 *      * Mac 产品族：包含 MacButton、MacTextBox 等一系列成套组件。
 *    - 抽象工厂能够保证客户端使用的是同一个产品族内的相关组件，防止风格混搭冲突！
 * ============================================================================
 */
public class AbstractFactoryPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("抽象工厂模式：跨平台产品族组件渲染演示");
        System.out.println("=================================================");

        // 客户端在 Windows 环境下：指定使用 Windows 组件族工厂
        UiComponentFactory winFactory = new WindowsUiFactory();
        Button winBtn = winFactory.createButton();
        TextBox winTxt = winFactory.createTextBox();
        winBtn.render();
        winTxt.render();

        System.out.println();

        // 客户端在 macOS 环境下：切换为 Mac 组件族工厂
        UiComponentFactory macFactory = new MacUiFactory();
        Button macBtn = macFactory.createButton();
        TextBox macTxt = macFactory.createTextBox();
        macBtn.render();
        macTxt.render();
    }
}

/**
 * 抽象产品等级1：按钮
 */
interface Button {
    void render();
}

/**
 * 抽象产品等级2：文本输入框
 */
interface TextBox {
    void render();
}

/**
 * Windows 按钮
 */
class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("[Windows 风格] 渲染直角边框质感按钮");
    }
}

/**
 * Windows 文本框
 */
class WindowsTextBox implements TextBox {
    @Override
    public void render() {
        System.out.println("[Windows 风格] 渲染标准文本输入框");
    }
}

/**
 * Mac 按钮
 */
class MacButton implements Button {
    @Override
    public void render() {
        System.out.println("[macOS 风格] 渲染磨砂圆角渐变按钮");
    }
}

/**
 * Mac 文本框
 */
class MacTextBox implements TextBox {
    @Override
    public void render() {
        System.out.println("[macOS 风格] 渲染高斯模糊透光文本框");
    }
}

/**
 * 抽象工厂：定义一个产品族的全部产品制造规范
 */
interface UiComponentFactory {
    Button createButton();
    TextBox createTextBox();
}

/**
 * 具体工厂1：专门制造 Windows 产品族
 */
class WindowsUiFactory implements UiComponentFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public TextBox createTextBox() {
        return new WindowsTextBox();
    }
}

/**
 * 具体工厂2：专门制造 macOS 产品族
 */
class MacUiFactory implements UiComponentFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public TextBox createTextBox() {
        return new MacTextBox();
    }
}
