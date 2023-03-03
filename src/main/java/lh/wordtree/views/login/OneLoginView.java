package lh.wordtree.views.login;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.CpMessage;
import lh.wordtree.entity.Author;
import lh.wordtree.ui.WTIcon;
import lh.wordtree.ui.WTInputPro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 只有在系统检测到没有个人信息文件的情况下才会显示这个弹窗
 */
public class OneLoginView extends Stage {
    private BorderPane root = new BorderPane();

    public OneLoginView() {
        this.setAlwaysOnTop(true);
        var wtIcon = new WTIcon("static/icon/x_头像.svg", 64, 64);
        AtomicReference<File> file = new AtomicReference<>();
        wtIcon.setOnMouseClicked(e -> {
            var fileChooser = new FileChooser();
            fileChooser.setTitle("请选择您头像的图片");
            file.set(fileChooser.showOpenDialog(this));
            try {
                wtIcon.setImage(new Image(new FileInputStream(file.get().getPath()), 64, 64, true, true));
            } catch (FileNotFoundException ex) {
                CpMessage.sendError("这个文件不能作为您的头像");
            }
        });
        root.setPadding(new Insets(30));
        root.setLeft(wtIcon);
        var nameInput = new WTInputPro("笔名");
        var qiangInput = new WTInputPro("签名");
        var basePassword = new WTInputPro("加密密码");
        var box = new VBox();
        box.getChildren().addAll(nameInput, qiangInput, basePassword);
        box.setSpacing(10);
        root.setRight(box);
        var box1 = new HBox();
        box1.setAlignment(Pos.BOTTOM_RIGHT);
        var confim = new Button("确定");
        {
            confim.setOnMouseClicked(e -> {
                this.close();
                Author author = null;
                try {
                    author = new Author(nameInput.getTextField().getText(),
                            qiangInput.getTextField().getText(),
                            file.get().getPath(),
                            basePassword.getTextField().getText(),
                            new ArrayList<>(), new ArrayList<>());
                } catch (Exception e2) {
                    CpMessage.sendError("必须要输入全部信息！");
                }
                if (author != null) {
                    var bytes = JSON.toJSONBytes(author, JSONWriter.Feature.PrettyFormat);
                    FileUtil.writeBytes(bytes, Config.USER_CONFIG_PATH);
                }
            });
        }
        var cel = new Button("取消");
        {
            cel.setOnMouseClicked(e -> this.close());
        }
        box1.getChildren().addAll(cel, confim);
        box.setSpacing(20);
        root.setBottom(box1);
        this.getIcons().add(new Image(Config.APP_ICON));
        this.setScene(new Scene(root, 500, 300));
        this.setTitle("因为您是第一次进入本程序，所以要完善必要的个人信息！！！");
    }

}
