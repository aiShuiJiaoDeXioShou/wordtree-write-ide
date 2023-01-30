package lh.wordtree.service.task;

import lh.wordtree.task.ITask;
import lh.wordtree.task.WTTask;

import java.util.List;

public interface TaskService {

    TaskService INSTANCE = new TaskServiceImpl();
    String BASE_PACKAGE = "lh.wordtree.task";

    void start(ITask iTask);

    List<WTTask> getWriteTasks();

    List<WTTask> getToggleFileTasks();

    List<WTTask> getLoopTasks();

    List<WTTask> getSaveTasks();
}
