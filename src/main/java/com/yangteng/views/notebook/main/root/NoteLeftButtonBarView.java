package com.yangteng.views.notebook.main.root;

import com.yangteng.views.notebook.component.NoteLeftButtonItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NoteLeftButtonBarView extends VBox {

    public static final NoteLeftButtonBarView INTER = new NoteLeftButtonBarView();

    public NoteLeftButtonBarView() {
        this.setPrefWidth(30);
        this.setPadding(new Insets(10));
        NoteLeftButtonItem fliesItem = new NoteLeftButtonItem("icon/flies.png", "书籍管理");
        NoteLeftButtonItem writeItem = new NoteLeftButtonItem("icon/write.png", "写作");
        this.setSpacing(10);
        this.getChildren().addAll(fliesItem, writeItem);
    }

}
