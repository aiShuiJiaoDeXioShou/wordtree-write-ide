package lh.wordtree.views.notebook.bookrack;

import cn.hutool.core.thread.ThreadUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lh.wordtree.entity.RecentFiles;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.record.WorkSpaceService;

import java.io.File;
import java.util.List;

public class BookHistoryListView extends VBox {
    public final static BookHistoryListView INSTANCE = new BookHistoryListView();
    public List<RecentFiles> recentFiles;
    private ListView<Label> bookHistoryList = new ListView<>();

    public BookHistoryListView() {
        this.myLayout(false);
    }

    public BookHistoryListView(boolean isShowTile) {
        this.myLayout(isShowTile);
    }

    private void myLayout(boolean isShowTile) {
        this.initWorkSpace();
        this.controller();
        this.getChildren().add(bookHistoryList);
        if (!isShowTile) return;
        var box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(6, 0, 6, 0));
        box.getChildren().add(new Label("最近打开的项目"));
        this.getChildren().add(box);
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
        var contextMenu = new ContextMenu();
        {
            var open = new MenuItem("打开");
            var del = new MenuItem("删除");
            var delAndDelFile = new MenuItem("删除并且删除文件夹");
            var details = new MenuItem("查看详情");
            contextMenu.getItems().addAll(open, del, delAndDelFile);
            contextMenu.show(label, Side.BOTTOM, 0, 0);
            open.setOnAction(event -> {
                this.jump(label);
            });
            del.setOnAction(event -> {
                var i = bookHistoryList.getItems().indexOf(label);
                bookHistoryList.getItems().remove(label);
                // 更新文件
                recentFiles.remove(i);
                WorkSpaceService.save(recentFiles);
            });
            delAndDelFile.setOnAction(event -> {
                var i = bookHistoryList.getItems().indexOf(label);
                bookHistoryList.getItems().remove(label);
                recentFiles.remove(i);
                // 更新文件
                WorkSpaceService.save(recentFiles);
                ThreadUtil.execAsync(() -> {
                    var file = new File(label.getId());
                    file.deleteOnExit();
                });
            });
            details.setOnAction(e -> {
            });
        }
    }

    // 跳转到IDE区域
    private void jump(Label label) {
        if (label.getId().equals(FactoryBeanService.nowRootFile.getValue().getPath())) {
            return;
        }
        FactoryBeanService.nowRootFile.set(new File(label.getId()));
    }
}
