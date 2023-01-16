package com.yangteng.library.component;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WTDialog extends Stage {

    public WTDialog(Parent root, Window window) {
        var scene = new Scene(root);
        this.initOwner(window);
        this.initModality(Modality.WINDOW_MODAL);
        this.setScene(scene);
    }

}
