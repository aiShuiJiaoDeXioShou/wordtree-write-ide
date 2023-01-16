package com.yangteng.library.views.notebook.main.core;

import cn.hutool.core.thread.ThreadUtil;
import com.yangteng.library.App;
import com.yangteng.library.component.WTFxAlert;
import com.yangteng.library.views.notebook.main.dialog.NewProjectDialogView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Objects;

public class NoteBookMenuView extends MenuBar {
    public static final NoteBookMenuView INSTANCE = new NoteBookMenuView();
    public MenuItem openFile,newWorkSpace;
    public Menu file;
    private LeftNoteBookFileTreeView lnbf = LeftNoteBookFileTreeView.INSTANCE;

    public NoteBookMenuView() {
        openFile = new MenuItem("打开工作区间");
        newWorkSpace = new MenuItem("新建工作空间");
        file = new Menu("文件");
        file.getItems().addAll(openFile, newWorkSpace);
        this.getMenus().addAll(file);
        this.controller();
    }

    public void controller() {
        // 打开工作空间
        openWorkSpace();
        // 新建工作空间
        newWorkSpace();
    }

    private void newWorkSpace() {
        newWorkSpace.setOnAction(e -> {
            var newProject = new NewProjectDialogView();
            newProject.showAndWait();
        });
    }

    /**
     * 打开工作空间
     */
    private void openWorkSpace() {
        openFile.setOnAction(event -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("请选择您的工作空间！");
            File file = fileChooser.showDialog(App.primaryStage);
            if (file.getPath().equals(lnbf.nowFile.getPath())) {
                WTFxAlert.show("请不要选择重复的工作空间!");
            } else if (Objects.isNull(file)) {
                WTFxAlert.show("该文件路径不能为空！");
            } else {
                ThreadUtil.execute(() -> {
                    lnbf.toggleFile(file);
                });
            }
        });
    }

}
