package com.yangteng.library.views.notebook.main.core;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

public class BottomStateView extends HBox {
    public static final BottomStateView INSTANCE = new BottomStateView();

    public BottomStateView() {
        this.setPrefHeight(40);
        this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ffff"), null, null)));
    }
}
