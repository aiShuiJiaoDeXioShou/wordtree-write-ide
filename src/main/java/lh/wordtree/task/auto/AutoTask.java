package lh.wordtree.task.auto;

import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

@Task(name = "后台记录任务", value = -1)
public class AutoTask implements WTTask {

    @Override
    public void init() {
        WTTask.super.init();
    }

    @Override
    public void end() {
        WTTask.super.end();
    }

    @Override
    public void write() {
        WTTask.super.write();
    }

    @Override
    public void save() {
        WTTask.super.save();
    }
}
