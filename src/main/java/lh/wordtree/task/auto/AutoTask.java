package lh.wordtree.task.auto;

import lh.wordtree.comm.entity.Figure;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

import java.util.Objects;

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
    public void write(String source) {
        if (FactoryBeanService.nowCodeArea.get() instanceof WTWriterEditor wtWriterEditor) {
            if (source.equals("1")) {
                var figures = FactoryBeanService.roles.get();
                if (Objects.isNull(figures)) return;
                if (figures.size() == 0) return;
                wtWriterEditor.popup().update(figures.stream().map(Figure::getName).toList());
                wtWriterEditor.popup().popupShow();
            } else wtWriterEditor.popup().hide();
        }
    }

    @Override
    public void save() {
        WTTask.super.save();
    }
}
