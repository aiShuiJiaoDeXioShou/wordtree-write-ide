package lh.wordtree.views.notebook.core;

import javafx.scene.control.TabPane;

public class TabMenuBarView extends TabPane {

    private TabMenuBarView() {
        this.setPrefSize(NoteCoreView.WIDTH * 0.66, NoteCoreView.HEIGHT);
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        this.setTabDragPolicy(TabDragPolicy.REORDER);
    }

    public static TabMenuBarView newInstance() {
        return TabMenuBarViewHolder.instance;
    }

    private static class TabMenuBarViewHolder {
        public static TabMenuBarView instance = new TabMenuBarView();
    }

}
