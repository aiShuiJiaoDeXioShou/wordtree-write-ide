package lh.wordtree.handler;

import javafx.scene.Node;

public interface EditorLanguageFunHandler<T extends Node, F> extends EditorLanguageHandler<Node, F> {
    T view(F f);

    default String name() {
        throw new UnsupportedOperationException();
    }
}
