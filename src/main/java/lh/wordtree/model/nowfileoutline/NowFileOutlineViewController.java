package lh.wordtree.model.nowfileoutline;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.wordtree.comm.BeanFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;

public class NowFileOutlineViewController {
    private NowFileOutlineView view;
    public NowFileOutlineViewController(NowFileOutlineView view) {
        this.view = view;
        view.nowFile.addListener(this::changeFile);
    }

    private void changeFile(ObservableValue<? extends File> observableValue, File file, File newValue) {
        // 读取该文件内容
        String data = FileUtil.readString(newValue, StandardCharsets.UTF_8);
        this.totalParagraph(data);
        this.totalWord(data);
    }

    // 统计自然段
    private void totalParagraph(String data) {
        ThreadUtil.execAsync(() -> {
            // 将该文件分割为不同的自然段
            String[] split = data.split("\n");
            // 遍历每一段为lisview附上值
            if (view.listView.getItems().size() > 0) {
                Platform.runLater(() -> view.listView.getItems().removeIf(f -> true));
            }
            for (int i = 1; i <= split.length; i++) {
                int finalI = i;
                Platform.runLater(() -> {
                    NowFileOutlineView.Paragraphs paragraphs = new NowFileOutlineView.Paragraphs(split[finalI - 1], finalI);
                    view.listView.getItems().add(paragraphs);
                    paragraphs.setOnMouseClicked(e -> {
                        if (BeanFactory.nowCodeArea.getValue() != null) {
                            BeanFactory.nowCodeArea.getValue().moveTo(finalI - 1, 0);
                        }
                    });
                });
            }
        });
    }

    // 统计单词
    private void totalWord(String data) {
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
            if (view.totalView.getItems().size() > 0) {
                Platform.runLater(() -> view.totalView.getItems().removeIf(f -> true));
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
                    view.totalView.getItems().add(item);
                });
            });
        });
    }

}
