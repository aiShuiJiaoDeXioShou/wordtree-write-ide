package lh.wordtree.plugin.toolbox.home;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class HomeScene extends Scene {
    public final static HBox center = new HBox();
    public final static HomeScene INSTANCE = new HomeScene(center);

    static {
        var leftMenus = LeftMenusView.INSTANCE;
        var rightShowView = RightShowViwe.INSTANCE;
        center.getChildren().addAll(leftMenus, rightShowView);
        center.setPrefHeight(700);
    }

    public HomeScene(Parent parent) {
        super(parent);
        this.getStylesheets().add(Objects.requireNonNull(HomeScene.class.getClassLoader().getResource("static/style/app.css")).toExternalForm());
    }
}
