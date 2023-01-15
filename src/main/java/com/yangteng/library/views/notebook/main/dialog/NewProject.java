package com.yangteng.library.views.notebook.main.dialog;

import com.yangteng.library.comm.Config;
import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.views.notebook.entity.NovelProject;
import com.yangteng.library.views.notebook.main.core.LeftNoteBookFileTreeView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.time.LocalDateTime;

public class NewProject extends Dialog {
    private SplitPane splitPane;
    private ListView<Label> listView;
    private BorderPane novel;

    public NewProject() {
        var dialogPane = new DialogPane();
        {
            dialogPane.getButtonTypes().add(ButtonType.CANCEL);
            this.setDialogPane(dialogPane);
        }
        splitPane = new SplitPane();
        listView = new ListView<Label>();
        {
            var label = new Label();
            label.setId("小说项目");
            label.setText("小说项目");
            label.setGraphic(new ImageView("static/icon/图书馆.png"));
            listView.getItems().addAll(label);
        }
        novel = new BorderPane();
        {
            // 新建一个小说
            var hBox = new HBox();
            var label = new Label("小说路径");
            var choose = new Button("选择");
            var textField = new TextField();
            hBox.getChildren().addAll(label, textField, choose);
            hBox.setSpacing(10);

            var hBox1 = new HBox();
            var name = new Label("名称:");
            var nameField = new TextField();
            hBox1.getChildren().addAll(name, nameField);
            hBox1.setSpacing(10);

            var hBox2 = new HBox();
            var img = new Label("封面:");
            var chooseImg = new TextField();
            hBox1.getChildren().addAll(img, chooseImg);
            hBox1.setSpacing(10);

            var hBox3 = new HBox();
            var theme = new Label("题材:");
            var themeField = new TextField();
            hBox1.getChildren().addAll(theme, themeField);
            hBox1.setSpacing(10);

            var hBox4 = new HBox();
            var number = new Label("目标字数:");
            var numberField = new TextField();
            hBox1.getChildren().addAll(theme, numberField);
            hBox1.setSpacing(10);

            var buttons = new HBox();
            buttons.setSpacing(10);
            buttons.setAlignment(Pos.BOTTOM_RIGHT);
            var button = new Button("创建");
            buttons.getChildren().addAll(button);

            button.setOnMouseClicked(e -> {
                var novelProject = new NovelProject();
                var path = textField.getText() + "/" + nameField.getText();
                {
                    novelProject.setPath(path);
                    novelProject.setAuthor(ConfigUtils.getProperties("username"));
                    novelProject.setImg(chooseImg.getText());
                    novelProject.setStartDateTime(LocalDateTime.now());
                    novelProject.setTargetWeb("起点");
                    novelProject.setTargetNumber(Integer.valueOf(number.getText()));
                    novelProject.setTheme(theme.getText());
                    novelProject.setName(name.getText());
                }
                var file = new File(path);
                Config.initWriteWorkSpace(file, novelProject);
                LeftNoteBookFileTreeView.INSTANCE.toggleFile(file);
                try {
                    this.clone();
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException(ex);
                }
            });

            var boxRoot = new VBox();
            boxRoot.getChildren().addAll(hBox1, hBox, hBox2, hBox3, hBox4);
            boxRoot.setSpacing(15);
            novel.setTop(boxRoot);
            novel.setBottom(buttons);
        }
        splitPane.getItems().addAll(listView, novel);
        dialogPane.setContent(splitPane);
        this.controller();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "小说项目" -> toggle(novel);
            }
        });
    }

    public void toggle(Node node) {
        splitPane.getItems().remove(1);
    }

}
