package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * 4. 策略模式 (Strategy Pattern)【必学，重构消除 if-else 核心利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    定义一系列可互换的算法族，把每一个算法分别封装到独立的策略类中，
 *    并让它们实现共同的策略接口。使得算法的变化完全独立于使用它的客户端。
 * 
 * 2. 为什么企业开发极其推崇策略模式？
 *    - 核心价值：彻底干掉恶心冗长、难以维护的成百上千行 if-else / switch-case 坏味道代码！
 *    - 完美遵守开闭原则：当业务新增一种策略（例如新增一种支付渠道、会员折扣、促销打折），
 *      只需新增一个策略实现类，无需改动现有业务主流程的任何代码。
 * 
 * 3. Spring 框架中的策略模式精妙落地：
 *    在 Spring 中，直接通过依赖注入 Map<String, StrategyService> 即可在启动时
 *    自动将所有实现类收集到 Map 中，调用时一行代码根据业务类型直接路由提取，极具生产实用价值！
 * ============================================================================
 */
public class StrategyPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("策略模式：模拟电商会员折扣动态计算（告别 if-else）");
        System.out.println("=================================================");

        // 模拟客户端选择不同会员身份
        DiscountContext context = new DiscountContext();

        double originalPrice = 1000.0;
        System.out.println("普通用户折后价: ¥" + context.calculatePrice("NORMAL", originalPrice));
        System.out.println("VIP会员折后价: ¥" + context.calculatePrice("VIP", originalPrice));
        System.out.println("SVIP会员折后价: ¥" + context.calculatePrice("SVIP", originalPrice));
    }
}

/**
 * 抽象打折策略接口
 */
interface DiscountStrategy {
    double calculate(double originalPrice);
}

/**
 * 具体策略1：普通用户无折扣
 */
class NormalDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice;
    }
}

/**
 * 具体策略2：VIP 会员 9 折
 */
class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice * 0.9;
    }
}

/**
 * 具体策略3：SVIP 会员 8 折
 */
class SvipDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice * 0.8;
    }
}

/**
 * 策略上下文环境（Context）：持有一组策略的路由表
 */
class DiscountContext {

    private final Map<String, DiscountStrategy> strategyMap = new HashMap<>();

    public DiscountContext() {
        // 初始化注册策略（在 Spring 中可通过 @Autowired Map<String, Strategy> 自动注入装配）
        strategyMap.put("NORMAL", new NormalDiscountStrategy());
        strategyMap.put("VIP", new VipDiscountStrategy());
        strategyMap.put("SVIP", new SvipDiscountStrategy());
    }

    /**
     * 根据会员类型动态路由到具体策略，彻底避免 if-else
     */
    public double calculatePrice(String userType, double originalPrice) {
        DiscountStrategy strategy = strategyMap.get(userType);
        if (strategy == null) {
            throw new IllegalArgumentException("未知的会员策略类型: " + userType);
        }
        return strategy.calculate(originalPrice);
    }
}
