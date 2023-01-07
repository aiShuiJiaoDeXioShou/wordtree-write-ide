package com.yangteng.library.views.notebook.main.root;

import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import javafx.scene.layout.BorderPane;

public class NoteBookRootView extends BorderPane {
    public final static NoteBookRootView INSTANCE = new NoteBookRootView();
    public NoteBookRootView() {
        this.setLeft(NoteLeftButtonBarView.INSTANCE);
        this.setCenter(NoteCoreView.INSTANCE);
    }
}
