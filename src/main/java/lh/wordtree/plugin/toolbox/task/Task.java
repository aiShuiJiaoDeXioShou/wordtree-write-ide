package lh.wordtree.plugin.toolbox.task;

public interface Task {
    TaskCycle getTaskCycle();

    TaskAsynch getTaskAsynch();

    // 执行的函数
    void apply();

    // last 在编辑器关闭时候的操作
    default void lastFun() {
    }

    enum TaskCycle {
        PRE, AFTER, WHEN
    }

    enum TaskAsynch {
        LOOP, ONE
    }
}