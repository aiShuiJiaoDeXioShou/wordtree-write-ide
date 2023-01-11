package com.yangteng.library.views.notebook.main.core;

import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.utils.FxAlertUtils;
import com.yangteng.library.utils.MyFileUtils;
import com.yangteng.library.views.notebook.component.MyCode;
import com.yangteng.library.views.notebook.entity.RecentFiles;
import com.yangteng.library.views.notebook.main.bookrack.BookRackView;
import com.yangteng.library.views.notebook.service.FileService;
import com.yangteng.library.views.notebook.service.WorkSpaceService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

public class LeftNoteBookFileTreeView extends TreeView<Label> {
    public static final LeftNoteBookFileTreeView INSTANCE = new LeftNoteBookFileTreeView();
    private MyFileUtils myFiles;
    public FileService fileService = new FileService() {
    };
    public File nowFile;
    private TreeItem<Label> tree;

    public LeftNoteBookFileTreeView() {
        var recentFiles = WorkSpaceService.get();
        var size = recentFiles.size();
        toggleFile(new File(recentFiles.get(size - 1).filePath));
    }

    private void gc() {
        if (this.tree != null) {
            this.tree.getChildren().remove(0, this.tree.getChildren().size());
            this.tree = null;
            this.myFiles = null;
            System.gc();
        }
    }

