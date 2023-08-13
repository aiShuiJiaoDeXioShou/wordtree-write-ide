package lh.wordtree.handler;

import lh.wordtree.comm.entity.fun.OrdinaryFunction;
import lh.wordtree.comm.entity.fun.OrdinaryFunction1;
import lh.wordtree.task.ITask;
import lh.wordtree.task.WTTask;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface TaskHandler {
    TaskHandler INSTANCE = new TaskHandlerImpl();
    LinkedHashMap<String, WTTask> tasks = new LinkedHashMap<>();
    List<OrdinaryFunction1<String>> writeTasks = new LinkedList<>();
    List<OrdinaryFunction> toggleFileTasks = new LinkedList<>();
    List<OrdinaryFunction> loopTasks = new LinkedList<>();
    List<OrdinaryFunction> saveTasks = new LinkedList<>();
    List<OrdinaryFunction> initTasks = new LinkedList<>();
    List<OrdinaryFunction> endTasks = new LinkedList<>();

    String BASE_PACKAGE = "lh.wordtree.task";

    default void start(ITask iTask, Object... obj) {
        switch (iTask) {
            case LOOP -> applyTask(loopTasks);
            case SAVE -> applyTask(saveTasks);
            case WRITE -> {
                var writeTasks1 = writeTasks;
                writeTasks1.forEach(fun -> {
                    if (obj[0] instanceof String s)
                        fun.apply(s);
                });
            }
            case TOGGLE_FILE -> applyTask(toggleFileTasks);
            case INIT -> applyTask(initTasks);
            case END -> applyTask(endTasks);
        }
    }

    void applyTask(List<OrdinaryFunction> tasks);
}
