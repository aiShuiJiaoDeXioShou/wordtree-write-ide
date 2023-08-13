package lh.wordtree;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.ConfigUtils;
import lh.wordtree.component.SystemMessage;
import lh.wordtree.service.Subscriber;
import lh.wordtree.task.TaskService;
import lh.wordtree.task.ITask;
import lh.wordtree.model.tray.WtSystemTray;

public class App extends Application {
    public final static StackPane rootPane = new StackPane();
    public static Stage primaryStage;
    public Scene scene;
    private final Log log = LogFactory.get();
    private WtSystemTray systemTray = new WtSystemTray();

    public void start(Stage stage) throws Exception {
        TaskService.INSTANCE.start(ITask.INIT);
        scene = AppScene.newInstance();
        primaryStage = stage;
        this.style();
        primaryStage.setScene(scene);
        primaryStage.setTitle(Config.APP_NAME);
        primaryStage.getIcons().add(new Image(Config.APP_ICON));
        primaryStage.setMinHeight(Config.APP_HEIGHT + 20);
        primaryStage.setMinWidth(Config.APP_WIDTH + 20);
        primaryStage.heightProperty().addListener(this::changed);
        // 创建系统托盘
        systemTray.doJavaFxStuff(stage);
        primaryStage.show();
        BeanFactory.heigth.bind(primaryStage.heightProperty());
        log.info("应用程序启动成功...");

        Subscriber.subscribe("init");
    }

    /**
     * 监听高度的变化
     */
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (newValue.doubleValue() == (Config.APP_HEIGHT + 20)) {
            SystemMessage.sendError("这已经是最小宽高了不能继续变低了！");
        }
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
        log.info("正在关闭系统托盘。");
        log.info("应用程序已退出。");
        System.exit(0);
    }

    private void style() {
        JMetro metro;
        if (ConfigUtils.getProperties("defThemeColor").equals("light")) {
            metro = new JMetro(Style.LIGHT);
        } else metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        metro.getOverridingStylesheets().addAll(
                Config.src("static/style/light.css"),
                Config.src("static/style/base.css"),
                Config.src("static/style/app.css"),
                Config.src("static/style/editor/writer-editor.css")
        );
    }
}
