package com.yangteng.views.main.component;


import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Menu extends Label {

    private Pane pareNode;

    public Menu(Pane pareNode, String name, String iconPath) {
        super(name);
        this.pareNode = pareNode;
        this.prefWidthProperty().bind(pareNode.widthProperty());
        this.setPadding(new Insets(10));
        this.setCursor(Cursor.HAND);
        this.getStyleClass().add("menu");
        var img = new Image(iconPath, 15, 15, true, true);
        var imageView = new ImageView(img);
        this.setGraphic(imageView);
        pareNode.getChildren().add(this);
    }

}
