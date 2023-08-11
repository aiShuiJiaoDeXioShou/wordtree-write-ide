package lh.wordtree.task;

public interface TaskService {

    TaskService INSTANCE = new TaskServiceImpl();
    String BASE_PACKAGE = "lh.wordtree.task";

    void start(ITask iTask, Object... obj);
}
