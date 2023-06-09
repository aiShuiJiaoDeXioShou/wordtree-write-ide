package lh.wordtree.views.tray;

import dorkbox.desktop.Desktop;
import dorkbox.jna.rendering.RenderProvider;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import javafx.application.Platform;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;

import java.io.IOException;

public class WtSystemTray {
    private dorkbox.systemTray.SystemTray systemTray;
    public void doJavaFxStuff(final Stage stage) {
        RenderProvider.set(new JavaFxProvider());
        dorkbox.systemTray.SystemTray.DEBUG = true;
        CacheUtil.clear("SysTrayExample");
        this.systemTray = dorkbox.systemTray.SystemTray.get("SysTrayExample");
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        systemTray.setTooltip(Config.APP_NAME);
        systemTray.setImage(WtSystemTray.class.getClassLoader().getResource("static/icon/icon.png"));
        systemTray.setStatus(Config.APP_NAME);

        Menu mainMenu = systemTray.getMenu();
        mainMenu.add(new Separator());
        MenuItem about = new MenuItem("关于", e -> {
            try {
                Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainMenu.add(about);

        mainMenu.add(new MenuItem("打开配置文件目录", e->{
            try {
                Desktop.browseDirectory(Config.APP_CONFIG_DIR);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));

        Menu theme = new Menu("主题");
        MenuItem dark = new MenuItem("暗黑");
        MenuItem light = new MenuItem("明亮");
        theme.add(dark);
        theme.add(light);
        systemTray.getMenu().add(theme);

        MenuItem setting = new MenuItem("设置");
        systemTray.getMenu().add(setting);

        MenuItem bookcase = new MenuItem("书架");
        systemTray.getMenu().add(bookcase);

        MenuItem dwon = new MenuItem("下载");
        systemTray.getMenu().add(dwon);

        MenuItem statistical = new MenuItem("统计");
        systemTray.getMenu().add(statistical);

        systemTray.getMenu().add(new MenuItem("退出", e->{
            systemTray.shutdown();
            Runnable runnable = ()->{
                stage.hide();
                Platform.exit();
            };

            if (!RenderProvider.isEventThread()) {
                RenderProvider.dispatch(runnable);
            } else {
                runnable.run();
            }
        })).setShortcut('q');
    }

    private void shutdown() {
        systemTray.shutdown();
    }
}
