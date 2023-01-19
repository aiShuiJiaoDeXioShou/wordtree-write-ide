package lh.wordtree.views.notebook.core;

import cn.hutool.core.thread.ThreadUtil;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import lh.wordtree.App;
import lh.wordtree.component.WTMessage;
import lh.wordtree.views.notebook.dialog.NewProjectDialogView;

import java.io.File;

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
            if (file == null) return;
            if (file.getPath().equals(lnbf.nowFile.getPath())) {
                WTMessage.sendWarning("请不要选择重复的工作空间!");
            } else {
                ThreadUtil.execute(() -> {
                    lnbf.toggleFile(file);
                });
            }
        });
    }

}
