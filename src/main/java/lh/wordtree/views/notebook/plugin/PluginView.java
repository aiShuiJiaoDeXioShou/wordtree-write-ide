package lh.wordtree.views.notebook.plugin;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lh.wordtree.component.WTFxInputAlert;
import lh.wordtree.component.WTMessage;

import java.util.ArrayList;
import java.util.List;

public class PluginView extends VBox {
    public PluginView() {
        var button = new Button("loading");
        var alert = new Button("alert");
        button.setPadding(new Insets(4, 10, 4, 10));
        this.getChildren().addAll(button, alert);
        button.setOnMouseClicked(e -> {
            WTMessage.sendError("无法使用该模块，因为没有开发！");
        });
        List<WTFxInputAlert.AlertTask> alertTasks = new ArrayList<>();
        alertTasks.add(new WTFxInputAlert.AlertTask("章节", "static/icon/default_file.svg", alert1 -> {
            System.out.println("章节");
        }));
        alertTasks.add(new WTFxInputAlert.AlertTask("Java", "static/icon/java.svg", alert1 -> {
            System.out.println("Java文件");
        }));
        alertTasks.add(new WTFxInputAlert.AlertTask("Python", "static/icon/py.svg", alert1 -> {
            System.out.println("Python文件");
        }));
        var alert1 = new WTFxInputAlert("新建文件", alertTasks);
        alert.setOnMouseClicked(e -> {
            alert1.show();
        });
    }

}
