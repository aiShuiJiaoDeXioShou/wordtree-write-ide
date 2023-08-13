package lh.wordtree.model.tray;

import dorkbox.desktop.Desktop;
import dorkbox.jna.rendering.ProviderType;
import dorkbox.jna.rendering.RenderProvider;
import dorkbox.jna.rendering.Renderer;
import dorkbox.os.OS;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;

import java.io.IOException;

public class WtSystemTray {
    public SystemTray systemTray;
    public Stage stage;
    public Menu mainMenu;
    public Menu theme = new Menu("主题");
    public MenuItem dark = new MenuItem("暗黑");
    public MenuItem light = new MenuItem("明亮");
    public MenuItem about = new MenuItem("关于");
    public MenuItem setting = new MenuItem("设置");
    public MenuItem bookcase = new MenuItem("书架");
    public MenuItem dwon = new MenuItem("下载");
    public MenuItem statistical = new MenuItem("统计");
    public MenuItem exit = new MenuItem("退出");

    public void doJavaFxStuff(final Stage stage) {
        this.stage = stage;
        this.systemTray = dorkbox.systemTray.SystemTray.get("SysTrayExample");
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        mainMenu = systemTray.getMenu();
        RenderProvider.set(new JavaFxProvider());
        dorkbox.systemTray.SystemTray.DEBUG = true;
        CacheUtil.clear("SysTrayExample");
        systemTray.setTooltip(Config.APP_NAME);
        systemTray.setImage(WtSystemTray.class.getClassLoader().getResource("static/icon/icon.png"));
        systemTray.setStatus(Config.APP_NAME);
        mainMenu.add(new Separator());
        mainMenu.add(about);
        mainMenu.add(new MenuItem("打开配置文件目录", e->{
            try {
                Desktop.browseDirectory(Config.APP_CONFIG_DIR);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));
        theme.add(dark);
        theme.add(light);
        systemTray.getMenu().add(theme);
        systemTray.getMenu().add(setting);
        systemTray.getMenu().add(bookcase);
        systemTray.getMenu().add(dwon);
        systemTray.getMenu().add(statistical);
        systemTray.getMenu().add(exit).setShortcut('q');

        WtSysetmTrayController sysTrayController = new WtSysetmTrayController(this);
    }

    private void shutdown() {
        systemTray.shutdown();
    }


    public class JavaFxProvider implements Renderer {
        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public ProviderType getType() {
            return ProviderType.JAVAFX;
        }

        @Override
        public boolean alreadyRunning() {
            return false;
        }

        @Override
        public boolean isEventThread() {
            return javafx.application.Platform.isFxApplicationThread();
        }

        @Override
        public int getGtkVersion() {
            if (!OS.INSTANCE.isLinux()) {
                return 0;
            }
            if (OS.INSTANCE.getJavaVersion() < 9) {
                return 2;
            }
            String version = OS.INSTANCE.getProperty("jdk.gtk.version", "2");
            if ("3".equals(version) || version.startsWith("3.")) {
                return 3;
            }
            else {
                return 2;
            }
        }

        @Override
        public
        boolean dispatch(final Runnable runnable) {
            if (isEventThread()) {
                runnable.run();
            }
            else {
                javafx.application.Platform.runLater(runnable);
            }
            return true;
        }
    }

}
