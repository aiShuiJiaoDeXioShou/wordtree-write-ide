package lh.wordtree.service.task;

import lh.wordtree.comm.entity.fun.OrdinaryFunction;
import lh.wordtree.comm.entity.fun.OrdinaryFunction1;
import lh.wordtree.task.ITask;

import java.util.List;

public interface TaskService {

    TaskService INSTANCE = new TaskServiceImpl();
    String BASE_PACKAGE = "lh.wordtree.task";

    void start(ITask iTask, Object... obj);

    List<OrdinaryFunction1<String>> getWriteTasks();

    List<OrdinaryFunction> getToggleFileTasks();

    List<OrdinaryFunction> getLoopTasks();

    List<OrdinaryFunction> getSaveTasks();

    List<OrdinaryFunction> getInitTasks();

    List<OrdinaryFunction> getEndTasks();
}
