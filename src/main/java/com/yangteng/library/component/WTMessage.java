package com.yangteng.library.component;

import cn.hutool.core.thread.ThreadUtil;
import com.yangteng.library.App;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import jfxtras.styles.jmetro.MDL2IconFont;

public interface WTMessage {

    static void sendInfo(String msg) {
        init(msg, Paint.valueOf("#f1f3f5"),new MDL2IconFont("\uE946"));
    }

    static void sendError(String msg) {
        init(msg, Paint.valueOf("#e03131"), new MDL2IconFont("\uEA39"));
    }

    static void sendSuccess(String msg) {
        init(msg, Paint.valueOf("#12b886"),new MDL2IconFont("\uE930"));
    }

    static void sendWarning(String msg) {
        init(msg, Paint.valueOf("#ffe066"),new MDL2IconFont("\uE7BA"));
    }

    static void sendLoading(String msg) {
        init(msg, Paint.valueOf("#74c0fc"),new MDL2IconFont("\uE895"));
    }


    static void init(String msg,Paint color, MDL2IconFont iconFont) {
        var stackPane = App.rootPane;

        // 提示本体
        var label = new Label();
        label.setStyle("-fx-text-fill: white");
        iconFont.setStyle("-fx-text-fill: white");
        label.setText(msg);
        label.setBackground(
                new Background(
                        new BackgroundFill(color,new CornerRadii(5), Insets.EMPTY)
                )
        );
        label.setPrefWidth(420);
        label.setPadding(new Insets(10,50,10,50));
        label.setFont(new Font(15));
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        label.setGraphic(iconFont);

        // 调整一下布局
        var space = new VBox();
        space.getChildren().add(label);
        space.setPadding(new Insets(20));
        space.setMaxSize(label.getPrefWidth() + 20, label.getPrefHeight() + 20);

        stackPane.getChildren().add(space);
        StackPane.setAlignment(space, Pos.TOP_CENTER);
        // 添加动画效果
        if (stackPane.getChildren().contains(space)) {
            // 调用一个线程去显示该提示
            ThreadUtil.execAsync(() -> {
                // 淡入淡出的动漫效果
                FadeTransition ft = new FadeTransition(Duration.millis(2000), space);
                ft.setFromValue(1.0);
                ft.setToValue(0.1);
                ft.setCycleCount(Timeline.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
                ThreadUtil.sleep(2200);
                Platform.runLater(() -> {
                    stackPane.getChildren().remove(space);
                });
            });
        }

    }

}
