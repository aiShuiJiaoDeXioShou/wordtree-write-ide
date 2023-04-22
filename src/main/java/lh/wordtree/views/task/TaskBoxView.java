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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.component.CpMessage;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.dao.WorkPlanMapper;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.dao.impl.WorkPlanMapperImpl;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.views.core.NoteCoreView;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskBoxView extends VBox {
    private final AuthorTaskMapper authorTaskMapper = new AuthorTaskMapperImpl();
    private static ListView<ItemBox> labelListView;
    private SimpleObjectProperty<ShowBox> showBox = new SimpleObjectProperty<>();
    private final WorkPlanMapper workPlanMapper = new WorkPlanMapperImpl();

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
                ItemBox taskBox = new ItemBox(authorTask);
                if (taskBox.isAfter) {
                    labelListView.getItems().add(taskBox);
                } else {
                    labelListView.getItems().add(0, taskBox);
                }
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
        VBox task = new VBox();
        Label title = new Label("任务列表：");
        title.setPadding(new Insets(0, 0, 0, 10));
        task.getChildren().addAll(title, labelListView);
        task.setStyle("-fx-background-radius: 15;");
        DropShadow effect = new DropShadow();
        effect.setColor(Color.valueOf("#ced4da"));
        effect.setOffsetX(5);
        effect.setOffsetY(5);
        ImageView img = new ImageView();
        img.setImage(new Image(Config.stc("static/icon/target_task.png")));
        img.setFitWidth(15);
        img.setFitHeight(15);
        title.setGraphic(img);
        task.setEffect(effect);
        // 重新添加或者第一次添加
        this.getChildren().addAll(showBox.get(), task);
    }

    private void controller() {
        labelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showBox.set(new ShowBox(newValue.task()));
        });
        showBox.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && this.getChildren().size() > 1) {
                this.getChildren().remove(0);
                this.getChildren().add(0, newValue);
            }
        });
    }

    private class ShowBox extends BorderPane {
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
            String startStr = LocalDateTimeUtil.format(autoTask.getStartDateTime(), "yyyy-MM-dd");
            String endStr = LocalDateTimeUtil.format(autoTask.getEndDateTime(), "yyyy-MM-dd");
            Label start = new Label(startStr);
            Label end = new Label(endStr);
            HBox hBox = new HBox();
            Label icon = new Label();
            ImageView iImage = new ImageView(new Image(Config.stc("static/icon/右箭头.png")));
            iImage.setFitHeight(15);
            iImage.setFitWidth(15);
            icon.setGraphic(iImage);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(start, icon, end);
            hBox.setStyle("-fx-background-color: #fff9db;-fx-background-radius: 5;-fx-padding: 2 10");

            var top = new HBox();
            top.getChildren().addAll(hBox, edit);
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
            describe.setMinHeight(100);
            describe.setStyle("-fx-background-radius: 5;-fx-background-color: #edf2ff;-fx-padding: 10");
            describe.setTextAlignment(TextAlignment.LEFT);
            describe.setAlignment(Pos.TOP_LEFT);
            describe.setWrapText(true);

            var duration = Duration.between(LocalDateTime.now(), autoTask.getEndDateTime());
            //获取相差的天数
            long days = duration.toDays();
            //获取相差的小时数
            long hour = duration.toHours();

            var isOk = "未完成";
            long number = workPlanMapper.selectDateNumber(startStr, endStr);
            isOk = autoTask.getNumber() < number ? "已完成" : "未完成";
            var hbox = new VBox();
            var time = new Label(days + "天 " + hour + " 小时\n");
            time.setStyle("-fx-text-fill: #4dabf7;-fx-font-size: 10;-fx-text-alignment: center;-fx-max-width: 100");
            var isOkLabel = new Label(isOk);
            if (isOk.equals("未完成")) {
                isOkLabel.setStyle("-fx-font-size: 20;-fx-text-fill: #f06595;");
            } else {
                isOkLabel.setStyle("-fx-font-size: 20;-fx-text-fill: #63e6be;");
            }
            hbox.setStyle("-fx-background-color: #e7f5ff;-fx-effect: dropshadow(three-pass-box,#8c8c8c, 10.0,0, -1, -2);-fx-background-radius: 5;-fx-padding: 8;");
            hbox.getChildren().addAll(isOkLabel, time);

            var box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(describe, hbox);
            box.setPadding(new Insets(15, 0, 15, 0));

            this.setPadding(new Insets(10));
            this.setTop(top);
            this.setCenter(box);
            this.setBottom(remember);
        }

    }

    private class AddPane extends BorderPane {
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

    private class ItemBox extends HBox {
        private SimpleBooleanProperty isBool = new SimpleBooleanProperty(true);
        private AuthorTask task;
        public boolean isAfter = false;

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
            label.setEllipsisString("...");
            isAfter = LocalDateTime.now().isAfter(task.getEndDateTime());
            if (isAfter) {
                label.setStyle("-fx-text-fill: #ced4da;");
                label.setDisable(false);
                label.setText("已过期： %s".formatted(label.getText()));
            }
            this.setSpacing(6);
            del.setOnMouseClicked(e -> {
                var i = authorTaskMapper.delOne(task.getId());
                if (i > -1) labelListView.getItems().remove(this);
            });
        }

    }
}
