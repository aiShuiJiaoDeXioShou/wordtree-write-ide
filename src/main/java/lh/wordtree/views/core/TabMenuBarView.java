package lh.wordtree.views.core;

import javafx.scene.control.TabPane;
import lh.wordtree.comm.BeanFactory;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;

public class TabMenuBarView extends TabPane {

    private TabMenuBarView() {
        this.setPrefSize(NoteCoreView.WIDTH * 0.66, NoteCoreView.HEIGHT);
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        this.setTabDragPolicy(TabDragPolicy.REORDER);
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getContent() instanceof VirtualizedScrollPane content) {
                BeanFactory.nowCodeArea.set((CodeArea) content.getContent());
            }
            String path = newValue.getId();
            if (!path.isBlank()) {
                File file = new File(path);
                if (file.exists()) {
                    BeanFactory.nowFile.set(file);
                }
            }
        });
    }

    public static TabMenuBarView newInstance() {
        return TabMenuBarViewHolder.instance;
    }

    private static class TabMenuBarViewHolder {
        public static TabMenuBarView instance = new TabMenuBarView();
    }

}
