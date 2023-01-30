package lh.wordtree;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import lh.wordtree.config.Config;

public class ConfirmWindow extends Window {
    public final VBox root = new VBox();

    public ConfirmWindow() {
        var scene = new Scene(root);
        Config.setStyle(scene);
        this.setScene(scene);
        this.show();
    }
}
