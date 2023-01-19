package lh.wordtree.views.notebook.core;

import javafx.scene.control.ScrollPane;

// 右侧的小组件栏
public class RightToolkitsView extends ScrollPane {
    public static final RightToolkitsView INSTANCE = new RightToolkitsView();
    public RightToolkitsView() {
        this.setPrefSize(NoteCoreView.WIDTH * 0.17, NoteCoreView.HEIGHT);
    }
}
