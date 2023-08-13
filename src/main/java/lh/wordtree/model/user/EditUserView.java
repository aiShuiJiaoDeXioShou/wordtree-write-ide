package lh.wordtree.model.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.component.TreeDialog;
import lh.wordtree.ui.controls.WTIcon;
import lh.wordtree.ui.controls.WTInputPro;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 只有在系统检测到没有个人信息文件的情况下才会显示这个弹窗
 */
public class EditUserView extends TreeDialog {
    public BorderPane root = new BorderPane();
    public WTIcon wtIcon = new WTIcon("static/icon/x_头像.svg", 64, 64);
    public AtomicReference<File> file = new AtomicReference<>();
    public WTInputPro nameInput = new WTInputPro("笔名");
    public WTInputPro qiangInput = new WTInputPro("签名");
    public WTInputPro basePassword = new WTInputPro("加密密码");
    public VBox box = new VBox();
    public Button cel = new Button("取消");
    public Button confim = new Button("确定");
    private final EditeUserController editeUserController;

    public EditUserView() {
        this.init();
        // 设置控制器Controler
        editeUserController = new EditeUserController(this);
    }

    private void init() {
        root.setPadding(new Insets(30));
        root.setLeft(wtIcon);
        box.getChildren().addAll(nameInput, qiangInput, basePassword);
        box.setSpacing(10);
        root.setRight(box);
        var box1 = new HBox();
        box1.setAlignment(Pos.BOTTOM_RIGHT);
        box1.getChildren().addAll(cel, confim);
        box.setSpacing(20);
        root.setBottom(box1);
        this.setScene(new Scene(root, 500, 300));
        this.setTitle("编辑你的个人信息");
    }

}