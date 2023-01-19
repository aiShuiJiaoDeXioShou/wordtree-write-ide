package lh.wordtree.views.notebook.root;

import javafx.scene.layout.BorderPane;
import lh.wordtree.views.notebook.core.NoteCoreView;

public class NoteBookRootView extends BorderPane {
    public final static NoteBookRootView INSTANCE = new NoteBookRootView();
    public NoteBookRootView() {
        this.setLeft(NoteLeftButtonBarView.INSTANCE);
        this.setCenter(NoteCoreView.INSTANCE);
    }
}
