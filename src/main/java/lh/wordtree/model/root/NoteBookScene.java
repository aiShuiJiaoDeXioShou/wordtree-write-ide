package lh.wordtree.model.root;

import javafx.scene.Scene;
import lh.wordtree.App;
import lh.wordtree.comm.config.Config;

public class NoteBookScene extends Scene {
    public NoteBookScene() {
        super(App.rootPane, Config.APP_WIDTH, Config.APP_HEIGHT);
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
