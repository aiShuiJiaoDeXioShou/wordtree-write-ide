package lh.wordtree.plugin;

import javafx.scene.Node;

public interface WTPlugin {
    void init();

    default void write() {
    }

    default void toggleFile() {
    }

    default void loop() {
    }

    default void save() {
    }

    default Node view() {
        return null;
    }

    void end();

    WTPluginConfig config();
}
