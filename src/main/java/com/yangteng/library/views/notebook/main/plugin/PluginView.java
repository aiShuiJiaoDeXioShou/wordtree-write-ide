package com.yangteng.library.views.notebook.main.plugin;

import com.yangteng.library.component.WTMessage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PluginView extends VBox {
    public PluginView() {
        var button = new Button("loading");
        button.setPadding(new Insets(4, 10, 4, 10));
        this.getChildren().addAll(button);
        button.setOnMouseClicked(e->{
            WTMessage.sendError("无法使用该模块，因为没有开发！");
        });
    }

}
