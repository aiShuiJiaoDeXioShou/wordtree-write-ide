package lh.wordtree.views.nowfileoutline;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.comm.utils.ChineseNumberUtils;
import lh.wordtree.comm.BeanFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;

public class NowFileOutlineView extends VBox {
    private NowFileOutlineView() {
        // 段落，自然段
        ListView<Paragraphs> listView = new ListView<>();
        // 文字统计信息
        ListView<HBox> totalView = new ListView<>();
        // 获取当前工作空间的文件,每当文件发生更改的时候，它随着文件变化
        // 根据文件的内容解析出有效信息
        SimpleObjectProperty<File> nowFile = BeanFactory.nowFile;
        nowFile.addListener((observable, oldValue, newValue) -> {
            // 读取该文件内容
            String data = FileUtil.readString(newValue, StandardCharsets.UTF_8);
            ThreadUtil.execAsync(() -> {
                // 将该文件分割为不同的自然段
                String[] split = data.split("\n");
                // 遍历每一段为lisview附上值
                if (listView.getItems().size() > 0) {
                    Platform.runLater(() -> listView.getItems().removeIf(f -> true));
                }
                for (int i = 1; i <= split.length; i++) {
                    int finalI = i;
                    Platform.runLater(() -> {
                        Paragraphs paragraphs = new Paragraphs(split[finalI - 1], finalI);
                        listView.getItems().add(paragraphs);
                        paragraphs.setOnMouseClicked(e -> {
                            if (BeanFactory.nowCodeArea.getValue() != null) {
                                BeanFactory.nowCodeArea.getValue().moveTo(finalI - 1, 0);
                            }
                        });
                    });
                }
            });

            // 使用中文分词统计中文信息，所描写的主要人物
            ThreadUtil.execAsync(() -> {
                // 分词
                TokenizerEngine engine = TokenizerUtil.createEngine();
                LinkedHashMap<String, Integer> wordMap = new LinkedHashMap<>();
                Result parse = engine.parse(data);
                for (Word word : parse) {
                    if (word.getText().length() < 2) continue;
                    Integer integer = wordMap.get(word.getText());
                    if (Objects.isNull(integer)) {
                        wordMap.put(word.getText(), 1);
                        continue;
                    }
                    integer++;
                    wordMap.put(word.getText(), integer);
                }
                if (totalView.getItems().size() > 0) {
                    Platform.runLater(() -> totalView.getItems().removeIf(f -> true));
                }
                Platform.runLater(() -> {
                    wordMap.forEach((s, integer) -> {
                        HBox item = new HBox();
                        var no = new Label(s);
                        var number = new Label(String.valueOf(integer));
                        number.setStyle("-fx-text-fill: red");
                        item.getChildren().addAll(no, number);
                        item.setPadding(new Insets(5));
                        item.setSpacing(5);
                        totalView.getItems().add(item);
                    });
                });
            });
        });
        VBox top = new VBox();
        Label label = new Label("文字统计");
        label.setPadding(new Insets(10, 0, 10, 20));
        top.getChildren().addAll(label, totalView);

        VBox center = new VBox();
        Label label1 = new Label("段落详情");
        label.setPadding(new Insets(10, 0, 10, 20));
        center.getChildren().addAll(label1, listView);
        listView.setPrefHeight(400);
        TabPane tabPane = new TabPane();
        Tab dl = new Tab("Word");
        Tab word = new Tab("Paragraph");
        tabPane.getTabs().addAll(dl, word);
        dl.setContent(top);
        word.setContent(center);
        tabPane.setSide(Side.LEFT);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        this.getChildren().add(tabPane);
        this.setAlignment(Pos.BOTTOM_LEFT);
        tabPane.prefHeightProperty().bind(this.heightProperty());
        listView.prefHeightProperty().bind(this.heightProperty());
        totalView.prefHeightProperty().bind(this.heightProperty());
    }

    public static NowFileOutlineView newInstance() {
        return NowFileOutlineView.NowFileOutlineViewHolder.instance;
    }

    private static class Paragraphs extends VBox {
        public Paragraphs(String text, int index) {
            String s = ChineseNumberUtils.translateToChineseNumerals(index);
            var no = new Label("第" + s + "段：");
            var data = new Label(text);
            this.getChildren().addAll(no, data);
            this.setPadding(new Insets(10));
            this.setSpacing(10);
        }
    }

    private static class NowFileOutlineViewHolder {
        public static NowFileOutlineView instance = new NowFileOutlineView();
    }
}
