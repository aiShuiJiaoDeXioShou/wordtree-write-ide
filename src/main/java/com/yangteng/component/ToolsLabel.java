package com.yangteng.component;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class ToolsLabel extends Label {

    public ToolsLabel(String text, String iconPath) {
        super(text);
        var img = new Image(iconPath, 25, 25, true, true);
        var imageView = new ImageView(img);
        this.setGraphic(imageView);
        this.setPadding(new Insets(10));
        this.setPrefWidth(150);
        this.setPrefHeight(50);
        this.setStyle("""
             -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );
             -fx-background-color: white;
        """);
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue()) {
                this.setBorder(new Border(new BorderStroke(Paint.valueOf("skyblue"),BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2 ,0))));
            } else  {
                this.setBorder(new Border(new BorderStroke(Paint.valueOf("white"),BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2 ,0))));
            }
        });
    }

}
