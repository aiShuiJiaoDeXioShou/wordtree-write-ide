package com.yangteng.library;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test4 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var dialog = new Dialog<>();
        var dialogPane = new DialogPane();
        dialog.setDialogPane(dialogPane);
        var box = new VBox();
        var button = new Button("打开");
        var box1 = new VBox();
        box1.getChildren().add(new Label("打不开"));
        dialogPane.setContent(box1);
        button.setOnMouseClicked(e -> dialog.show());
        box.getChildren().add(button);
        var scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
