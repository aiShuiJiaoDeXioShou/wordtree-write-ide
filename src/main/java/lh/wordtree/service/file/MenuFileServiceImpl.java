package lh.wordtree.service.file;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import lh.wordtree.component.CpFileMenu;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuFileServiceImpl implements MenuFileService {
    private List<File> files;
    private File root;
    private TreeItem<Label> treeRoot;
    private List<TreeItem<Label>> dirList = null;
    private List<TreeItem<Label>> fileList = null;

    public MenuFileServiceImpl(File root) {
        this.root = root;
    }


    private void dirFileTreeItem(@NotNull File file, TreeItem<Label> treeItem) {
        File[] listFiles = file.listFiles();
        assert listFiles != null;
        for (File f : Arrays.stream(listFiles).sorted((o1, o2) -> {
            if (o1.isDirectory() == o2.isDirectory()) {
                return o1.compareTo(o2);
            } else {
                if (o1.isDirectory()) {
                    return -1;
                } else return 0;
            }
        }).toList()) {
            var item = new CpFileMenu(f);
            if (f.isDirectory()) {
                dirList.add(item);
                dirFileTreeItem(f, item);
            } else {
                fileList.add(item);
            }
            treeItem.getChildren().add(item);
        }
    }

    private void dirFiles(@NotNull File file) {
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) {
                dirFiles(f);
            } else {
                files.add(f);
            }
        }
    }

    public List<File> getFiles() {
        files = new ArrayList<>();
        dirFiles(root);
        return files;
    }

    public TreeItem<Label> getTree() {
        dirList = new ArrayList<>();
        fileList = new ArrayList<>();
        treeRoot = new CpFileMenu(root);
        if (root.isDirectory()) dirFileTreeItem(root, treeRoot);
        return treeRoot;
    }

    public List<TreeItem<Label>> getDirList() {
        if (dirList == null) throw new RuntimeException("你的先启用getTree方法");
        return dirList;
    }

    public List<TreeItem<Label>> getFileList() {
        if (fileList == null) throw new RuntimeException("你的先启用getTree方法");
        return fileList;
    }
}
