package com.yangteng.views.notebook.component;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NoteLeftButtonItem extends Label {
    public NoteLeftButtonItem(String iconPath,String tooltipText) {
        Image image = new Image(iconPath, 25,25,true,true);
        this.setGraphic(new ImageView(image));
        this.getStyleClass().add("note-left-button-item");
        Tooltip.install(this, new Tooltip(tooltipText));
    }
}
