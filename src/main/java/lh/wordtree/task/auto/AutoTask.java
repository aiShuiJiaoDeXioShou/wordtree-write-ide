package lh.wordtree.task.auto;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.Figure;
import lh.wordtree.editor.WriterEditor;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

import java.util.Comparator;
import java.util.List;
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
        var pattern = new StringBuilder();
        if (BeanFactory.nowCodeArea.get() instanceof WriterEditor writerEditor) {
            if (!source.isBlank() && source.length() < 20) {
                var figures = BeanFactory.roles.get();
                pattern.append(".*").append(source).append(".*");
                if (Objects.isNull(figures)) return;
                if (figures.size() == 0) return;
                List<Label> labels = figures.stream()
                        .map(Figure::getName)
                        .filter(s -> s.matches(pattern.toString()))
                        .sorted(Comparator.naturalOrder())
                        .distinct()
                        .map(Label::new)
                        .peek(label -> {
                            ImageView img = new ImageView(new Image(Config.src("static/icon/关系网.png")));
                            img.setFitHeight(18);
                            img.setFitWidth(18);
                            HBox h = new HBox();
                            h.setAlignment(Pos.CENTER);
                            Label t = new Label("人物");
                            t.setStyle("-fx-background-radius: 5;-fx-font-size: 10;-fx-text-fill: #ffff;-fx-background-color: #69db7c;-fx-padding: 6");
                            h.getChildren().addAll(img, t);
                            h.setSpacing(5);
                            label.setGraphic(h);
                        })
                        .toList();
                if (labels.size() > 0) {
                    writerEditor.popup().update(FXCollections.observableArrayList(labels), source);
                    writerEditor.popup().popupShow();
                }
            } else writerEditor.popup().hide();
        }
    }

    @Override
    public void save() {
        WTTask.super.save();
    }
}
