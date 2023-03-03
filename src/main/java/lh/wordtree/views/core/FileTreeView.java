package lh.wordtree.views.core;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lh.wordtree.component.CpFileMenu;
import lh.wordtree.entity.RecentFiles;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.file.FileService;
import lh.wordtree.service.record.WorkSpaceService;
import lh.wordtree.views.root.NoteLeftButtonBarView;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

public class FileTreeView extends TreeView<Label> {
    private FileTreeView() {

    }

    public static FileTreeView newInstance() {
        return FileTreeViewHolder.instance;
    }

    public void toggleFile(File file) {
        // 变更全局根目录
        FactoryBeanService.nowRootFile.set(file);
        this.nowFile = file;
        // 改变路由状态
        NoteLeftButtonBarView.newInstance().listView.getSelectionModel().select(1);
        // 执行垃圾回收机制
        ThreadUtil.execAsync(() -> {
            Platform.runLater(() -> {
                // 回收垃圾保存内存不溢出
                this.gc();
                this.tree = new CpFileMenu(this.nowFile);
                this.setRoot(this.tree);
                if (this.getRoot().getChildren().size() > 0) {
                    this.getRoot().setExpanded(true);
                }
            });
            System.gc();
        });
        ThreadUtil.execAsync(this::flushWorkSpace);
    }

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

    private static class FileTreeViewHolder {
        public static FileTreeView instance = new FileTreeView();
    }

    private void flushWorkSpace() {
        // 对工作空间的保存操作
        var file = FactoryBeanService.nowRootFile.getValue();
        List<RecentFiles> recentFiles = WorkSpaceService.get();
        var filesStream = recentFiles.stream().filter(re -> re.getFilePath().equals(file.getPath())).toList();
        if (filesStream.size() == 0) return;
        // 判断打开的工作空间是否存在
        var files = recentFiles.stream().peek(re -> {
            if (!re.getFilePath().equals(file.getPath())) {
                re.setTime(LocalDateTime.now());
            }
        }).sorted((o1, o2) -> (int) (o1.getTime().getLong(ChronoField.SECOND_OF_DAY) - o2.getTime().getLong(ChronoField.SECOND_OF_DAY))).toList();
        WorkSpaceService.save(files);
    }
}