package lh.wordtree.handler;

import javafx.scene.Node;

public interface EditorLanguageHandler<T extends Node, F> {
    T view(F f);

    String name();
}
