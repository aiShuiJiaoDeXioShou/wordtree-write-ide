package lh.wordtree.component;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lh.wordtree.App;
import lh.wordtree.comm.config.Config;

public class TreeDialog extends Stage {

    public TreeDialog() {
        this.getIcons().add(new Image(Config.src("static/icon/icon.png")));
        this.setAlwaysOnTop(true);
        this.resizableProperty().setValue(false);
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(App.primaryStage);
    }

}
