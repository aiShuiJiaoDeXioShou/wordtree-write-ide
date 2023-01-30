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
import lh.wordtree.service.task.TaskService;
import lh.wordtree.service.web.WebStartsServiceImpl;
import lh.wordtree.task.ITask;
import lh.wordtree.utils.ConfigUtils;
import lh.wordtree.views.notebook.root.NoteBookScene;
import lh.wordtree.views.toolbox.home.HomeScene;

import java.util.Objects;

public class App extends Application {
    public final static StackPane rootPane = new StackPane();

    public static Stage primaryStage;
    public Scene scene = NoteBookScene.INSTANCE;
    private Log log = LogFactory.get();
    private WebStartsServiceImpl web = new WebStartsServiceImpl();

    @Override
    public void start(Stage stage) throws Exception {
        log.info("应用程序开始启动...");
        log.info("正在初始化web服务...");
        web.start();
        primaryStage = stage;
        this.setStyle();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Learn");
        primaryStage.getIcons().add(new Image("static/icon/icon.png"));
        primaryStage.show();
        log.info("正在启动初始化服务...");
        log.info("应用程序启动成功...");
    }

    /**
     * 在应用启动之前进行初始化操作
     *
     * @throws Exception
     */
    public void init() throws Exception {
        TaskService.INSTANCE.start(ITask.INIT);
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
        web.stop();
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
                HomeScene.class
                        .getClassLoader()
                        .getResource(path)
        ).toExternalForm();
    }
}
