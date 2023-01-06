package com.yangteng.views.notebook.main.core;

import com.yangteng.Index;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class NoteBookMenuView extends MenuBar {
    public static final NoteBookMenuView INSTANCE = new NoteBookMenuView();
    public MenuItem openFile;
    public Menu file;
    private LeftNoteBookFileTreeView lnbf = LeftNoteBookFileTreeView.INSTANCE;

    public NoteBookMenuView() {
        openFile = new MenuItem("打开工作区间");
        file = new Menu("文件");
        file.getItems().addAll(openFile);
        this.getMenus().addAll(file);
        this.controller();
    }

    public void controller() {
        file.setOnAction(event -> {
            DirectoryChooser fileChooser = new DirectoryChooser();{
                fileChooser.setTitle("请选择您的工作空间！");
                File file = fileChooser.showDialog(Index.primaryStage);
                if (file != null) {
                    lnbf.toggleFile(file);
                }
            }
        });
    }

}
