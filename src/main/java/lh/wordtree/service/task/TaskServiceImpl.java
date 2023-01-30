package lh.wordtree.service.task;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.task.ITask;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskServiceImpl implements TaskService {
    private final List<WTTask> writeTasks = new LinkedList<>();
    private final List<WTTask> toggleFileTasks = new LinkedList<>();
    private final List<WTTask> loopTasks = new LinkedList<>();
    private final List<WTTask> saveTasks = new LinkedList<>();
    private final List<WTTask> initTasks = new LinkedList<>();
    private final List<WTTask> endTasks = new LinkedList<>();
    private final List<WTTask> globalTasks = new LinkedList<>();

    public TaskServiceImpl() {
        annotationParse();
    }

    /**
     * 注解解释器
     */
    private void annotationParse() {
        // 获取base包下所有的类,扫描注解
        var classes = ClassUtil.scanPackage(BASE_PACKAGE);
        // 判断是否包含Task注解，如果包含Task注解则保留，如果不包含则过滤掉
        var wtTasks = classes.stream()
                // 过滤空对象
                .filter(aClass -> !Objects.isNull(aClass.getAnnotation(Task.class)))
                // 对对象进行分类
                .peek(aClass -> {
                    Object o = ReflectUtil.newInstance(aClass);
                    var annotation = aClass.getAnnotation(Task.class);
                    var iTask = annotation.iTask();
                    if (o instanceof WTTask wtTask) {
                        switch (iTask) {
                            case LOOP -> loopTasks.add(wtTask);
                            case SAVE -> saveTasks.add(wtTask);
                            case WRITE -> writeTasks.add(wtTask);
                            case TOGGLE_FILE -> toggleFileTasks.add(wtTask);
                            case INIT -> initTasks.add(wtTask);
                            case END -> endTasks.add(wtTask);
                            case NONE -> globalTasks.add(wtTask);
                        }
                    }
                })
                .toList();
    }

    public void start(ITask iTask) {
        switch (iTask) {
            case LOOP -> applyTask(getLoopTasks());
            case SAVE -> applyTask(getSaveTasks());
            case WRITE -> applyTask(getWriteTasks());
            case TOGGLE_FILE -> applyTask(getToggleFileTasks());
            case INIT -> applyTask(getInitTasks());
            case END -> applyTask(getEndTasks());
        }
        globalTasksApply(iTask);
    }

    private void applyTask(List<WTTask> tasks) {
        if (tasks.size() > 0)
            tasks.forEach(WTTask::apply);
    }

    private void globalTasksApply(ITask iTask) {
        if (globalTasks.size() > 0)
            switch (iTask) {
                case LOOP -> globalTasks.forEach(WTTask::loop);
                case SAVE -> globalTasks.forEach(WTTask::save);
                case WRITE -> globalTasks.forEach(WTTask::write);
                case TOGGLE_FILE -> globalTasks.forEach(WTTask::toggleFile);
                case INIT -> globalTasks.forEach(WTTask::init);
                case END -> globalTasks.forEach(WTTask::end);
            }
    }

    public List<WTTask> getInitTasks() {
        return initTasks;
    }

    public List<WTTask> getWriteTasks() {
        return writeTasks;
    }

    public List<WTTask> getToggleFileTasks() {
        return toggleFileTasks;
    }

    public List<WTTask> getLoopTasks() {
        return loopTasks;
    }

    public List<WTTask> getSaveTasks() {
        return saveTasks;
    }

    public List<WTTask> getEndTasks() {
        return endTasks;
    }
}
