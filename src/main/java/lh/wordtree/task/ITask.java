package lh.wordtree.task;

public enum ITask {

    /**
     * 正在写入
     */
    WRITE,

    /**
     * 切换文件时操作
     */
    TOGGLE_FILE,

    /**
     * 后台任务
     */
    LOOP,

    /**
     * 保存是否操作
     */
    SAVE,

    /**
     * 初始化项目时操作
     */
    INIT,

    /**
     * 在项目关闭之后执行的操作
     */
    END,
    GLOBAL
}
