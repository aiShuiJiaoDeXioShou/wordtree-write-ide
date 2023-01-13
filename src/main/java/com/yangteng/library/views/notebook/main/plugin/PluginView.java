package com.yangteng.library.views.notebook.main.plugin;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class PluginView extends VBox {

    public final static PluginView INSTANCE = new PluginView();

    public PluginView() {
        var button = new Button("loading");
        button.setGraphic(new FontIcon("fa-search"));
        button.setPadding(new Insets(4, 10, 4, 10));
        this.getChildren().addAll(button);
    }

}
