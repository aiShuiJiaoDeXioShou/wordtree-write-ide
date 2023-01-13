package com.yangteng.library.views.notebook.main.plugin;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PluginView extends VBox {

    public final static PluginView INSTANCE = new PluginView();

    public PluginView() {
        var button = new Button("loading");
        button.setPadding(new Insets(4, 10, 4, 10));
        this.getChildren().addAll(button);
    }

}
