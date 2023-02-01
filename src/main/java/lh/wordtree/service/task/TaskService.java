package lh.wordtree.service.task;

import lh.wordtree.entity.fun.OrdinaryFunction;
import lh.wordtree.task.ITask;

import java.util.List;

public interface TaskService {

    TaskService INSTANCE = new TaskServiceImpl();
    String BASE_PACKAGE = "lh.wordtree.task";

    void start(ITask iTask);

    List<OrdinaryFunction> getWriteTasks();

    List<OrdinaryFunction> getToggleFileTasks();

    List<OrdinaryFunction> getLoopTasks();

    List<OrdinaryFunction> getSaveTasks();
}
