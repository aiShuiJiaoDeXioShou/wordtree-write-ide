package com.yangteng.library;

import com.yangteng.library.views.notebook.main.root.NoteBookScene;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Index extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setScene(NoteBookScene.INSTANCE);
        primaryStage.setTitle("Learn");
        primaryStage.getIcons().add(new Image("static/icon/图书馆.png"));
        primaryStage.show();
    }
}
