package com.yangteng.views.notebook.main.core;

import com.yangteng.utils.ConfigUtils;
import com.yangteng.utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class LeftNoteBookFileTreeView extends TreeView<Text> {
    public static final LeftNoteBookFileTreeView INSTANCE = new LeftNoteBookFileTreeView();
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
            // 为文件树添加点击事件
            fileTree.getValue().setOnMouseClicked(event -> {
                if (event.getClickCount() < 2) return;
                ObservableList<Tab> tabs = TabMenuBarView.INSTANCE.getTabs();
                FilteredList<Tab> filtered = tabs.filtered(tab -> tab.getId().equals(fileTree.getValue().getId()));
                if (filtered.size() > 0) {
                    Tab tab = filtered.get(0);
                    TabMenuBarView.INSTANCE.getSelectionModel().select(tab);
                } else {
                    addTab(fileTree);
                }
            });

            // 为文件树添加右键事件

        }
    }

    // 添加右键菜单
    public void addContextMenu(TreeItem<Text> fileTree) {
        var target = fileTree.getValue();
        var contextMenu = new ContextMenu();

    }

    public void addTab(TreeItem<Text> fileTree) {
        String filePath = fileTree.getValue().getId();
        var textArea = new TextArea();
        {
            textArea.setPrefRowCount(40);
            textArea.setPrefColumnCount(80);
            textArea.setPadding(new Insets(5, 5, 5, 5));
            textArea.setWrapText(true);
        }
        var tab = new Tab();
        {
            tab.setContent(textArea);
            tab.setText(fileTree.getValue().getText());
            tab.setId(filePath);
            tab.setGraphic(new Text(""));
            tab.setOnCloseRequest(event -> {
                var target = (Tab)event.getSource();
                var graphic = (Text) target.getGraphic();
                var iconText = graphic.getText();
                if (iconText.equals("*")) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("还没有保存文件是否关闭？");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        alert.close();
                    } else event.consume();
                }
            });
        }
        try {
            String context = Files.readString(Path.of(filePath));
            textArea.setText(context);
            // 只要文本发生了改变，改变tab标签的ui状态
            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                tab.setGraphic(new Text("*"));
            });
            TabMenuBarView.INSTANCE.getTabs().add(tab);
            TabMenuBarView.INSTANCE.getSelectionModel().select(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
