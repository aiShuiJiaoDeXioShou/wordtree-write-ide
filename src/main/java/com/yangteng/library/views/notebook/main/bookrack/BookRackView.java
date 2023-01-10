package com.yangteng.library.views.notebook.main.bookrack;

import com.yangteng.library.views.notebook.entity.RecentFiles;
import com.yangteng.library.views.notebook.main.core.LeftNoteBookFileTreeView;
import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import com.yangteng.library.views.notebook.main.root.NoteBookRootView;
import com.yangteng.library.views.notebook.dao.WorkSpaceMapper;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class BookRackView extends BorderPane {

    public final static BookRackView INSTANCE = new BookRackView();;
    public List<RecentFiles> recentFiles;
    public ListView<Label> bookHistoryList;

    public BookRackView() {
        this.initWorkSpace();
        this.myLayout();
        this.controller();
    }

    public void update() {
        this.initWorkSpace();
        this.myLayout();
        this.controller();
    }

    private void myLayout() {
        this.setPrefSize(700, 600);
        this.setPadding(new Insets(0,0,10,0));
        this.setLeft(bookHistoryList);
    }

    private void initWorkSpace() {
        if (bookHistoryList != null) {
            bookHistoryList.getItems().remove(0, bookHistoryList.getItems().size());
            System.gc();
        }
        bookHistoryList = new ListView<>();
        // 获取工作空间的历史所有数据
        recentFiles = WorkSpaceMapper.get();
        for (RecentFiles recentFile : recentFiles) {
            var label = new Label(recentFile.filePath);
            label.setGraphic(new Text(recentFile.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            bookHistoryList.getItems().add(label);
        }
    }

    private void controller() {
        // 为选中状态添加事件
        bookHistoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
        // 为双击或者右击事件添加操作
        bookHistoryList.getItems().forEach(label -> {
            label.setOnMouseClicked(event -> {
                if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                    this.bookHistoryRightClick(label);
                }
                // 添加双击事件,打开指定工作空间
                if (event.getClickCount() >= 2) {
                    // 跳转到IDE区域
                    this.jump(label);
                }
            });
        });
    }

    private void bookHistoryRightClick(Label label) {
        var contextMenu = new ContextMenu();{
            var open = new MenuItem("打开");
            var del = new MenuItem("删除");
            var delAndDelFile = new MenuItem("删除并且删除文件夹");
            var details = new MenuItem("查看详情");
            contextMenu.getItems().addAll(open, del,delAndDelFile);
            contextMenu.show(label, Side.BOTTOM, 0, 0);
            open.setOnAction(event -> {
                this.jump(label);
            });
            del.setOnAction(event -> {
                bookHistoryList.getItems().remove(label);
                // 更新文件
                recentFiles.remove(bookHistoryList.getItems().indexOf(label));
                WorkSpaceMapper.save(recentFiles);
            });
            details.setOnAction(e->{

            });
        }
    }

    // 跳转到IDE区域
    private void jump(Label label) {
        NoteBookRootView.INSTANCE.setCenter(NoteCoreView.INSTANCE);
        if (label.getText().equals(LeftNoteBookFileTreeView.INSTANCE.nowFile.getPath())) {
            return;
        }
        LeftNoteBookFileTreeView.INSTANCE.toggleFile(new File(label.getText()));
    }
}
