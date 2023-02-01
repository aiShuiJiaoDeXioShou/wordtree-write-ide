package lh.wordtree.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Task {
    ITask iTask() default ITask.NONE;

    /**
     * 优先级
     */
    int value() default 0;

    String name() default "";
}
