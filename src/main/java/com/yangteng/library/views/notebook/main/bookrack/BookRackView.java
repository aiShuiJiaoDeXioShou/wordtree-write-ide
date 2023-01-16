package com.yangteng.library.views.notebook.main.bookrack;

import cn.hutool.core.thread.ThreadUtil;
import com.yangteng.library.utils.JDBCUtils;
import com.yangteng.library.views.notebook.entity.RecentFiles;
import com.yangteng.library.views.notebook.main.core.LeftNoteBookFileTreeView;
import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import com.yangteng.library.views.notebook.main.root.NoteBookRootView;
import com.yangteng.library.views.notebook.mapper.AuthorTaskMapper;
import com.yangteng.library.views.notebook.service.WorkSpaceService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.List;

public class BookRackView extends BorderPane {

    public final static BookRackView INSTANCE = new BookRackView();;
    public List<RecentFiles> recentFiles;
    public ListView<Label> bookHistoryList;
    private AuthorTaskMapper authorTaskMapper = JDBCUtils.getSqlSessionFactory().getMapper(AuthorTaskMapper.class);

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
        this.setPadding(new Insets(0, 0, 10, 0));
        var vBox = new VBox();
        {
            var box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(6, 0, 6, 0));
            box.getChildren().add(new Label("最近打开的项目"));
            vBox.getChildren().add(box);
        }
        vBox.getChildren().add(bookHistoryList);
        this.setLeft(vBox);
        var centerBox = new VBox();
        {
            var title = new VBox();
            title.getChildren().add(new Label("任务管理"));
            title.setPadding(new Insets(0, 0, 10, 0));
            var labelListView = new ListView<Label>();
            {
                labelListView.prefHeightProperty().bind(this.heightProperty());
                ThreadUtil.execAsync(()->{
                    // 查询该用户所有创建过的任务
                    var authorTasks = authorTaskMapper.selectAll();
                    Platform.runLater(()->{
                        authorTasks.forEach(authorTask -> {
                            var label = new Label();
                            label.setText(authorTask.getDescribe());
                            label.setGraphic(new Text(authorTask.getNumber().toString()));
                            labelListView.getItems().add(label);
                        });
                    });
                });
            }
            centerBox.getChildren().addAll(title, labelListView);
        }
        this.setCenter(centerBox);
    }

    private void initWorkSpace() {
        if (bookHistoryList != null) {
            bookHistoryList.getItems().remove(0, bookHistoryList.getItems().size());
            System.gc();
        } else {
            bookHistoryList = new ListView<>();
            {
                bookHistoryList.prefHeightProperty().bind(this.heightProperty());
            }
        }
        // 获取工作空间的历史所有数据
        recentFiles = WorkSpaceService.get();
        for (RecentFiles recentFile : recentFiles) {
            var label = new Label();
            label.setGraphic(new Text(recentFile.workspaceName()));
            label.setId(recentFile.filePath());
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
                WorkSpaceService.save(recentFiles);
            });
            details.setOnAction(e->{

            });
        }
    }

    // 跳转到IDE区域
    private void jump(Label label) {
        NoteBookRootView.INSTANCE.setCenter(NoteCoreView.INSTANCE);
        if (label.getId().equals(LeftNoteBookFileTreeView.INSTANCE.nowFile.getPath())) {
            return;
        }
        LeftNoteBookFileTreeView.INSTANCE.toggleFile(new File(label.getId()));
    }
}
