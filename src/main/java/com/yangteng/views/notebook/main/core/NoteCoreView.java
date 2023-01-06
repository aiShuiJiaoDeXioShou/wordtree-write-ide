package com.yangteng.views.notebook.main.core;

import javafx.scene.layout.BorderPane;

public class NoteCoreView {
    public static final BorderPane INTER = new BorderPane();

    static {
        INTER.setTop(NoteBookMenuView.INTER);
        INTER.setCenter(TabMenuBarView.INTER);
        var menus = LeftNoteBookFileTreeView.INTER;{
            INTER.setLeft(menus);
        }

    }
}
