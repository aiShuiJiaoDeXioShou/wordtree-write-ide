package lh.wordtree.views.notebook.core;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lh.wordtree.component.WTFileMenu;
import lh.wordtree.entity.RecentFiles;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.file.FileService;
import lh.wordtree.service.record.WorkSpaceService;
import lh.wordtree.utils.ConfigUtils;
import lh.wordtree.views.notebook.bookrack.BookHistoryListView;
import lh.wordtree.views.notebook.root.NoteLeftButtonBarView;

import java.io.File;
import java.time.LocalDateTime;

public class FileTreeView extends TreeView<Label> {
    public static final FileTreeView INSTANCE = new FileTreeView();
    public FileService fileService = new FileService() {
    };
    public File nowFile;
    private TreeItem<Label> tree;

    private void gc() {
        if (this.tree != null) {
            this.tree.getChildren().remove(0, this.tree.getChildren().size());
            this.tree = null;
            System.gc();
        }
    }

    public void toggleFile(File file) {
        // 变更全局根目录
        FactoryBeanService.nowRootFile.set(file);
        this.nowFile = file;
        // 改变路由状态
        NoteLeftButtonBarView.INSTANCE.listView.getSelectionModel().select(1);
        // 执行垃圾回收机制
        ThreadUtil.execAsync(() -> {
            Platform.runLater(() -> {
                // 回收垃圾保存内存不溢出
                this.gc();
                this.tree = new WTFileMenu(this.nowFile);
                this.setRoot(this.tree);
                if (this.getRoot().getChildren().size() > 0) {
                    this.getRoot().setExpanded(true);
                }
            });
            System.gc();
        });
        // 对工作空间的保存操作
        ThreadUtil.execAsync(() -> {
            var recentFiles = BookHistoryListView.INSTANCE.recentFiles;
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