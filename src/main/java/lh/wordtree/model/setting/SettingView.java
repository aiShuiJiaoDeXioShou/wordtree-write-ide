package lh.wordtree.model.setting;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SettingView extends StackPane {
    public TreeView<Label> treeView;
    public BorderPane borderPane;
    public final Button apply;
    public final Button clear;
    private SettingController settingController;

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

        root.getChildren().add(general);
        root.getChildren().add(codeSetting);
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

        settingController = new SettingController(this);
    }
}