    /**
     * 重命名
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param rename
     */
    private static void rename(TreeItem<Label> fileTree, FileService fileService, Label target, @NotNull MenuItem rename) {
        rename.setOnAction(e -> {
            var oldId = target.getId();
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("重新命名您的文件");
            var textField = new TextField();
            textField.setText(target.getText());
            // 文本框设置聚焦包过选中文本
            Platform.runLater(() -> {
                textField.requestFocus();
                textField.selectRange(0, target.getText().length());
            });
            alert.setGraphic(textField);
            var buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                var str = fileService.rename(target.getId(), textField.getText());
                if (str != null) {
                    target.textProperty().setValue(textField.getText());
                    target.idProperty().setValue(str);
                }
                reaname0(fileTree, oldId, str);
            } else e.consume();
        });
    }

    /**
     * 重命名循环方法
     *
     * @param fileTree
     * @param oldId
     * @param str
     */
    private static void reaname0(@NotNull TreeItem<Label> fileTree, String oldId, String str) {
        // 判断是不是目录，如果是目录更改目录下面所有文件的属性
        if (fileTree.getChildren().size() > 0) {
            fileTree.getChildren().forEach(f -> {
                if (f.getChildren().size() > 0) {
                    reaname0(f, oldId, str);
                } else {
                    var t = f.getValue();
                    var id = t.getId();
                    assert str != null;
                    var replace = id.replace(oldId, str);
                    t.idProperty().setValue(replace);
                }
            });
        }
    }

    /**
     * 删除方法
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param del
     */
    private static void menuDelFunction(TreeItem<Label> fileTree, FileService fileService, Label target, @NotNull MenuItem del) {
        del.setOnAction(e -> {
            // 默认删除到回收站
            var bool = fileService.del(target.getId());
            if (bool) {
                // 更新节点
                var parent = fileTree.getParent();
                if (parent != null) {
                    fileTree.getParent().getChildren().remove(fileTree);
                } else FxAlertUtils.show("错误: 这是根节点您不能删除", Alert.AlertType.ERROR);
            } else {
                FxAlertUtils.show("文件删除失败");
            }
        });
    }

    private void treeItemClick(@NotNull TreeItem<Label> fileTree) {
        fileTree.getValue().setOnMouseClicked(event -> {
            // 为文件树添加右键事件
            if (event.getButton().name().equals(MouseButton.SECONDARY.name())) fileMenuAddContextMenu(fileTree);
            // 双击事件
            if (event.getClickCount() >= 2) this.menuDoubleClick(fileTree);
        });
    }

    private void menuDoubleClick(TreeItem<Label> fileTree) {
        ObservableList<Tab> tabs = TabMenuBarView.INSTANCE.getTabs();
        FilteredList<Tab> filtered = tabs.filtered(tab -> tab.getId().equals(fileTree.getValue().getId()));
        if (filtered.size() > 0) {
            Tab tab = filtered.get(0);
            TabMenuBarView.INSTANCE.getSelectionModel().select(tab);
        } else {
            addTab(fileTree);
        }
    }

    private void controller() {
        // 为文件树添加点击事件,为文件树中的文件添加点击事件
        myFiles.getFileList().forEach(this::treeItemClick);
        // 为文件树中的文件夹添加右击事件
        myFiles.getDirList().forEach(fileTree ->
                fileTree.getValue().setOnMouseClicked(event -> {
                    if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                        // 为文件树添加右键事件
                        fileMenuAddContextMenu(fileTree);
                            }
                        }
                )
        );
    }

    /**
     * 为文件树中的文件添加右键菜单，极其响应事件
     *
     * @param fileTree
     */
    private void fileMenuAddContextMenu(@NotNull TreeItem<Label> fileTree) {
        var target = fileTree.getValue();
        var contextMenu = new ContextMenu();

        var cp = new MenuItem("复制");
        cp.setOnAction(e -> fileService.copy(target.getId()));

        var cv = new MenuItem("粘贴");
        cv.setOnAction(e -> {
            var file = new File(target.getId());
            // 对本地文件进行粘贴操作
            var paste = fileService.paste(target.getId());
            // 刷新复制之后的文件夹节点
            paste.forEach(f -> {
                var tree = new MyFileUtils(f).getTree();
                if (tree.getChildren().size() > 0) tree.getChildren().forEach(this::treeItemClick);
                this.treeItemClick(tree);
                if (file.isDirectory())
                    fileTree.getChildren().add(tree);
                else fileTree.getParent().getChildren().add(tree);
            });
        });

        var del = new MenuItem("删除");
        menuDelFunction(fileTree, fileService, target, del);

        var newFile = new MenuItem("新建文件");
        newFile.setOnAction(e -> {
            newFileOrFolder(fileTree, fileService, target, e, 0);
        });

        var newFolder = new MenuItem("新建文件夹");
        newFolder.setOnAction(e -> {
            newFileOrFolder(fileTree, fileService, target, e, 1);
        });

        var openFileDir = new MenuItem("打开文件所在位置");
        openFileDir.setOnAction(e -> {
            fileService.openFileDir(target.getId());
        });

        var rename = new MenuItem("重命名");
        rename(fileTree, fileService, target, rename);

        var upload = new MenuItem("重新刷新");
        upload.setOnAction(e -> {
            this.toggleFile(nowFile);
        });

        contextMenu.getItems().addAll(cp, cv, del, openFileDir, newFile, newFolder, rename, upload);
        contextMenu.show(target, Side.BOTTOM, 0, 0);
    }

    /**
     * 新建文件夹
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param e
     */
    private void newFileOrFolder(TreeItem<Label> fileTree, FileService fileService, @NotNull Label target, ActionEvent e, int type) {
        var bool = new File(target.getId()).isFile();
        TreeItem<Label> parent;
        if (bool) {
            parent = fileTree.getParent();
        } else {
            parent = fileTree;
        }
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("新建");
        var textField = new TextField();
        // 文本框设置聚焦
        Platform.runLater(textField::requestFocus);
        alert.setGraphic(textField);
        var buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            var idStr = parent.getValue().getId() + "/" + textField.getText();
            var treeItem = new TreeItem<Label>();
            var text = new Label(textField.getText());
            text.setId(idStr);
            text.setText(textField.getText());
            text.prefWidth(250);
            treeItem.setValue(text);
            var b = type == 0 ? fileService.createFile(idStr) : fileService.createFolder(idStr);
            if (b) {
                parent.getChildren().add(treeItem);
                // 为节点添加事件
                this.treeItemClick(treeItem);
            }
        } else {
            e.consume();
        }
    }

    /**
     * 每当双击该文件树的时候添加一个tab导航
     *
     * @param fileTree
     */
    private void addTab(@NotNull TreeItem<Label> fileTree) {
        var value = fileTree.getValue();
        String filePath = value.getId();
        var code = new MyCode(new File(filePath));
        var tab = new Tab();
        {
            tab.setContent(code);
            tab.textProperty().bind(value.textProperty());
            tab.idProperty().bind(value.idProperty());
            tab.setGraphic(new Text(""));
            tab.setOnCloseRequest(event -> {
                var target = (Tab) event.getSource();
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
            code.replaceText(context);
            // 只要文本发生了改变，改变tab标签的ui状态
            code.textProperty().addListener((observable, oldValue, newValue) -> {
                tab.setGraphic(new Text("*"));
            });
            TabMenuBarView.INSTANCE.getTabs().add(tab);
            TabMenuBarView.INSTANCE.getSelectionModel().select(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toggleFile(File file) {
        this.nowFile = file;
        new Thread(() -> {
            Platform.runLater(() -> {
                // 回收垃圾保存内存不溢出
                this.gc();
                this.myFiles = new MyFileUtils(file);
                this.tree = this.myFiles.getTree();
                this.controller();
                this.setRoot(this.tree);
            });
            System.gc();
        }).start();
        new Thread(() -> {
            var recentFiles = BookRackView.INSTANCE.recentFiles;
            var filesStream = recentFiles.stream().filter(re -> re.filePath.equals(file.getPath())).toList();
            if (filesStream.size() == 0) {
                var recent = new RecentFiles();
                {
                    recent.time = LocalDateTime.now();
                    recent.filePath = file.getPath();
                    recent.userName = ConfigUtils.getProperties("username");
                }
                recentFiles.add(recent);
                WorkSpaceService.save(recentFiles);
            } else {
                var recentList = recentFiles.stream().peek(re -> {
                    if (re.filePath.equals(file.getPath())) {
                        re.time = LocalDateTime.now();
                    }
                }).toList();
                WorkSpaceService.save(recentList);
            }
        }).start();
        // 刷新BookRackView的UI状态
        new Thread(BookRackView.INSTANCE::update).start();
    }
}