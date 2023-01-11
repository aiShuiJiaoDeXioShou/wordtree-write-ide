package com.yangteng.library;

import com.yangteng.library.comm.Config;
import com.yangteng.library.views.main.home.HomeScene;
import com.yangteng.library.views.notebook.main.root.NoteBookScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.File;
import java.util.Objects;

public class App extends Application {
    public static Stage primaryStage;
    public Scene scene = NoteBookScene.INSTANCE;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        this.setStyle();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Learn");
        primaryStage.getIcons().add(new Image("icon.ico"));
        primaryStage.show();
    }


    private void setStyle() {
        JMetro metro = new JMetro(Style.LIGHT);
        metro.setScene(scene);
        metro.getOverridingStylesheets().addAll(getStyle("static/css/base.css"), getStyle("static/css/app.css"));
    }

    private String getStyle(String path) {
        return Objects.requireNonNull(HomeScene.class.getClassLoader().getResource(path)).toExternalForm();
    }

    // 初始化配置对象
    private void initConfig() {
        // 判断这个路径下面是否有.wordtree目录，没有进行创建操作
        var appConfigDirFile = new File(Config.APP_CONFIG_DIR);
        if (!appConfigDirFile.exists()) {
            appConfigDirFile.mkdirs();
        }
    }
}
