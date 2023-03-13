package lh.wordtree;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.ConfigUtils;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.task.TaskService;
import lh.wordtree.task.ITask;
import lh.wordtree.views.root.NoteBookScene;

import java.util.Objects;

public class App extends Application {
    public final static StackPane rootPane = new StackPane();

    public static Stage primaryStage;
    public Scene scene = NoteBookScene.newInstance();
    private final Log log = LogFactory.get();

    public App() {
        log.info("应用程序开始启动...");
        TaskService.INSTANCE.start(ITask.INIT);
    }

    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        this.setStyle();
        primaryStage.setScene(scene);
        primaryStage.setTitle(Config.APP_NAME);
        primaryStage.getIcons().add(new Image(Config.APP_ICON));
        primaryStage.show();
        FactoryBeanService.heigth.bind(primaryStage.heightProperty());
        log.info("应用程序启动成功...");
    }

    /**
     * 在应用启动之前进行初始化操作
     *
     * @throws Exception
     */
    public void init() throws Exception {
    }

    /**
     * 应用结束的时候执行的操作
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        // 关闭web服务器
        log.info("正在关闭窗口。");
        TaskService.INSTANCE.start(ITask.END);
        log.info("正在关闭web服务。");
        log.info("应用程序已退出。");
        System.exit(0);
    }

    private void setStyle() {
        JMetro metro;
        if (ConfigUtils.getProperties("defThemeColor").equals("light")) {
            metro = new JMetro(Style.LIGHT);
        } else metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        metro.getOverridingStylesheets().addAll(
                getStyle("static/style/light.css"),
                getStyle("static/style/base.css"),
                getStyle("static/style/app.css"),
                getStyle("static/style/editor/writer-editor.css")
        );
    }

    private String getStyle(String path) {
        return Objects.requireNonNull(
                App.class
                        .getClassLoader()
                        .getResource(path)
        ).toExternalForm();
    }
}
