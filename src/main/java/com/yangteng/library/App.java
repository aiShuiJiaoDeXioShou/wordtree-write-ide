package com.yangteng.library;

import com.yangteng.library.comm.Config;
import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.views.main.home.HomeScene;
import com.yangteng.library.views.notebook.main.root.NoteBookScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class App extends Application {
    static {
        // 应用程序启动前要进行初始化操作
        try {
            Config.initConfig();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage primaryStage;
    public Scene scene = NoteBookScene.INSTANCE;

    @Override
    public void start(Stage stage) throws Exception {
        System.gc();
        primaryStage = stage;
        this.setStyle();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Learn");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.show();
    }

    private void setStyle() {
        JMetro metro;
        if (ConfigUtils.getProperties("defThemeColor").equals("light")) {
            metro = new JMetro(Style.LIGHT);
        } else metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        metro.getOverridingStylesheets().addAll(getStyle("static/css/base.css"), getStyle("static/css/app.css"));
    }

    private String getStyle(String path) {
        return Objects.requireNonNull(HomeScene.class.getClassLoader().getResource(path)).toExternalForm();
    }
}
