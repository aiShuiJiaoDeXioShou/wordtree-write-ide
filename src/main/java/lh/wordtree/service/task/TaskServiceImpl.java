package lh.wordtree.service.task;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.comm.entity.fun.OrdinaryFunction;
import lh.wordtree.task.ITask;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskServiceImpl implements TaskService {
    private final List<OrdinaryFunction> writeTasks = new LinkedList<>();
    private final List<OrdinaryFunction> toggleFileTasks = new LinkedList<>();
    private final List<OrdinaryFunction> loopTasks = new LinkedList<>();
    private final List<OrdinaryFunction> saveTasks = new LinkedList<>();
    private final List<OrdinaryFunction> initTasks = new LinkedList<>();
    private final List<OrdinaryFunction> endTasks = new LinkedList<>();

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
        var taskList = classes.stream()
                // 过滤空对象
                .filter(aClass -> !Objects.isNull(aClass.getAnnotation(Task.class)))
                // 对任务的优先级别进行排序
                .sorted((o1, o2) -> {
                    var o1Task = o1.getAnnotation(Task.class);
                    var o2Task = o2.getAnnotation(Task.class);
                    var v1 = o1Task.value();
                    var v2 = o2Task.value();
                    return v2 - v1;
                })
                // 对对象进行分类
                .peek(aClass -> {
                    Object o = ReflectUtil.newInstance(aClass);
                    var annotation = aClass.getAnnotation(Task.class);
                    var iTask = annotation.iTask();
                    if (o instanceof WTTask wtTask) {
                        switch (iTask) {
                            case LOOP -> loopTasks.add(wtTask::apply);
                            case SAVE -> saveTasks.add(wtTask::apply);
                            case WRITE -> writeTasks.add(wtTask::apply);
                            case TOGGLE_FILE -> toggleFileTasks.add(wtTask::apply);
                            case INIT -> initTasks.add(wtTask::apply);
                            case END -> endTasks.add(wtTask::apply);
                            case NONE -> {
                                loopTasks.add(wtTask::loop);
                                saveTasks.add(wtTask::save);
                                writeTasks.add(wtTask::write);
                                toggleFileTasks.add(wtTask::toggleFile);
                                initTasks.add(wtTask::init);
                                endTasks.add(wtTask::end);
                            }
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
    }

    private void applyTask(List<OrdinaryFunction> tasks) {
        if (tasks.size() > 0)
            tasks.forEach(OrdinaryFunction::apply);
    }

    public List<OrdinaryFunction> getInitTasks() {
        return initTasks;
    }

    public List<OrdinaryFunction> getWriteTasks() {
        return writeTasks;
    }

    public List<OrdinaryFunction> getToggleFileTasks() {
        return toggleFileTasks;
    }

    public List<OrdinaryFunction> getLoopTasks() {
        return loopTasks;
    }

    public List<OrdinaryFunction> getSaveTasks() {
        return saveTasks;
    }

    public List<OrdinaryFunction> getEndTasks() {
        return endTasks;
    }
}
