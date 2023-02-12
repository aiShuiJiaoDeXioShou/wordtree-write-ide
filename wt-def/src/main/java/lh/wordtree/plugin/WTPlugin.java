package lh.wordtree.plugin;

import javafx.scene.Node;

public interface WTPlugin {
    void init();

    void apply();

    default void write() {
        throw new UnsupportedOperationException();
    }

    default void toggleFile() {
        throw new UnsupportedOperationException();
    }

    default void loop() {
        throw new UnsupportedOperationException();
    }

    default void save() {
        throw new UnsupportedOperationException();
    }

    default Node view() {
        throw new UnsupportedOperationException();
    }

    void end();

    WTPluginConfig config();
}
