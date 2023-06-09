package lh.wordtree.uitest;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import dorkbox.jna.rendering.RenderProvider;
import dorkbox.os.OS;
import dorkbox.systemTray.Checkbox;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import dorkbox.desktop.Desktop;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class TestTrayJavaFX {

    public static final URL BLUE_CAMPING = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");
    public static final URL BLACK_FIRE = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");

    public static final URL BLACK_MAIL = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");
    public static final URL GREEN_MAIL = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");

    public static final URL BLACK_BUS = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");
    public static final URL LT_GRAY_BUS = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");

    public static final URL BLACK_TRAIN = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");
    public static final URL GREEN_TRAIN = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");
    public static final URL LT_GRAY_TRAIN = TestTrayJavaFX.class.getClassLoader().getResource("arrow-correct-roundyuanzhengque.png");

    // from issue 123
    public static final URL NOTIFY_IMAGE = TestTrayJavaFX.class.getResource("arrow-correct-roundyuanzhengque.png");


    public static class MyApplication extends Application {
        private TestTrayJavaFX testTrayJavaFX = new TestTrayJavaFX();

        public
        MyApplication() {
        }

        @Override
        public
        void start(final Stage stage) {
            testTrayJavaFX.doJavaFxStuff(stage);
        }

        @Override
        public void stop() throws Exception {
            // SWT/JavaFX "shutdown hooks" have changed. Since it's no longer available with JPMS, it is no longer supported.
            // Developers must add the shutdown hooks themselves.
            testTrayJavaFX.shutdown();
        }
    }

    public static void main(String[] args) {
        Application.launch(MyApplication.class);
    }

    private SystemTray systemTray;
    private ActionListener callbackGray;

    public TestTrayJavaFX() {

    }

    public void doJavaFxStuff(final Stage stage) {
        stage.setTitle("Hello World JavaFx!");
        Button btn = new Button();
        btn.setText("Say 'Hello World JavaFx'");
        btn.setOnAction(event->System.out.println("Hello World JavaFx!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        stage.setScene(new Scene(root, 300, 250));
        stage.show();

        // required, so the rendering back-end knows that we are using JavaFX
        RenderProvider.set(new JavaFxProvider());

        SystemTray.DEBUG = true; // for test apps, we always want to run in debug mode

        // for test apps, make sure the cache is always reset. These are the ones used, and you should never do this in production.
        CacheUtil.clear("SysTrayExample");

        // SwingUtil.setLookAndFeel(null); // set Native L&F (this is the System L&F instead of CrossPlatform L&F)
        // SystemTray.SWING_UI = new CustomSwingUI();

        this.systemTray = SystemTray.get("SysTrayExample");
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        // SWT/JavaFX "shutdown hooks" have changed. Since it's no longer available with JPMS, it is no longer supported.
        // Developers must add the shutdown hooks themselves.

        systemTray.setTooltip("Mail Checker");
        systemTray.setImage(LT_GRAY_TRAIN);
        systemTray.setStatus("No Mail");

        callbackGray = e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus(null);
            systemTray.setImage(BLACK_TRAIN);

            entry.setCallback(null);
//                systemTray.setStatus("Mail Empty");
            systemTray.getMenu().remove(entry);
            System.err.println("POW");
        };


        Menu mainMenu = systemTray.getMenu();

        MenuItem greenEntry = new MenuItem("Green Mail", e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus("Some Mail!");
            systemTray.setImage(GREEN_TRAIN);

            entry.setCallback(callbackGray);
            entry.setImage(BLACK_MAIL);
            entry.setText("Delete Mail");
            entry.setTooltip(null); // remove the tooltip
//                systemTray.remove(menuEntry);
        });
        greenEntry.setImage(GREEN_MAIL);
        // case does not matter
        greenEntry.setShortcut('G');
        greenEntry.setTooltip("This means you have green mail!");
        mainMenu.add(greenEntry);


        Checkbox checkbox = new Checkbox("Euro € Mail", e->System.err.println("Am i checked? " + ((Checkbox) e.getSource()).getChecked()));
        checkbox.setShortcut('€');
        mainMenu.add(checkbox);

        MenuItem removeTest = new MenuItem("This should not be here", e->{
            try {
                Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainMenu.add(removeTest);
        mainMenu.remove(removeTest);

        mainMenu.add(new Separator());

        mainMenu.add(new MenuItem("About", e->{
            try {
                Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));

        mainMenu.add(new MenuItem("Temp Directory", e->{
            try {
                Desktop.browseDirectory(OS.INSTANCE.getTEMP_DIR().getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));

        mainMenu.add(new MenuItem("Notify", e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus("Notification!");
            systemTray.setImage(NOTIFY_IMAGE);

            entry.setImage(NOTIFY_IMAGE);
            entry.setText("Did notify");
            System.err.println("NOTIFICATION!");
        }));

        Menu submenu = new Menu("Options", BLUE_CAMPING);
        submenu.setShortcut('t');


        MenuItem disableMenu = new MenuItem("Disable menu", BLACK_BUS, e->{
            MenuItem source = (MenuItem) e.getSource();
            source.getParent().setEnabled(false);
        });
        submenu.add(disableMenu);


        submenu.add(new MenuItem("Hide tray", LT_GRAY_BUS, e->systemTray.setEnabled(false)));
        submenu.add(new MenuItem("Remove menu", BLACK_FIRE, e->{
            MenuItem source = (MenuItem) e.getSource();
            source.getParent().remove();
        }));
        submenu.add(new MenuItem("Add new entry to tray",
                e->systemTray.getMenu().add(new MenuItem("Random " + new Random().nextInt(10)))));
        mainMenu.add(submenu);

        MenuItem entry = new MenuItem("Type: " + systemTray.getType().toString());
        entry.setEnabled(false);
        systemTray.getMenu().add(entry);

        systemTray.getMenu().add(new MenuItem("Quit", e->{
            systemTray.shutdown();

            Runnable runnable = ()->{
                stage.hide(); // must do this BEFORE Platform.exit() otherwise odd errors show up
                Platform.exit();  // necessary to close javaFx
            };

            if (!RenderProvider.isEventThread()) {
                RenderProvider.dispatch(runnable);
            } else {
                runnable.run();
            }

            //System.exit(0);  not necessary if all non-daemon threads have stopped.
        })).setShortcut('q'); // case does not matter
    }

    private void shutdown() {
        // SWT/JavaFX "shutdown hooks" have changed. Since it's no longer available with JPMS, it is no longer supported.
        // Developers must add the shutdown hooks themselves.
        systemTray.shutdown();
    }
}
