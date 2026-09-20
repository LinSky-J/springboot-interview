package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

/**
 * ============================================================================
 * 12. 迭代器模式 (Iterator Pattern)【建议学，遍历容器标准规范】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    提供一种方法顺序访问一个聚合对象中的各个元素，而又不需要暴露该对象的内部底层数据结构
 *    （无论是数组、双向链表、跳表还是哈希表）。
 * 
 * 2. 为什么需要迭代器模式？
 *    - 统一遍历标准：如果不使用迭代器，遍历 ArrayList 必须用索引 for(int i=0;i<size;i++)，
 *      遍历 LinkedList 必须顺着 Node 节点 next 寻址，遍历 Set 甚至没有下标。
 *    - 迭代器模式将“存储数据”与“遍历数据”的职责彻底分离，对外暴露通用的 hasNext() 和 next() 契约，
 *      Java 语法糖增强 for 循环（foreach）底层完全基于此实现！
 * 
 * 3. 经典工业级应用：
 *    - Java 集合框架体系：java.util.Iterator 接口与 java.lang.Iterable 接口。
 * ============================================================================
 */
public class IteratorPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("迭代器模式：自定义容器屏蔽内部数组细节，统一遍历");
        System.out.println("=================================================");

        CustomNameList list = new CustomNameList();
        list.add("Java");
        list.add("Spring");
        list.add("MyBatis");
        list.add("MySQL");

        // 获取抽象迭代器进行遍历
        CustomIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            System.out.println("遍历读取到元素: " + item);
        }
    }
}

/**
 * 抽象迭代器接口
 */
interface CustomIterator {
    boolean hasNext();
    String next();
}

/**
 * 抽象聚合容器接口
 */
interface CustomAggregate {
    CustomIterator iterator();
}

/**
 * 具体聚合容器实现（内部使用定长数组存储）
 */
class CustomNameList implements CustomAggregate {

    private final String[] elements = new String[10];
    private int size = 0;

    public void add(String element) {
        if (size < elements.length) {
            elements[size++] = element;
        }
    }

    @Override
    public CustomIterator iterator() {
        return new CustomListIterator();
    }

    /**
     * 内部类具体迭代器实现：持有当前游标索引
     */
    private class CustomListIterator implements CustomIterator {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("容器遍历已无更多元素！");
            }
            return elements[cursor++];
        }
    }
}
