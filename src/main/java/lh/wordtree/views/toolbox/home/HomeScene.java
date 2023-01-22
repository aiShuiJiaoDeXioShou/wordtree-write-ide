package lh.wordtree.views.toolbox.home;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class HomeScene extends Scene {
    public final static HBox center = new HBox();
    static {
        var leftMenus = LeftMenusView.INSTANCE;
        var rightShowView = RightShowViwe.INSTANCE;
        center.getChildren().addAll(leftMenus, rightShowView);
        center.setPrefHeight(700);
    }

    public final static HomeScene INSTANCE =  new HomeScene(center);

    public HomeScene(Parent parent) {
        super(parent);
        this.getStylesheets().add(Objects.requireNonNull(HomeScene.class.getClassLoader().getResource("static/style/app.css")).toExternalForm());
    }
}
