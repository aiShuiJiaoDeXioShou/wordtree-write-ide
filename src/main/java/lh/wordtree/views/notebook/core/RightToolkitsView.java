package lh.wordtree.views.notebook.core;

import javafx.scene.layout.AnchorPane;

// 右侧的小组件栏
public class RightToolkitsView extends AnchorPane {
    public static final RightToolkitsView INSTANCE = new RightToolkitsView();

    public RightToolkitsView() {
        this.getStyleClass().add("right-toolkits");
        this.setPrefSize(NoteCoreView.WIDTH * 0.17, NoteCoreView.HEIGHT);
    }
}
