package lh.wordtree;

import javafx.scene.layout.BorderPane;
import lh.wordtree.model.core.NoteCoreView;
import lh.wordtree.model.router.NavigationView;

public class RootView extends BorderPane {
    public RootView() {
        this.setLeft(NavigationView.newInstance());
        this.setCenter(NoteCoreView.newInstance());
    }

    public static RootView newInstance() {
        return NoteBookRootViewHolder.instance;
    }

    private static class NoteBookRootViewHolder {
        public static RootView instance = new RootView();
    }
}
