package lh.wordtree.views.root;

import javafx.scene.Scene;
import lh.wordtree.App;

public class NoteBookScene extends Scene {
    public NoteBookScene() {
        super(App.rootPane);
        App.rootPane.getChildren().clear();
        App.rootPane.getChildren().add(NoteBookRootView.newInstance());
    }

    public static NoteBookScene newInstance() {
        return NoteBookSceneHolder.instance;
    }

    private static class NoteBookSceneHolder {
        public static NoteBookScene instance = new NoteBookScene();
    }

}
