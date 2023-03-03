package lh.wordtree.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * 此列表仅仅用于显示功能
 */
public class WTNoteList extends VBox {

    private List<String> list;

    public WTNoteList(List<String> list) {
        this.list = list;
        this.getStyleClass().add("wt-node-list");
        this.prefHeight(200);
        list.forEach(item -> {
            var label = new Label(item);
            label.getStyleClass().add("wt-node-item");
            this.getChildren().add(label);
        });
    }

}
