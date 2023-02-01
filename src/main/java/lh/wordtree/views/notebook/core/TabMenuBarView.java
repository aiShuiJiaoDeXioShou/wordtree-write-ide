package lh.wordtree.views.notebook.core;

import javafx.scene.control.TabPane;

public class TabMenuBarView extends TabPane {

    public static final TabMenuBarView INSTANCE = new TabMenuBarView();

    public TabMenuBarView() {
        this.setPrefSize(NoteCoreView.WIDTH * 0.66, NoteCoreView.HEIGHT);
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        this.setTabDragPolicy(TabDragPolicy.REORDER);
    }

}
