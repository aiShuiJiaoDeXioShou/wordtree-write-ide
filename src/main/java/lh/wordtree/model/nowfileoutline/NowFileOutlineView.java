package lh.wordtree.model.nowfileoutline;

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
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.comm.utils.ChineseNumberUtils;

import java.io.File;

public class NowFileOutlineView extends VBox {

    // 段落，自然段
    public ListView<Paragraphs> listView = new ListView<>();
    // 文字统计信息
    public ListView<HBox> totalView = new ListView<>();
    // 获取当前工作空间的文件,每当文件发生更改的时候，它随着文件变化
    // 根据文件的内容解析出有效信息
    public SimpleObjectProperty<File> nowFile = BeanFactory.nowFile;
    private NowFileOutlineViewController controller;

    private NowFileOutlineView() {
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
        controller = new NowFileOutlineViewController(this);
    }

    public static NowFileOutlineView newInstance() {
        return NowFileOutlineView.NowFileOutlineViewHolder.instance;
    }

    private static class NowFileOutlineViewHolder {
        public static NowFileOutlineView instance = new NowFileOutlineView();
    }

    static class Paragraphs extends VBox {
        public Paragraphs(String text, int index) {
            String s = ChineseNumberUtils.translateToChineseNumerals(index);
            var no = new Label("第" + s + "段：");
            var data = new Label(text);
            this.getChildren().addAll(no, data);
            this.setPadding(new Insets(10));
            this.setSpacing(10);
        }
    }
}
