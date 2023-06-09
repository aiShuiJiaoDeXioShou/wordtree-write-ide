package lh.wordtree.component;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;

public class TreeStage extends Stage {

    public TreeStage() {
        this.getIcons().add(new Image(Config.src("static/icon/icon.png")));
    }

}
