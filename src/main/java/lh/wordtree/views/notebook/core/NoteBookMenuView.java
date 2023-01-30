package lh.wordtree.views.notebook.core;

import cn.hutool.core.thread.ThreadUtil;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import lh.wordtree.App;
import lh.wordtree.component.WTMessage;
import lh.wordtree.component.WTOneWindow;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.views.notebook.bookrack.BookHistoryListView;
import lh.wordtree.views.notebook.newbox.NewProjectDialogView;

import java.io.File;

public class NoteBookMenuView extends BorderPane {
    public static final NoteBookMenuView INSTANCE = new NoteBookMenuView();
    public HBox actionBar;
    private MenuItem openFile, newWorkSpace;
    private Menu file;
    private MenuBar menuBar;
    private Button update;
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    public Label toggleWorkSpace;
    private FileTreeView lnbf = FileTreeView.INSTANCE;

    public NoteBookMenuView() {
        this.getStyleClass().add("note-book-menu");
        openFile = new MenuItem("打开工作区间");
        newWorkSpace = new MenuItem("新建工作空间");
        file = new Menu("文件");
        file.getItems().addAll(openFile, newWorkSpace);
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file);
        this.setLeft(menuBar);

        // 中间显示工作空间状态
        toggleWorkSpace = new Label();
        {
            FactoryBeanService.nowRootFile.addListener((observable, oldValue, newValue) -> {
                toggleWorkSpace.setText(newValue.getName());
            });
        }
        toggleWorkSpace.getStyleClass().add("toggle-work-space");
        this.setCenter(toggleWorkSpace);
        toggleWorkSpace.setOnMouseClicked(e -> {
            var stage = new WTOneWindow("历史工作空间");
            stage.getRoot().getChildren().addAll(BookHistoryListView.INSTANCE);
            stage.getLabel().setStyle("-fx-text-fill: #ffff");
            stage.getTop().setStyle("-fx-background-color: #495057");
            stage.show();
        });

        // 右侧编译文件树
        actionBar = new HBox();
        {
            // 这里有一个选择button
            update = new Button("执行");
            // 创建选择框组件
            choiceBox.getItems().addAll("上传", "编译");
            choiceBox.getSelectionModel().select(0);
            actionBar.getChildren().addAll(choiceBox, update);
            actionBar.setSpacing(10);
        }
        this.getStyleClass().add("note-menu-bar");
        this.setPadding(new Insets(0, 30, 0, 0));
        this.setRight(actionBar);
        this.controller();
    }

    public void controller() {
        // 打开工作空间
        openWorkSpace();
        // 新建工作空间
        newWorkSpace();
    }

    private void newWorkSpace() {
        newWorkSpace.setOnAction(e -> {
            var newProject = new NewProjectDialogView();
            newProject.showAndWait();
        });
    }

    /**
     * 打开工作空间
     */
    private void openWorkSpace() {
        openFile.setOnAction(event -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("请选择您的工作空间！");
            File file = fileChooser.showDialog(App.primaryStage);
            if (file == null) return;
            if (file.getPath().equals(lnbf.nowFile.getPath())) {
                WTMessage.sendWarning("请不要选择重复的工作空间!");
            } else {
                ThreadUtil.execute(() -> {
                    lnbf.toggleFile(file);
                });
            }
        });
    }

}
