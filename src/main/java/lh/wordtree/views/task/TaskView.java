package lh.wordtree.views.task;

import cn.hutool.core.date.LocalDateTimeUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.ui.controls.WTInputPro;
import lh.wordtree.ui.utils.Config;
import lh.wordtree.views.core.NoteCoreView;

import java.time.LocalDateTime;

public class TaskView extends VBox {
    private static final AuthorTaskMapper authorTaskMapper = new AuthorTaskMapperImpl();
    private static ListView<ItemBox> labelListView;
    private SimpleObjectProperty<ShowBox> showBox = new SimpleObjectProperty<>();

    public TaskView() {
        this.view();
        this.controller();
    }

    private void view() {
        var authorTasks = authorTaskMapper.selectAll();
        labelListView = new ListView<>();
        {
            labelListView.prefHeight(NoteCoreView.HEIGHT);
            // 查询该用户所有创建过的任务
            authorTasks.forEach(authorTask -> {
                labelListView.getItems().add(new ItemBox(authorTask));
            });
        }
        if (authorTasks.size() > 0) {
            showBox.set(new ShowBox(authorTasks.get(0)));
            if (this.getChildren().size() > 0) {
                // 删除所有的根节点
                this.getChildren().removeIf(node -> true);
            }
        } else {
            showBox.set(new ShowBox(null));
        }
        // 重新添加或者第一次添加
        this.getChildren().addAll(showBox.get(), labelListView);
    }

    private void controller() {
        labelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showBox.set(new ShowBox(newValue.task()));
        });
        showBox.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.getChildren().remove(0);
                this.getChildren().add(0, newValue);
            }
        });
    }

    private static class ShowBox extends BorderPane {
        CpButtonItem edit = new CpButtonItem("\uE70F", "写作");

        public ShowBox(AuthorTask autoTask) {
            edit.setOnMouseClicked(e -> {
                var pane = new AddPane();
                var window = new Stage();
                var scene = new Scene(pane, 500, 500);
                Config.setStyle(scene);
                window.setScene(scene);
                window.show();
            });
            if (autoTask == null) {
                this.setCenter(edit);
                return;
            }
            this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFF"), null, null)));
            var startLabel = new Label();
            String startStr = LocalDateTimeUtil.format(autoTask.getStartDateTime(), "yyyy-MM-dd hh");
            String endStr = LocalDateTimeUtil.format(autoTask.getEndDateTime(), "yyyy-MM-dd hh");
            startLabel.setText(startStr + " -> " + endStr);
            var top = new HBox();
            top.getChildren().addAll(startLabel, edit);
            top.setSpacing(20);

            Label remember = new Label("不积跬步无以至千里，不积小流五以成江海。");
            remember.setStyle("-fx-text-fill: #cccc;-fx-font-size: 10;");
            var describe = new Label(autoTask.getDescribe());
            describe.setMinWidth(200);
            var hour = autoTask.getEndDateTime().getHour() - autoTask.getStartDateTime().getHour();
            var isOk = "未完成";
            if (autoTask.getComplete() != null) {
                isOk = autoTask.getComplete().equals("true") ? "已完成" : "未完成";
            }
            var time = new Label(hour + " h\n" + isOk);
            time.setStyle("-fx-text-fill: red;-fx-font-size: 20;-fx-text-alignment: center;-fx-max-width: 100");
            time.setWrapText(true);
            var box = new HBox();
            box.getChildren().addAll(describe, time);
            box.setPadding(new Insets(15, 0, 15, 0));

            this.setPadding(new Insets(10));
            this.setTop(top);
            this.setCenter(box);
            this.setBottom(remember);
        }

    }

    private static class AddPane extends BorderPane {
        public AddPane() {
            var center = new VBox();
            var endTime = new WTInputPro("结束时间：");
            var name = new WTInputPro("任务名称：");
            var der = new WTInputPro("任务描述:");
            var number = new WTInputPro("目标字数：");
            var isMandatory = new WTInputPro("是否采用强制手段：");
            center.setSpacing(15);
            center.getChildren().addAll(endTime, name, der, number, isMandatory);
            this.setCenter(center);
            var save = new Button("保存");
            var bottm = new BorderPane();
            bottm.setRight(save);
            this.setBottom(bottm);
            save.setOnMouseClicked(e -> {
                var authorTask = new AuthorTask();
                authorTask.setNumber(Integer.parseInt(number.getTextField().getText()));
                authorTask.setEndDateTime(endTime.getTextField().getText());
                authorTask.setMandatory(isMandatory.getTextField().getText());
                authorTask.setStartDateTime(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd hh:mm:ss"));
                authorTask.setDescribe(der.getTextField().getText());
                authorTask.setComplete("false");
                authorTask.setAuthorName(name.getTextField().getText());
                authorTaskMapper.insertAuthorTask(authorTask);
                labelListView.getItems().add(new ItemBox(authorTask));
                var window = (Stage) this.getScene().getWindow();
                window.close();
            });

            this.setPadding(new Insets(15));
        }
    }

    private static class ItemBox extends HBox {
        private SimpleBooleanProperty isBool = new SimpleBooleanProperty(true);
        private AuthorTask task;

        public ItemBox(AuthorTask task) {
            this.task = task;
            view();
            controller();
        }

        public AuthorTask task() {
            return task;
        }

        public void setForbidden(boolean b) {
            isBool.set(b);
        }

        private void view() {
            var label = new Label(task.getAuthorName());
            label.setPrefWidth(200);
            var del = new Label("删除");
            del.setStyle("-fx-text-fill: red;");
            this.getChildren().addAll(label, del);
            this.setSpacing(6);
            del.setOnMouseClicked(e -> {
                var i = authorTaskMapper.delOne(task.getId());
                if (i > -1) labelListView.getItems().remove(this);
            });
        }

        private void controller() {

        }

    }
}
