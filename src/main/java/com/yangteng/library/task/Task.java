package com.yangteng.library.task;

public interface Task {
    TaskCycle getTaskCycle();

    TaskAsynch getTaskAsynch();

    // 执行的函数
    void apply();

    enum TaskCycle {
        PRE, AFTER, WHEN
    }

    enum TaskAsynch {
        LOOP, ONE
    }
}