package com.yangteng.library.views.notebook.main.core;

import javafx.scene.layout.BorderPane;

public class NoteCoreView {
    public static final BorderPane INSTANCE = new BorderPane();

    static {
        INSTANCE.setTop(NoteBookMenuView.INSTANCE);
        INSTANCE.setCenter(TabMenuBarView.INSTANCE);
        INSTANCE.setRight(RightToolkitsView.INSTANCE);
        var menus = LeftNoteBookFileTreeView.INSTANCE;{
            INSTANCE.setLeft(menus);
        }
        INSTANCE.setBottom(BottomStateView.INSTANCE);

    }
}
