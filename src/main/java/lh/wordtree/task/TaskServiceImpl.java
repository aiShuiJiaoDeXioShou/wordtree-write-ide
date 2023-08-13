package lh.wordtree.task;

import lh.wordtree.comm.entity.fun.OrdinaryFunction;

import java.util.List;

/**
 * 手动注册的任务
 */
public class TaskServiceImpl implements TaskService {



    public TaskServiceImpl() {
        sign(new WorkTask());
        sign(new InitTask());
        sign(new AutoTask());
        sign(new TimerTask());
        sign(new FileBackground());
        this.init();
    }

    public void init() {
        tasks.forEach((s, wtTask) -> {
            loopTasks.add(wtTask::loop);
            saveTasks.add(wtTask::save);
            writeTasks.add(wtTask::write);
            toggleFileTasks.add(wtTask::toggleFile);
            initTasks.add(wtTask::init);
            endTasks.add(wtTask::end);
        });
    }

    public void sign(WTTask task) {
        tasks.put(getTaskName(task), task);
    }

    private String getTaskName(Object task) {
        return task.getClass().getAnnotation(Task.class).name();
    }

    public void applyTask(List<OrdinaryFunction> tasks) {
        if (tasks.size() > 0)
            tasks.forEach(OrdinaryFunction::apply);
    }
}
