package com.yangteng.views.notebook.main.root;

import com.yangteng.views.notebook.main.core.NoteCoreView;
import javafx.scene.layout.HBox;

public class NoteBookRootView extends HBox {
    public final static NoteBookRootView INSTANCE = new NoteBookRootView();
    public NoteBookRootView() {
        this.getChildren().addAll(NoteLeftButtonBarView.INSTANCE, NoteCoreView.INSTANCE);
    }
}
