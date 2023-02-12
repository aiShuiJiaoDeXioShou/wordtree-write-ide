package lh.wordtree.handler.editor;

import javafx.scene.Node;

public interface EditorLanguageHandler<T extends Node, F> {
    T view(F f);

    String name();
}
