package lh.wordtree.views.tray;

import dorkbox.jna.rendering.ProviderType;
import dorkbox.jna.rendering.Renderer;
import dorkbox.os.OS;

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
        // this is only true for SWT. JavaFX running detection is elsewhere
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
