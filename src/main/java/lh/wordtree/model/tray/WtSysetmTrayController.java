package lh.wordtree.model.tray;

import dorkbox.desktop.Desktop;
import dorkbox.jna.rendering.RenderProvider;
import javafx.application.Platform;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class WtSysetmTrayController {
    private WtSystemTray wtSystemTray;

    public WtSysetmTrayController(WtSystemTray wtSystemTray) {
        this.wtSystemTray = wtSystemTray;
        wtSystemTray.exit.setCallback(this::exit);
        wtSystemTray.about.setCallback(this::about);
    }

    // 退出方法
    public void exit(ActionEvent event) {
        wtSystemTray.systemTray.shutdown();
        Runnable runnable = ()->{
            wtSystemTray.stage.hide();
            Platform.exit();
        };
        if (!RenderProvider.isEventThread()) {
            RenderProvider.dispatch(runnable);
        } else {
            runnable.run();
        }
    }

    // 关于方法
    public void about(ActionEvent event) {
        try {
            Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
