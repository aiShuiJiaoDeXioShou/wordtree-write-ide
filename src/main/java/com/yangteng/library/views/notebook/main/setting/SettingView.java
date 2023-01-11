package com.yangteng.library.views.notebook.main.setting;

import com.yangteng.library.views.notebook.service.SettingService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SettingView extends StackPane {
    public final static SettingView INSTANCE = new SettingView();
    public TreeView<Label> treeView;
    public BorderPane borderPane;
    public SettingService service = new SettingService() {
    };
    private Button apply, clear;

    public SettingView() {
        // 左侧设置树
        treeView = new TreeView<>();
        borderPane = new BorderPane();
        treeView.setPrefWidth(250);

        var root = new TreeItem<Label>();
        root.setValue(new Label("设置列表"));

        var general = new TreeItem<Label>();
        var generalLable = new Label("常规设置");
        general.setValue(generalLable);

        var codeSetting = new TreeItem<Label>();
        var codeSettingLable = new Label("编辑器设置");
        codeSetting.setValue(codeSettingLable);

        root.getChildren().addAll(general, codeSetting);
        treeView.setRoot(root);
        borderPane.setLeft(treeView);

        // 下侧应用按钮,取消确定按钮
        var bottom = new HBox();
        apply = new Button("应用");
        clear = new Button("取消");
        bottom.getChildren().addAll(apply, clear);
        bottom.setAlignment(Pos.BOTTOM_RIGHT);
        bottom.setSpacing(10);
        bottom.setPadding(new Insets(10, 10, 10, 0));
        borderPane.setBottom(bottom);

        this.getChildren().add(borderPane);
        this.controller();
    }

    private void controller() {
        // 左侧文件树监听事件
        leftTreeAction();
        apply.setOnMouseClicked(event -> {
            var center = borderPane.getCenter();
            if (center instanceof TextArea) {
                // 对配置文件重新设置
                service.saveSettingText(((TextArea) center).getText());
            }
        });
    }

    private void leftTreeAction() {
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            var node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text) {
                var text = ((Text) node).getText();
                TextArea textArea = null;
                switch (text) {
                    case "常规设置" -> {
                        var settingValue = service.getSettingValue();
                        if (textArea == null) textArea = new TextArea();
                        textArea.setText(settingValue);
                        borderPane.setCenter(textArea);
                    }
                    case "编辑器设置" -> {

                    }
                }
            }
        });
    }

}
