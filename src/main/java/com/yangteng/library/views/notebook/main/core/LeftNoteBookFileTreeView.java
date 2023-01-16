package com.yangteng.library.views.notebook.main.core;

import cn.hutool.core.thread.ThreadUtil;
import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.views.notebook.entity.RecentFiles;
import com.yangteng.library.views.notebook.main.bookrack.BookRackView;
import com.yangteng.library.views.notebook.main.root.NoteLeftButtonBarView;
import com.yangteng.library.views.notebook.service.FileService;
import com.yangteng.library.views.notebook.service.MenuFileService;
import com.yangteng.library.views.notebook.service.WorkSpaceService;
import com.yangteng.library.views.notebook.service.impl.MenuFileServiceImpl;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.time.LocalDateTime;

public class LeftNoteBookFileTreeView extends TreeView<Label> {
    public static final LeftNoteBookFileTreeView INSTANCE = new LeftNoteBookFileTreeView();
    private MenuFileService myFiles;
    public FileService fileService = new FileService() {
    };
    public File nowFile;
    private TreeItem<Label> tree;

    public LeftNoteBookFileTreeView() {
        var recentFiles = WorkSpaceService.get();
        var size = recentFiles.size();
        this.nowFile = new File(recentFiles.get(0).filePath());
        toggleFile(this.nowFile);
    }

    private void gc() {
        if (this.tree != null) {
            this.tree.getChildren().remove(0, this.tree.getChildren().size());
            this.tree = null;
            this.myFiles = null;
            System.gc();
        }
    }


    public void toggleFile(File file) {
        this.nowFile = file;
        // 改变路由状态
        NoteLeftButtonBarView.INSTANCE.listView.getSelectionModel().select(1);
        // 执行垃圾回收机制
        ThreadUtil.execAsync(() -> {
            Platform.runLater(() -> {
                // 回收垃圾保存内存不溢出
                this.gc();
                this.myFiles = new MenuFileServiceImpl(file);
                this.tree = this.myFiles.getTree();
                this.setRoot(this.tree);
            });
            System.gc();
        });
        // 对工作空间的保存操作
        ThreadUtil.execAsync(() -> {
            var recentFiles = BookRackView.INSTANCE.recentFiles;
            var filesStream = recentFiles.stream().filter(re -> re.filePath().equals(file.getPath())).toList();
            // 判断打开的工作空间是否存在
            if (filesStream.size() > 0)
                recentFiles = recentFiles.stream().filter(re -> !re.filePath().equals(file.getPath())).toList();
            var recent = new RecentFiles(LocalDateTime.now(), file.getPath(), ConfigUtils.getProperties("username"), file.getName());
            recentFiles.add(recent);
            recentFiles.sort((o1, o2) -> o1.time().isBefore(o2.time()) ? 0 : -1);
            WorkSpaceService.save(recentFiles);
        });
    }
}