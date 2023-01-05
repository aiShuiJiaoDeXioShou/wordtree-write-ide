package com.yangteng.views.notebook.main.core;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class NoteBookMenuView extends MenuBar {
    public static final NoteBookMenuView INTER = new NoteBookMenuView();

    public NoteBookMenuView() {
        MenuItem openFile = new MenuItem("打开工作区间");
        Menu file = new Menu("文件");
        file.getItems().addAll(openFile);
        this.getMenus().addAll(file);
    }

}
