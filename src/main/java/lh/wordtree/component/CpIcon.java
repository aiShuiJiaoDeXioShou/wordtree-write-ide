package lh.wordtree.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CpIcon extends Label {
    private ImageView imageView;

    public CpIcon(String icon, String id) {
        imageView = new ImageView(new Image(icon));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        this.setGraphic(imageView);
        this.getStyleClass().addAll("icon-font", "mdl2-assets", "note-left-item");
        this.setId(id);
    }

    public CpIcon(Image icon, String id) {
        imageView = new ImageView(icon);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        this.setGraphic(imageView);
        this.getStyleClass().addAll("icon-font", "mdl2-assets", "note-left-item");
        this.setId(id);
    }

    public ImageView imageView() {
        return imageView;
    }
}
