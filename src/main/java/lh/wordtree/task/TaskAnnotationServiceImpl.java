package lh.wordtree.task;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.comm.entity.fun.OrdinaryFunction;

import java.util.List;
import java.util.Objects;

/**
 * 利用java反射的方式进行了解析
 */
public class TaskAnnotationServiceImpl implements TaskService {

    public TaskAnnotationServiceImpl() {
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
                    if (o instanceof WTTask wtTask) {
                        if (!annotation.name().isBlank()) tasks.put(annotation.name(), wtTask);
                        loopTasks.add(wtTask::loop);
                        saveTasks.add(wtTask::save);
                        writeTasks.add(wtTask::write);
                        toggleFileTasks.add(wtTask::toggleFile);
                        initTasks.add(wtTask::init);
                        endTasks.add(wtTask::end);
                    }
                })
                .toList();
    }

    public void applyTask(List<OrdinaryFunction> tasks) {
        if (tasks.size() > 0)
            tasks.forEach(OrdinaryFunction::apply);
    }
}
