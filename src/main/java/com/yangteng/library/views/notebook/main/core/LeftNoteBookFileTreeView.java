package com.yangteng.library.views.notebook.main.core;

import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        // 为文件树中的文件添加点击事件
        for (TreeItem<Text> fileTree : myFiles.getFileList()) {
            // 为文件树添加点击事件
            fileTree.getValue().setOnMouseClicked(event -> {
                if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                    // 为文件树添加右键事件
                    fileMenuAddContextMenu(fileTree);
                }
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
        }

        // 为文件树中的文件夹添加右击事件
        for (TreeItem<Text> fileTree : myFiles.getDirList()) {
            fileTree.getValue().setOnMouseClicked(event -> {
                if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                    // 为文件树添加右键事件
                    fileMenuAddContextMenu(fileTree);
                }
            });
        }
    }

    /**
     * 为文件树中的文件添加右键菜单，极其响应事件
     * @param fileTree
     */
    private void fileMenuAddContextMenu(TreeItem<Text> fileTree) {
        var target = fileTree.getValue();
        var contextMenu = new ContextMenu();{
            var cp = new MenuItem("复制");
            var cv = new MenuItem("粘贴");
            var del = new MenuItem("删除");
            var openFileDir = new MenuItem("打开文件夹");
            contextMenu.getItems().addAll(cp, cv, del, openFileDir);
        }
        contextMenu.show(target,Side.BOTTOM, 0, 0);
    }

    /**
     * 每当双击该文件树的时候添加一个tab导航
     * @param fileTree
     */
    private void addTab(TreeItem<Text> fileTree) {
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
