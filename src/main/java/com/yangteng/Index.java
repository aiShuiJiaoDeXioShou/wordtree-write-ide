package com.yangteng;

import com.yangteng.views.home.HomeView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Index extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setScene(HomeView.INTER);
        primaryStage.setTitle("Learn");
        primaryStage.getIcons().add(new Image("icon/图书馆.png"));
        primaryStage.show();
    }
}
