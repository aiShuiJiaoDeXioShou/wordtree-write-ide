package lh.wordtree.uitest;

import cn.hutool.core.thread.ThreadUtil;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Message extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("""
            -fx-background-color: yellow;
        """);
        stackPane.setPadding(new Insets(20));
        var button = new Button("这个是根目录的一个元素");
        stackPane.setAlignment(button, Pos.CENTER);
        button.setOnMouseClicked(e -> {
            var label = new Label();
            label.setText("Hello World!");
            label.setStyle("""
                -fx-font-size: 15;
                -fx-background-color: skyblue;
                -fx-padding: 10 50 10 50;
                -fx-background-radius: 5;
                -fx-pref-width: 300;
            """);
            label.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(label);
            // 添加动画效果

            stackPane.setAlignment(label, Pos.BOTTOM_RIGHT);
            if (stackPane.getChildren().contains(label)) {
                ThreadUtil.execAsync(()->{
                    // 淡入淡出的动漫效果
                    FadeTransition ft = new FadeTransition(Duration.millis(1500), label);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.1);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                    ThreadUtil.sleep(1500);
                    Platform.runLater(() -> {
                        stackPane.getChildren().remove(label);
                    });
                });
            }
        });
        stackPane.getChildren().addAll(button);
        stackPane.setPrefSize(500, 600);
        var scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
