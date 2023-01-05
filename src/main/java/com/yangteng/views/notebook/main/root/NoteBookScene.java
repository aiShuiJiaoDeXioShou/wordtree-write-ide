package com.yangteng.views.notebook.main.root;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class NoteBookScene extends Scene {
    public final static NoteBookScene INTER = new NoteBookScene(NoteBookRootView.INTER);
    public NoteBookScene(Parent root) {
        super(root);
    }

}
