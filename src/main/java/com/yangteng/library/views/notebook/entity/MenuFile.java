package com.yangteng.library.views.notebook.entity;

import com.yangteng.library.views.notebook.component.FileMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuFile {
    private List<File> files;
    private File root;
    private TreeItem<Label> treeRoot;
    private List<TreeItem<Label>> dirList = null;
    private List<TreeItem<Label>> fileList = null;

    public MenuFile(File root) {
        this.root = root;
    }


    private void getFiles1(File file, TreeItem<Label> treeItem) {
        File[] listFiles = file.listFiles();
        assert listFiles != null;
        for (File f : Arrays.stream(listFiles).sorted().toList()) {
            var item = new FileMenu(f);
            if (f.isDirectory()) {
                dirList.add(item);
                getFiles1(f, item);
            } else {
                fileList.add(item);
            }
            treeItem.getChildren().add(item);
        }
    }

    private void getFiles0(File file) {
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) {
                getFiles0(f);
            } else {
                files.add(f);
            }
        }
    }

    public List<File> getFiles() {
        files = new ArrayList<>();
        getFiles0(root);
        return files;
    }

    public TreeItem<Label> getTree() {
        dirList = new ArrayList<>();
        fileList = new ArrayList<>();
        treeRoot = new FileMenu(root);
        if (root.isDirectory()) getFiles1(root, treeRoot);
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
