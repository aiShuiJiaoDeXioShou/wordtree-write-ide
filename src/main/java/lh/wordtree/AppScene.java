package lh.wordtree;

import javafx.scene.Scene;
import lh.wordtree.comm.config.Config;
import lh.wordtree.service.Subscribe;
import lh.wordtree.service.Subscriber;

public class AppScene extends Scene {
    public AppScene() {
        super(App.rootPane, Config.APP_WIDTH, Config.APP_HEIGHT);
        App.rootPane.getChildren().clear();
        App.rootPane.getChildren().add(RootView.newInstance());
    }

    public static AppScene newInstance() {
        return NoteBookSceneHolder.instance;
    }

    private static class NoteBookSceneHolder {
        public static AppScene instance = new AppScene();
    }

}
