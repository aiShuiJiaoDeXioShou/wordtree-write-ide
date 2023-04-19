package lh.wordtree.views.task;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.component.CpMessage;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.views.core.NoteCoreView;

import java.time.LocalDateTime;

public class TaskBoxView extends VBox {
    private static final AuthorTaskMapper authorTaskMapper = new AuthorTaskMapperImpl();
    private static ListView<ItemBox> labelListView;
    private SimpleObjectProperty<ShowBox> showBox = new SimpleObjectProperty<>();

    public TaskBoxView() {
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

        /**
         * 新增窗口的弹出窗
         */
        Stage window = new Stage();

        public ShowBox(AuthorTask autoTask) {
            edit.setOnMouseClicked(e -> {
                var pane = new AddPane();
                var scene = new Scene(pane, 500, 500);
                lh.wordtree.comm.config.Config.setBaseStyle(scene);
                window.setAlwaysOnTop(true);
                window.setMaximized(false);
                window.setTitle("新增一条目标任务");
                window.setScene(scene);
                window.getIcons().add(new Image(Config.APP_ICON));
                window.show();
            });
            if (autoTask == null) {
                this.setCenter(edit);
                return;
            }
            this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFF"), null, null)));
            var startLabel = new Label();
            String startStr = LocalDateTimeUtil.format(autoTask.getStartDateTime(), "yyyy-MM-dd");
            String endStr = LocalDateTimeUtil.format(autoTask.getEndDateTime(), "yyyy-MM-dd");
            startLabel.setText(startStr + " -> " + endStr);
            var top = new HBox();
            top.getChildren().addAll(startLabel, edit);
            top.setSpacing(20);

            Label remember = new Label("不积跬步无以至千里，不积小流五以成江海。");
            ImageView img = new ImageView();
            img.setFitHeight(15);
            img.setFitWidth(15);
            img.setImage(new Image(Config.stc("static/icon/info.png")));
            remember.setGraphic(img);
            remember.setStyle("-fx-text-fill: #adb5bd;-fx-font-size: 10;");
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
            var iptW = 180;

            var label = new Label("结束时间日期：");
            var endTime = new MFXDatePicker();

            var name = new MFXTextField();
            name.setFloatingText("任务名称：");

            var der = new MFXTextField();
            der.setFloatingText("任务描述:");

            var number = new MFXTextField();
            number.setFloatingText("目标字数：");

            var isMandatory = new MFXToggleButton("是否采用强制手段");

            center.setSpacing(15);
            center.getChildren().addAll(name, label, endTime, der, number, isMandatory);
            FXCollections.observableArrayList(endTime, name, der, number)
                    .forEach(item -> {
                        item.setMinWidth(iptW);
                    });

            this.setCenter(center);
            var save = new MFXButton("保存");
            var bottm = new BorderPane();
            bottm.setRight(save);
            this.setBottom(bottm);

            save.setOnMouseClicked(e -> {
                if (der.getText().isBlank()) {
                    CpMessage.sendError("任务详情描述不能为空！");
                    return;
                }
                if (name.getText().isBlank()) {
                    CpMessage.sendError("任务名称不能为空！");
                    return;
                }
                if (endTime.getValue() == null) {
                    CpMessage.sendError("结束时间不能为空！");
                    return;
                }
                var authorTask = new AuthorTask();
                authorTask.setNumber(Integer.parseInt(number.getText()));
                authorTask.setEndDateTime(LocalDateTimeUtil.format(endTime.getValue(), "yyyy-MM-dd HH:mm:ss"));
                authorTask.setMandatory(isMandatory.isSelected() ? "true" : "false");
                authorTask.setStartDateTime(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
                authorTask.setDescribe(der.getText());
                authorTask.setComplete("false");
                authorTask.setAuthorName(name.getText());
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
            ImageView img = new ImageView();
            img.setFitWidth(15);
            img.setFitHeight(15);
            img.setImage(new Image(Config.stc("static/icon/task-color.png")));
            label.setGraphic(img);
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
