package com.yangteng.views.notebook.main.core;

import com.yangteng.utils.ConfigUtils;
import com.yangteng.utils.FileUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LeftNoteBookFileTreeView extends TreeView<Text> {
    public static final LeftNoteBookFileTreeView INTER = new LeftNoteBookFileTreeView();
    private FileUtils.MyFiles myFiles;
    private TreeItem<Text> tree;
    public LeftNoteBookFileTreeView() {
        super();
        myFiles = FileUtils.getFiles(new File(ConfigUtils.getProperties("defFilePath")));
        tree = myFiles.getTree();
        this.setRoot(tree);
        this.controller();
    }

    public LeftNoteBookFileTreeView toggleFile(File file) {
        myFiles = FileUtils.getFiles(file);
        tree = myFiles.getTree();
        this.setRoot(tree);
        this.controller();
        return this;
    }

    private void controller() {
        // 为文件树添加点击事件
        List<TreeItem<Text>> fileList = myFiles.getFileList();
        for (int i = 0; i < fileList.size(); i++) {
            var fileTree = fileList.get(i);
            fileTree.getValue().setOnMouseClicked(event -> {
                if (event.getClickCount() < 2) return;
                ObservableList<Tab> tabs = TabMenuBarView.INTER.getTabs();
                FilteredList<Tab> filtered = tabs.filtered(tab -> tab.getId().equals(fileTree.getValue().getId()));
                if (filtered.size() > 0) {
                    Tab tab = filtered.get(0);
                    TabMenuBarView.INTER.getSelectionModel().select(tab);
                } else {
                    addTab(fileTree);
                }
            });
        }
    }

    public void addTab(TreeItem<Text> fileTree) {
        String filePath = fileTree.getValue().getId();
        var textArea = new TextArea();{
            textArea.setPrefRowCount(40);
            textArea.setPrefColumnCount(80);
            textArea.setPadding(new Insets(5, 5, 5, 5));
            textArea.setWrapText(true);
        }
        Tab tab = new Tab();{
            tab.setContent(textArea);
            tab.setText(fileTree.getValue().getText());
            tab.setId(filePath);
            tab.setGraphic(new Text("*"));
        }
        try {
            String context = Files.readString(Path.of(filePath));
            textArea.setText(context);
            // 只要文本发生了改变，改变tab标签的ui状态
            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                tab.setGraphic(new Text("*"));
            });
            TabMenuBarView.INTER.getTabs().add(tab);
            TabMenuBarView.INTER.getSelectionModel().select(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
