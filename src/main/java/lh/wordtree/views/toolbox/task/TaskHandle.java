package lh.wordtree.views.toolbox.task;

import cn.hutool.core.util.ClassUtil;

import java.util.Set;

public class TaskHandle {

    private final Set<Class<?>> classes;

    public TaskHandle(String packagePath) {
        // 获取Task接口所有的实现
        // 因为用反射性能会大大的降低，所以我们还是手动new出来吧（要么指定包也行）
        classes = ClassUtil.scanPackage(packagePath);
        classes.forEach(aClass -> {

        });
    }

}
