package lh.wordtree.views.core;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import lh.wordtree.App;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.SystemMessage;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.language.CountryService;
import lh.wordtree.ui.controls.WTIcon;
import lh.wordtree.ui.controls.WTOneWindow;
import lh.wordtree.views.bookrack.UserInfoListView;
import lh.wordtree.views.newbox.NewProjectDialogView;

import java.io.File;
import java.util.Map;

public class MenuView extends BorderPane {
    //文件树
    private final FileTreeView lnbf = FileTreeView.newInstance();

    private MenuView() {
        this.getStyleClass().add("note-book-menu");
        this.setPadding(new Insets(0, 30, 0, 0));
        this.setRight(actionBar);
        this.controller();
    }

    private MenuItem openFile, newWorkSpace;

    public static MenuView newInstance() {
        return MenuViewHolder.instance;
    }

    public HBox actionBar = new HBox();
    // 中间显示工作空间状态
    public Label toggleWorkSpace = new Label();
    private Map<String, String> language = CountryService.language;
    private Menu file = new Menu(language.get("文件") + "(F)");
    private MenuItem revocation, returns, copy, paste;
    private Menu edit = new Menu(language.get("编辑") + "(E)");
    private Menu help = new Menu(language.get("帮助") + "(h)");
    private MenuBar menuBar = new MenuBar();
    private Label ok;
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    {
        openFile = new MenuItem(language.get("打开"));
        openFile.setGraphic(new WTIcon("static/icon/文件夹-打开-没文件.svg"));
        newWorkSpace = new MenuItem(language.get("新建"));
        newWorkSpace.setGraphic(new WTIcon("static/icon/新建.svg"));
        file.getItems().addAll(openFile, newWorkSpace);
    }

    {
        revocation = new MenuItem(language.get("撤销"));
        revocation.setGraphic(new WTIcon("static/icon/还原.svg"));
        returns = new MenuItem(language.get("返回"));
        returns.setGraphic(new WTIcon("static/icon/返回.svg"));
        copy = new MenuItem(language.get("复制"));
        copy.setGraphic(new WTIcon(new Image("static/icon/复制.png")));
        paste = new MenuItem(language.get("粘贴"));
        paste.setGraphic(new WTIcon(new Image("static/icon/粘贴.png")));
        edit.getItems().addAll(revocation, returns, copy, paste);
    }

    {
        var aboutAuthor = new MenuItem(language.get("关于作者"));
        aboutAuthor.setGraphic(new WTIcon(new Image("static/icon/作者.png")));
        var setting = new MenuItem(language.get("设置"));
        setting.setGraphic(new WTIcon(new Image("static/icon/设置_填充.png")));
        var version = new MenuItem(language.get("版本"));
        version.setGraphic(new WTIcon(new Image("static/icon/版本信息.png")));
        var study = new MenuItem(language.get("学习"));
        study.setGraphic(new WTIcon(new Image("static/icon/数位学习 (2).png")));
        help.getItems().addAll(aboutAuthor, setting, study, version);
    }

    {
        menuBar.getMenus().addAll(file, edit, help);
        this.setLeft(menuBar);
    }


    {
        // 这里有一个选择button
        ok = new Label();
        ok.getStyleClass().add("wt-button");
        ok.setGraphic(new WTIcon("static/icon/执行.svg"));
        // 创建选择框组件
        choiceBox.getItems().addAll("编译", "上传");
        choiceBox.setPrefWidth(90);
        choiceBox.getSelectionModel().select(0);
        actionBar.getChildren().addAll(choiceBox, ok);
        actionBar.setSpacing(10);
        ok.setOnMouseClicked(e -> {
            if (choiceBox.getValue().equals("编译")) {
                ThreadUtil.execAsync(()-> {
                    Platform.runLater(()-> ok.setGraphic(new WTIcon(new Image("static/icon/加载.png"))));
                    String wtRootPath = BeanFactory.nowRootFile.getValue().getPath();
                    String cmd = "%s -path %s -tmpl %s -create -outpath %s"
                            .formatted(
                                    Config.src("static/tool/build/main.exe").replace("file:/", ""),
                                    wtRootPath,
                                    Config.src("static/tool/build/book.tmpl").replace("file:/", ""),
                                    "%s\\.wordtree\\out".formatted(wtRootPath));
                    System.out.println(cmd);
                    RuntimeUtil.exec(cmd);
                    Platform.runLater(()-> ok.setGraphic(new WTIcon("static/icon/执行.svg")));
                });
            }
        });
    }

    {
        BeanFactory.nowRootFile.addListener((observable, oldValue, newValue) -> {
            toggleWorkSpace.setText(newValue.getName());
        });
        toggleWorkSpace.getStyleClass().add("toggle-work-space");
        this.setCenter(toggleWorkSpace);
        toggleWorkSpace.setOnMouseClicked(e -> {
            var stage = new WTOneWindow(language.get("历史工作空间"));
            stage.getRoot().getChildren().addAll(new UserInfoListView());
            stage.getLabel().setStyle("-fx-text-fill: #ffff");
            stage.getTop().setStyle("-fx-background-color: #495057");
            stage.show();
        });
    }

    private static class MenuViewHolder {
        public static MenuView instance = new MenuView();
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
                SystemMessage.sendWarning("请不要选择重复的工作空间!");
            } else {
                ThreadUtil.execute(() -> {
                    lnbf.toggleFile(file);
                });
            }
        });
    }

}
