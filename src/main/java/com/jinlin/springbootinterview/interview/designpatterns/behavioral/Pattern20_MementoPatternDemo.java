package com.jinlin.springbootinterview.interview.designpatterns.behavioral;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * 20. 备忘录模式 (Memento Pattern)【不破坏封装性的快照与状态恢复利器】
 * ============================================================================
 * 
 * [教学核心解析]
 * 1. 核心定义：
 *    在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
 *    这样以后就可将该对象恢复到原先保存的状态。
 * 
 * 2. 三大核心角色及其封装边界：
 *    - 原发器 (Originator)：
 *      拥有需要备份的内部状态，负责创建备忘录快照 save()，以及根据备忘录恢复现场 restore()。
 *    - 备忘录 (Memento)：
 *      存储原发器的内部状态快照。
 *      为了防止数据被外部篡改，备忘录通常对外部只提供窄接口（不可见/不可改），只对原发器提供宽接口。
 *    - 负责人 (Caretaker)：
 *      负责保管备忘录历史栈，但绝不能对备忘录的具体内容进行检查、修改或读取。
 * 
 * 3. 为什么不直接由外部对象调用 Getter/Setter 备份？
 *    - 破坏封装性：原发器的大量私有内部状态被迫公之于众，外部类与原发器的内部实现强耦合。
 *    - 备忘录模式让原发器自己决定备份什么、恢复什么，外部管理者只充当无知的“快照容器”。
 * 
 * 4. 经典工业级与源码应用：
 *    - 文本编辑器/IDE 的代码历史版本回滚与 Ctrl+Z
 *    - 游戏存档系统（Checkpoint / Save & Load）
 *    - 数据库事务中的 Savepoint（保存点）：
 *      java.sql.Connection.setSavepoint() 与 rollback(Savepoint savepoint)
 *    - Spring Web Flow 的会话状态快照恢复
 * ============================================================================
 */
public class Pattern20_MementoPatternDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("备忘录模式：代码文本编辑器（历史快照保存与无损撤回）");
        System.out.println("=================================================");

        // 1. 创建原发器（文本编辑器）与管理者（版本历史记录栈）
        TextEditor editor = new TextEditor();
        EditorHistoryCaretaker history = new EditorHistoryCaretaker();

        // 2. 编写第一版内容并生成快照
        editor.setContent("public class HelloWorld { }");
        System.out.println("当前编辑内容:\n" + editor.getContent());
        history.pushSnapshot(editor.createSnapshot());

        // 3. 编写第二版内容并生成快照
        System.out.println("\n--- 追加第二版代码并备份 ---");
        editor.setContent("public class HelloWorld {\n    public static void main(String[] args) {\n    }\n}");
        System.out.println("当前编辑内容:\n" + editor.getContent());
        history.pushSnapshot(editor.createSnapshot());

        // 4. 编写第三版内容（发生手滑误删或错误修改，尚未备份）
        System.out.println("\n--- 第三版：发生误操作修改 ---");
        editor.setContent("ERROR_SYNTAX_CORRUPTED_CODE_12345");
        System.out.println("当前编辑内容:\n" + editor.getContent());

        // 5. 触发撤销：恢复到第二版
        System.out.println("\n--- 触发第一次撤回 (Ctrl+Z) ---");
        TextEditorMemento snapshot2 = history.popSnapshot();
        if (snapshot2 != null) {
            editor.restoreSnapshot(snapshot2);
        }
        System.out.println("恢复后的内容:\n" + editor.getContent());

        // 6. 再次触发撤销：恢复到第一版
        System.out.println("\n--- 再次触发撤回 (Ctrl+Z) ---");
        TextEditorMemento snapshot1 = history.popSnapshot();
        if (snapshot1 != null) {
            editor.restoreSnapshot(snapshot1);
        }
        System.out.println("恢复后的内容:\n" + editor.getContent());
    }
}

/**
 * 备忘录角色 (Memento)：存储原发器的内部状态快照（不可变只读对象，确保历史不受污染）
 */
class TextEditorMemento {

    private final String content;
    private final long timestamp;

    public TextEditorMemento(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // 仅提供给原发器恢复使用
    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 原发器角色 (Originator)：拥有内部状态，负责生成和根据备忘录恢复状态
 */
class TextEditor {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    // 创建快照备忘录
    public TextEditorMemento createSnapshot() {
        System.out.println("[系统快照] 正在捕获当前文本现场并生成备忘录对象...");
        return new TextEditorMemento(this.content);
    }

    // 根据备忘录恢复内部状态
    public void restoreSnapshot(TextEditorMemento memento) {
        this.content = memento.getContent();
        System.out.println("[系统恢复] 已根据时间戳为 " + memento.getTimestamp() + " 的备忘录成功回滚现场！");
    }
}

/**
 * 负责人角色 (Caretaker)：负责管理备忘录的生命周期与版本栈，对备忘录内部内容一无所知
 */
class EditorHistoryCaretaker {

    private final Deque<TextEditorMemento> historyStack = new ArrayDeque<>();

    public void pushSnapshot(TextEditorMemento memento) {
        historyStack.push(memento);
    }

    public TextEditorMemento popSnapshot() {
        if (historyStack.isEmpty()) {
            System.out.println("[提示] 暂无可恢复的历史快照！");
            return null;
        }
        return historyStack.pop();
    }
}
