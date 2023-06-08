package lh.wordtree.component;

import cn.hutool.core.io.FileUtil;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.comm.entity.NovelProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BookComponent extends HBox {

    public BookComponent(NovelProject novelProject) {
        this.getStyleClass().add("wtbook");
        var name = new Label(novelProject.getName());
        name.getStyleClass().add("wtbook-name");
        var author = new Label(novelProject.getAuthor());
        author.getStyleClass().add("wtbook-author");
        var brief = new Label(novelProject.getBriefIntroduction());
        brief.getStyleClass().add("wtbook-brief");
        var image = new ImageView();
        Image img = null;
        if (FileUtil.exist(novelProject.getImg())) {
            try {
                img = new Image(new FileInputStream(novelProject.getImg()), 150, 150, true, true);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (img != null) image.setImage(img);
        var box = new VBox();
        box.getChildren().addAll(name, author, brief);
        box.setSpacing(15);
        this.getChildren().addAll(image, box);
        this.setSpacing(20);
    }

}
