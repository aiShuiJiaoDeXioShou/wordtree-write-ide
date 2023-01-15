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
        primaryStage.getIcons().add(new Image("static/icon/icon.png"));
        primaryStage.show();
    }

    /**
     * 在应用启动之前进行初始化操作
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * 应用结束的时候执行的操作
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        // 结束前的操作
        super.stop();
        // 结束后的操作
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
