package lh.wordtree.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CpIcon extends Label {
    public CpIcon(String icon, String id) {
        var imageView = new ImageView(new Image(icon));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        this.setGraphic(imageView);
        this.getStyleClass().addAll("icon-font", "mdl2-assets", "note-left-item");
        this.setId("书架管理");
    }
}
