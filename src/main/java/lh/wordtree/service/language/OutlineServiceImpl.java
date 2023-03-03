package lh.wordtree.service.language;

import cn.hutool.core.io.FileUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lh.wordtree.entity.Outline;
import lh.wordtree.ui.WLabel;
import lh.wordtree.views.core.NoteCoreView;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class OutlineServiceImpl implements OutlineService {
    private File nowFile;
    private BorderPane root = new BorderPane();
    /**
     * 中间编辑器
     */
    private CodeArea codeArea = new CodeArea();
    /**
     * 大事件
     */
    private ListView<Box> listView;
    /**
     * 新建
     */
    private Button newButton;
    /**
     * 关系网
     */
    private Button outLine;
    /**
     * 排序
     */
    private Button sort;

    public OutlineServiceImpl(File nowFile) {
        this.nowFile = nowFile;
    }

    public Node view() {
        this.pase();
        this.layout();
        this.controller();
        return root;
    }

    private void layout() {
        listView = new ListView<>();
        var leftView = new VBox();
        leftView.setPrefHeight(NoteCoreView.HEIGHT);
        {
            var box = new HBox();
            newButton = new Button("新建");
            outLine = new Button("关系图");
            sort = new Button("排序");
            box.setAlignment(Pos.CENTER);
            box.setSpacing(10);
            box.getChildren().addAll(newButton, outLine, sort);
            box.setPrefHeight(35);
            listView.prefHeightProperty().bind(leftView.heightProperty());
            leftView.getChildren().addAll(box, listView);
        }

        for (int i = 0; i < 10; i++) {
            var outline = new Outline();
            outline.setTitle("火烧国会");
            outline.setFigures(List.of("希特勒", "斯大林"));
            outline.setDescription("预示了希特勒最终夺得了权柄！");
            outline.setContent("预示了希特勒最终夺得了权柄！");
            var box = new Box(outline);
            listView.getItems().add(box);
        }

        // 完成对UI的绘制任务
        root.setLeft(leftView);
        root.setCenter(codeArea);
        // 中间的root
        var center = new VBox();
        {
            center.setPrefHeight(NoteCoreView.HEIGHT);
            codeArea.prefHeightProperty().bind(center.heightProperty());
            center.getChildren().addAll(codeArea);
        }
        root.setCenter(center);
    }

    /**
     * 解析原数据
     */
    private void pase() {
        // 读取该文件里面的内容
        var data = FileUtil.readString(nowFile, StandardCharsets.UTF_8);
        // 转化成java对象

    }

    private void controller() {
        // 添加对记事项选择事件
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.isNull(newValue)) return;
            codeArea.clear();
            codeArea.appendText(newValue.outline().getContent());
        });
        // 添加对大纲事件的显示,创建一个新的窗口
        outLine.setOnMouseClicked(e -> {
            new ShowOutLine().show();
        });
    }

    static class ShowOutLine extends Stage {
        public ShowOutLine() {
            var root = new BorderPane();
            var canvas = new Canvas();
            canvas.setHeight(150);
            canvas.setWidth(200);
            var context2D = canvas.getGraphicsContext2D();
            context2D.moveTo(30, 120);
            context2D.quadraticCurveTo(100, 20, 160, 120);
            context2D.stroke();
            root.setCenter(canvas);
            var scene = new Scene(root);
            this.setScene(scene);
        }
    }

    static class Box extends VBox {
        private Outline outline;

        public Box(Outline outline) {
            this.outline = outline;
            var children = this.getChildren();
            var title = new Label(outline.getTitle());
            title.setStyle("-fx-padding: 5 8 8 8;-fx-border-width: 0 0 1 0;-fx-border-color: #cccc;");
            var der = new Label(outline.getDescription());
            der.setStyle("-fx-padding: 5 8 2 8;-fx-border-width: 0 0 1 0;-fx-border-color: #cccc;");
            var hbox = new HBox();
            hbox.setStyle("-fx-padding: 5 8 2 8;");
            hbox.setSpacing(10);
            outline.getFigures().forEach(f -> {
                hbox.getChildren().add(new WLabel(f));
            });
            children.addAll(title, der, hbox);
        }

        public Outline outline() {
            return outline;
        }
    }

}
