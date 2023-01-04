package com.yangteng.views.home;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class HomeView extends Scene {
    public final static HBox center = new HBox();
    static {
        var leftMenus = LeftMenusView.INTER;
        var rightShowView = RightShowViwe.INTER;
        center.getChildren().addAll(leftMenus, rightShowView);
        center.setPrefHeight(700);
    }

    public final static HomeView INTER =  new HomeView(center);

    public HomeView(Parent parent) {
        super(parent);
        this.getStylesheets().add(Objects.requireNonNull(HomeView.class.getResource("light.css")).toExternalForm());
    }
}
