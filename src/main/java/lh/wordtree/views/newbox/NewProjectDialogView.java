package lh.wordtree.views.newbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import lh.wordtree.App;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.NovelProject;
import lh.wordtree.comm.utils.ConfigUtils;
import lh.wordtree.component.TreeDialog;
import lh.wordtree.ui.controls.WTInputPro;
import lh.wordtree.views.core.FileTreeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class NewProjectDialogView extends TreeDialog {
    private SplitPane splitPane;
    private ListView<Label> listView;
    private BorderPane novel;

    public NewProjectDialogView() {
        splitPane = new SplitPane();
        listView = new ListView<>();
        {
            var label = new Label();
            label.setId("WordTree项目");
            label.setText("WordTree项目");
            ImageView icon = new ImageView("static/icon/icon.png");
            icon.setFitHeight(35);
            icon.setFitWidth(35);
            label.setGraphic(icon);
            listView.getItems().addAll(label);
        }
        splitPane.getItems().addAll(listView, novelInit());
        splitPane.setPrefSize(800,600);
        splitPane.setDividerPosition(0, 0.3);
        var scene = new Scene(splitPane);
        Config.setStyle(scene);
        this.setScene(scene);
        this.controller();
    }

    private BorderPane novelInit() {
        novel = new BorderPane();
        // 新建一个小说
        var choose = new Button("选择");
        var hBox = new WTInputPro("小说路径:",true,choose);
        var textField = hBox.getTextField();
        choose.setOnMouseClicked(e->{
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("请选择储存的地点！");
            File file = fileChooser.showDialog(this);
            if (file != null) textField.setText(file.getPath());
        });

        var hBox1 = new WTInputPro("名称:",true);
        var nameField = hBox1.getTextField();

        var chooseImg = new Button("选择图片路径");
        var hBox2 = new WTInputPro("封面:",true,chooseImg);
        AtomicReference<File> fileImage = new AtomicReference<>();
        chooseImg.setOnMouseClicked(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择封面的文件路径！");
            fileImage.set(fileChooser.showOpenDialog(this));
            if (fileImage.get() == null) return;
            System.out.println(fileImage.get().getPath());
            Image image;
            try {
                image = new Image(new FileInputStream(fileImage.get()),90,90,true,true);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            var imageView = new ImageView(image);
            hBox2.getChildren().add(imageView);
        });

        var hBox3 = new WTInputPro("题材:", true);
        var themeField = hBox3.getTextField();

        var hBox4 = new WTInputPro("目标字数:", true);
        var numberField = hBox4.getTextField();

        var hBox5 = new WTInputPro("简介:", true);
        var introductionField = hBox4.getTextField();

        var buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        var button = new Button("创建");
        buttons.getChildren().addAll(button);

        button.setOnMouseClicked(e -> {
            var novelProject = new NovelProject();
            var path = textField.getText() + "/" + nameField.getText();
            try {
                novelProject.setPath(path);
                novelProject.setAuthor(ConfigUtils.getProperties("username"));
                novelProject.setImg(fileImage.get().getPath());
                novelProject.setStartDateTime(LocalDateTime.now());
                novelProject.setTargetWeb("起点");
                novelProject.setTargetNumber(Integer.valueOf(numberField.getText()));
                novelProject.setTheme(themeField.getText());
                novelProject.setName(nameField.getText());
                novelProject.setBriefIntroduction(introductionField.getText());
            } catch (NullPointerException exception) {
                System.out.println("不能为空！");
            }
            var file = new File(path);
            Config.initWriteWorkSpace(file, novelProject);
            this.close();
            FileTreeView.newInstance().toggleFile(file);
        });

        var boxRoot = new VBox();
        boxRoot.getChildren().addAll(hBox1, hBox, hBox2, hBox3, hBox4, hBox5);
        boxRoot.setSpacing(15);
        novel.setTop(boxRoot);
        novel.setBottom(buttons);
        novel.setPadding(new Insets(20));
        return novel;
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "WordTree项目" -> toggle(novelInit());
            }
        });
    }

    public void toggle(Node node) {
        splitPane.getItems().remove(1);
        splitPane.getItems().add(1,node);
        splitPane.setDividerPosition(0, 0.3);
    }

}
