package lh.wordtree.views.toolbox.home;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class IndexView extends HBox {
    public final static IndexView INSTANCE = new IndexView();
    public IndexView() {
        this.getChildren().addAll(new Label("我是首页"));
    }
}
