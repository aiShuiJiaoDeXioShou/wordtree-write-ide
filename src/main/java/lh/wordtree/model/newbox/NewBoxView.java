package lh.wordtree.model.newbox;

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
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.NovelProject;
import lh.wordtree.comm.utils.ConfigUtils;
import lh.wordtree.component.TreeDialog;
import lh.wordtree.ui.controls.WTInputPro;
import lh.wordtree.model.core.FileTreeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class NewBoxView extends TreeDialog {
    public SplitPane splitPane;
    public ListView<Label> listView;
    public BorderPane novel;
    private NewBoxController controller;

    public NewBoxView() {
        splitPane = new SplitPane();
        listView = new ListView<>();

        var label = new Label();
        label.setId("WordTree项目");
        label.setText("WordTree项目");
        ImageView icon = new ImageView("static/icon/icon.png");
        icon.setFitHeight(35);
        icon.setFitWidth(35);
        label.setGraphic(icon);
        listView.getItems().addAll(label);

        splitPane.setPrefSize(800,600);
        splitPane.setDividerPosition(0, 0.3);

        var scene = new Scene(splitPane);
        Config.setStyle(scene);
        this.setScene(scene);

        controller = new NewBoxController(this);
    }





}
