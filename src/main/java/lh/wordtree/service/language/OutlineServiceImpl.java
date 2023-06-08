package lh.wordtree.service.language;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.archive.entity.Outline;
import lh.wordtree.ui.controls.WTIcon;
import lh.wordtree.ui.controls.WTInputPro;
import lh.wordtree.ui.controls.WTLabel;
import lh.wordtree.views.core.NoteCoreView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

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
    private static SimpleObjectProperty<List<Outline>> outlines = new SimpleObjectProperty<>(List.of());
    /**
     * 关系网
     */
    private WTIcon outLine;
    /**
     * 排序
     */
    private WTIcon sort;
    /**
     * 新建大纲
     */
    private WTIcon newIcon;
    private Outline nowOutline;


    public OutlineServiceImpl(File nowFile) {
        this.nowFile = nowFile;
    }

    public Node view() {
        this.layout();
        this.controller();
        this.pase();
        return root;
    }

    private void layout() {
        listView = new ListView<>();
        var leftView = new VBox();
        leftView.setPrefHeight(NoteCoreView.HEIGHT);
        {
            var box = new HBox();
            outLine = new WTIcon(new Image("static/icon/关系图.png"));
            sort = new WTIcon(new Image("static/icon/排序.png"));
            newIcon = new WTIcon(new Image("static/icon/新建.png"));
            box.setSpacing(10);
            box.getChildren().addAll(newIcon, outLine, sort);
            box.setPrefHeight(35);
            box.setPadding(new Insets(10));
            box.setStyle("-fx-background-color: #f0f3f9");
            listView.prefHeightProperty().bind(leftView.heightProperty());
            leftView.getChildren().addAll(box, listView);
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
        if (data.isBlank()) {
            FileUtil.writeString("[]", nowFile, StandardCharsets.UTF_8);
        }
        // 转化成java对象
        try {
            var outlines1 = JSON.parseArray(data, Outline.class);
            outlines.setValue(outlines1);
        } catch (Exception e) {
            System.err.println("文件格式异常！！！");
            e.printStackTrace();
        }
    }

    private void controller() {
        // 添加对记事项选择事件
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.isNull(newValue)) return;
            nowOutline = newValue.outline();
            codeArea.clear();
            codeArea.appendText(newValue.outline().getContent());
        });
        // 添加对大纲事件的显示,创建一个新的窗口
        outLine.setOnMouseClicked(e -> {
            new ShowOutLine().show();
        });
        // 新建记事
        newIcon.setOnMouseClicked(e -> {
            var stage = new AddOutline(outline -> {
                var ol = outlines.get();
                var list = new ArrayList<Outline>();
                ol.forEach(list::add);
                list.add(outline);
                outlines.setValue(list);
                return null;
            });
            stage.show();
        });
        outlines.addListener((observable, oldValue, newValue) -> {
            listView.getItems().removeIf(node -> true);
            newValue.forEach(outline -> {
                listView.getItems().add(new Box(outline));
            });
        });
        Nodes.addInputMap(codeArea, InputMap.consume(keyPressed(S, CONTROL_DOWN), event -> {
            nowOutline.setContent(codeArea.getText());
            FileUtil.writeUtf8String(JSON.toJSONString(outlines.get(), JSONWriter.Feature.PrettyFormat), nowFile);
        }));
    }

    static class AddOutline extends Stage {
        public AddOutline(Function<Outline, Void> function) {
            var stage = this;
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(15));

            var name = new WTInputPro("记事项目：");
            var der = new WTInputPro("详情描述：");
            var figures = new WTInputPro("参与人物：");
            var box = new VBox();
            box.setSpacing(15);
            box.getChildren().addAll(name, der, figures);
            root.setCenter(box);

            var save = new Button("保存");
            var bottom = new BorderPane();
            bottom.setRight(save);
            root.setBottom(bottom);
            save.setOnMouseClicked(e -> {
                var outline = new Outline();
                outline.setContent("");
                outline.setDescription(der.getTextField().getText());
                outline.setFigures(List.of(figures.getTextField().getText().split(",")));
                outline.setTitle(name.getTextField().getText());
                function.apply(outline);
            });
            var scene = new Scene(root, 500, 500);
            Config.setStyle(scene);
            stage.setScene(scene);
        }
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
                hbox.getChildren().add(new WTLabel(f));
            });
            children.addAll(title, der, hbox);
            var contextMenu = new ContextMenu();
            var del = new MenuItem("删除");
            var edit = new MenuItem("编辑");
            contextMenu.getItems().addAll(del, edit);
            this.setOnMouseClicked(event -> {
                if (event.getButton().name().equals(MouseButton.SECONDARY.name()))
                    contextMenu.show(this, Side.BOTTOM, 150, 0);
            });
            del.setOnAction(e -> {
                var value = outlines.getValue();
                var list2 = new ArrayList<Outline>();
                value.forEach(list2::add);
                list2.removeIf(outline1 -> outline1.getTitle().equals(outline.getTitle()));
                outlines.setValue(list2);
            });
        }

        public Outline outline() {
            return outline;
        }
    }

}
