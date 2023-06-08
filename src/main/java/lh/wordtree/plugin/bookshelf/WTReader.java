package lh.wordtree.plugin.bookshelf;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.component.IconComponent;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class WTReader extends Stage {
    private File file;
    private BorderPane root;
    private Scene scene;
    private ListView<Label> titleEl;
    private WTReaderCoder readerCoder;
    private VirtualizedScrollPane<CodeArea> vsPane;
    private IconComponent setting;
    private IconComponent search;
    private IconComponent next;
    private IconComponent pre;
    private SimpleBooleanProperty visible = new SimpleBooleanProperty(false);

    public WTReader(@NotNull File file) {
        root = new BorderPane();
        scene = new Scene(root, 1000, 600);
        titleEl = new ListView<>();
        readerCoder = new WTReaderCoder();
        vsPane = new VirtualizedScrollPane<>(readerCoder);
        this.file = file;
        var sk = new StackPane();
        sk.getChildren().add(vsPane);

        setting = new IconComponent("static/icon/setting.png", "设置");
        sk.getChildren().add(setting);
        StackPane.setAlignment(setting, Pos.TOP_RIGHT);
        StackPane.setMargin(setting, new Insets(10, 20, 0, 0));

        search = new IconComponent("static/icon/搜索.png", "设置");
        sk.getChildren().add(search);
        StackPane.setAlignment(search, Pos.TOP_RIGHT);
        StackPane.setMargin(search, new Insets(30, 20, 0, 0));
        search.visibleProperty().bind(visible);

        next = new IconComponent("static/icon/next.png", "上一张", 33, 33);
        next.visibleProperty().bind(visible);
        sk.getChildren().add(next);
        StackPane.setAlignment(next, Pos.CENTER_RIGHT);

        pre = new IconComponent("static/icon/上一个.png", "上一个", 30, 30);
        pre.visibleProperty().bind(visible);
        sk.getChildren().add(pre);
        StackPane.setAlignment(pre, Pos.CENTER_LEFT);
        StackPane.setMargin(pre, new Insets(10, 20, 0, 0));

        VBox v = new VBox();
        HBox h1 = new HBox();
        {
            Label 亮度 = new Label("亮度");
            CheckBox checkBox = new CheckBox("跟随系统");
            Slider slider = new Slider();
            slider.setMin(1);
            slider.setMax(100);
            h1.getChildren().addAll(亮度, slider, checkBox);
            h1.setSpacing(10);
            h1.setAlignment(Pos.CENTER_LEFT);
        }
        HBox h2 = new HBox();
        {
            Label 字号 = new Label("字号");
            Label font = new Label("18");
            Button add = new Button("+");
            Button jian = new Button("-");
            Button bt = new Button("系统字体");
            bt.setGraphic(new IconComponent("static/icon/上一个.png", "上一个"));
            h2.getChildren().addAll(字号, add, font, jian, bt);
            h2.setSpacing(10);
            h2.setAlignment(Pos.CENTER_LEFT);
        }
        HBox h3 = new HBox();
        {
            Label 背景 = new Label("背景");
            ListView<Label> list = new ListView<>();
            Label 青色 = new Label("青色");
            Label 白色 = new Label("白色");
            Label 黄色 = new Label("黄色");
            list.setOrientation(Orientation.HORIZONTAL);
            list.getItems().addAll(青色, 白色, 黄色);
            h3.getChildren().addAll(背景, list);
            h3.setSpacing(10);
            h3.setAlignment(Pos.CENTER_LEFT);
        }
        v.getChildren().addAll(h1, h2, h3);
        v.setSpacing(8);
        v.setStyle("-fx-padding: 10;-fx-background-color: #f8f9fb;-fx-fill: wite;-fx-max-height: 130;-fx-pref-width: 100");
        v.visibleProperty().bind(visible);
        sk.getChildren().add(v);
        StackPane.setAlignment(v, Pos.BOTTOM_CENTER);

        String bookTitle = WTFileUtils.fileName(file);
        readerCoder.replaceText(bookTitle);
        this.setTitle(bookTitle);
        this.setScene(scene);
        Config.setStyle(scene);

        SplitPane pane = new SplitPane();
        pane.getItems().addAll(titleEl, sk);
        root.setCenter(pane);
        pane.setDividerPosition(0, 0.2);
        ReaderTextBuilder build = ReaderTextBuilder.builder(file);
        List<String> titles = build.findTitle();
        for (String title : titles) {
            Label label = new Label(title);
            titleEl.getItems().add(label);
        }

        titleEl.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    String content = build.findTitleByContent(newValue.getText());
                    readerCoder.replaceText(content);
                });

        readerCoder.setOnMouseClicked(event -> {
            visible.setValue(!visible.get());
        });
    }

}
